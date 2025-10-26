package com.susu.service.impl;

import com.susu.domain.entity.ImportSession;
import com.susu.domain.entity.WechatBill;
import com.susu.domain.vo.BillPreviewVO;
import com.susu.domain.vo.BillProcessResultVO;
import com.susu.mapper.BillCategoryMapper;
import com.susu.mapper.ImportSessionMapper;
import com.susu.mapper.WechatBillMapper;
import com.susu.service.BillCategoryService;
import com.susu.service.ImportSessionService;
import com.susu.service.XlsxParseService;
import com.susu.utils.HashUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信账单 XLSX 专属解析服务
 */
@Service
public class XlsxParseServiceImpl implements XlsxParseService {
    @Autowired
    private BillCategoryService billCategoryService;
    @Autowired
    private BillCategoryMapper billCategoryMapper;
    @Autowired
    private ImportSessionMapper importSessionMapper;
    @Autowired
    private WechatBillMapper wechatBillMapper;
    @Autowired
    private ImportSessionService importSessionService;

    // 微信账单关键配置：根据你的账单实际列调整（以下为默认列索引，0 代表第一列）
    private static final int COL_TRANSACTION_TIME = 0;  // 第 1 列：交易时间
    private static final int COL_TRANSACTION_TYPE = 1;  // 第 2 列：交易类型
    private static final int COL_COUNTERPARTY = 2;       // 第 3 列：交易对方
    private static final int COL_PRODUCT = 3;            // 第 4 列：商品说明
    private static final int COL_PRODUCT_TYPE = 4;       // 第 5 列：收支类型
    private static final int COL_AMOUNT = 5;             // 第 6 列：金额(元)
    private static final int COL_PAYMENT_METHOD = 6;     // 第 7 列：支付方式
    private static final int COL_STATUS = 7;             // 第 8 列：当前状态
    private static final int COL_TRANSACTION_ID = 8;     // 第 9 列：交易单号
    private static final int COL_MERCHANT_ORDER_ID = 9;  // 第 10 列：商户单号
    private static final int COL_REMARK = 10;             // 第 11 列：备注

    // 微信账单日期格式（固定为 yyyy-MM-dd HH:mm:ss，如“2024-10-01 12:30:45”）
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析微信账单 XLSX 文件，返回所有账单条目列表
     *
     * @param file 前端上传的微信账单 XLSX 文件
     * @return 微信账单条目列表（每一条为 WechatBill 对象）
     */
    @Override
    public BillProcessResultVO parseXlsxFile(MultipartFile file) throws IOException, ParseException {

        BillProcessResultVO billProcessResultVO = new BillProcessResultVO();
        List<WechatBill> billList = new ArrayList<>();

        // 1. 校验文件格式
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".xlsx")) {
            throw new IllegalArgumentException("请上传微信账单的 .xlsx 格式文件");
        }

        // 2. 读取 XLSX 文件流
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // 微信账单通常只有 1 个工作表，直接取第一个
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new RuntimeException("账单文件中无工作表");
            }

            // 3. 遍历行：跳过前 N 行表头（微信账单前 3-4 行为说明，需根据实际调整起始行）
            int startRow = 17;  // 示例：从第 18 行开始解析（0 为第一行，4 即第 5 行）
            int lastRowNum = sheet.getLastRowNum();  // 最后一行的索引
            int duplicateRowNum = 0;
            Integer sessionId = importSessionMapper.getImportSessionCount();
                if (sessionId == null) {
                    sessionId = 0;
                }
            // 4. 跳过汇总行（最后一行为“合计”，需过滤）
            for (int rowIndex = startRow; rowIndex < lastRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue; // 跳过空行
                }

                // 5. 解析当前行，封装为 WechatBill 对象
                WechatBill bill = parseSingleBillRow(row);
                if(bill==null){
                    duplicateRowNum++;
                    continue;
                }

                bill.setSessionId(sessionId+1);
                bill.setRowIndex(rowIndex-startRow+1);//从1开始
                billList.add(bill);

            }

            billProcessResultVO.setDuplicates(duplicateRowNum);
            billProcessResultVO.setTotal(lastRowNum-startRow);
            billProcessResultVO.setInserted(billList.size());
            billProcessResultVO.setErrors(lastRowNum-startRow-billList.size()-duplicateRowNum);
            billProcessResultVO.setPreview(billList.stream().map(bill -> {
                BillPreviewVO previewVO = new BillPreviewVO();
                previewVO.setRowIndex(bill.getRowIndex());
                previewVO.setOccurredAt(DATE_FORMAT.format(bill.getTransactionTime()));
                previewVO.setRecordType(bill.getTransactionType());
                previewVO.setCategoryName(billCategoryService.getCategoryName(bill.getFirstCategoryId(), bill.getSecondCategoryId()));
                previewVO.setAmount(bill.getAmount().toString());
                previewVO.setPaymentMethod(bill.getPaymentMethod());
                previewVO.setMerchant(bill.getCounterparty());
                previewVO.setNote(bill.getRemark());
                previewVO.setStatus(bill.getStatus());
                return previewVO;
            }).collect(Collectors.toList()));
            ImportSession importSession = new ImportSession();
            importSession.setCurId(sessionId+1);
            importSession.setSourceType("微信账单xlsx");
            importSession.setFileName(originalFilename);
            importSession.setTotalRows(lastRowNum-startRow);
            importSession.setInsertedRows(billList.size());
            importSession.setDuplicateRows(duplicateRowNum);
            importSession.setErrorRows(lastRowNum-startRow-billList.size()-duplicateRowNum);
            importSession.setImportTime(LocalDateTime.now());
            importSessionService.addImportSession(importSession);
            for(WechatBill bill : billList){wechatBillMapper.insertWechatBill(bill);}
        }

        return billProcessResultVO;
    }

    /**
     * 解析单行账单数据（将 Excel 行转为 WechatBill 实体）
     */
    private WechatBill parseSingleBillRow(Row row) throws ParseException {
        WechatBill bill = new WechatBill();

        // --------------- 1. 解析交易时间（关键：格式转换）---------------
        String timeStr = getCellValue(row.getCell(COL_TRANSACTION_TIME));
        if (timeStr.isEmpty()) {
            return null; // 无交易时间的行视为无效行，跳过
        }
        Date transactionTime = DATE_FORMAT.parse(timeStr);
        bill.setTransactionTime(transactionTime);

        // --------------- 2. 解析交易类型 ---------------
        String transactionType = getCellValue(row.getCell(COL_TRANSACTION_TYPE));
        bill.setTransactionType(transactionType);

        // --------------- 3. 解析交易对方 ---------------
        String counterparty = getCellValue(row.getCell(COL_COUNTERPARTY));
        bill.setCounterparty(counterparty);

        // --------------- 4. 解析商品说明 ---------------
        String product = getCellValue(row.getCell(COL_PRODUCT));
        bill.setProduct(product);

        // --------------- 5. 解析金额（关键：处理正负号，区分收支）---------------
        String amountStr = getCellValue(row.getCell(COL_AMOUNT));
        BigDecimal amount = new BigDecimal(amountStr.substring(1));
        bill.setAmount(amount);
        //---------------- 6. 解析收支类型 ---------------
        String productType = getCellValue(row.getCell(COL_PRODUCT_TYPE));
        bill.setProductType(productType);

        // --------------- 7. 解析支付方式 ---------------
        String paymentMethod = getCellValue(row.getCell(COL_PAYMENT_METHOD));
        bill.setPaymentMethod(paymentMethod);

        // --------------- 8. 解析交易单号 ---------------
        String transactionId = getCellValue(row.getCell(COL_TRANSACTION_ID));
        bill.setTransactionId(transactionId);

        // --------------- 9. 解析商户单号（非必选，可能为空）---------------
        String merchantOrderId = getCellValue(row.getCell(COL_MERCHANT_ORDER_ID));
        bill.setMerchantOrderId(merchantOrderId);

        // --------------- 10. 解析备注（非必选，可能为空）---------------
        String remark = getCellValue(row.getCell(COL_REMARK));
        if (remark.isEmpty()) {
            remark="无";
        }
        bill.setRemark(remark);
        // --------------- 11. 解析当前状态 ---------------
        String status = getCellValue(row.getCell(COL_STATUS));
        bill.setStatus(status);
        // --------------- 12. 生成专属hash值 ---------------
        String uniqueKey=timeStr+product+transactionId;
        String billHash= HashUtils.generateMd5Hash(uniqueKey);
        bill.setBillHash(billHash);

        // --------------- 13. 检查哈希值是否重复 ---------------
        int count = wechatBillMapper.countByBillHash(billHash);
        if (count > 0) {
            // 哈希值重复，说明该账单已存在，跳过
            return null;
        }
        // --------------- 14. 自动分类 ---------------
        billCategoryService.autoClassify(bill);



        return bill;
    }

    /**
     * 通用工具：获取 Excel 单元格的值（处理所有数据类型）
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                // 字符串类型：直接取文本，去除空格
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 数字类型：微信账单中金额为数字，转为字符串（避免科学计数法）
                return String.valueOf(cell.getNumericCellValue()).trim();
            case BOOLEAN:
                // 布尔类型：转为“true/false”字符串
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // 公式类型：取公式计算结果
                return getCellValue(cell); // 递归获取结果
            default:
                return "";
        }
    }
}