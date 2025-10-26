package com.susu.controller;

import com.susu.Result.Result;
import com.susu.domain.entity.WechatBill;
import com.susu.domain.vo.BillPreviewVO;
import com.susu.domain.vo.BillProcessResultVO;
import com.susu.service.XlsxParseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@RestController
@RequestMapping("/api/import")
@Tag(name = "微信账单导入接口",description = "提供微信账单导入相关接口")
public class ImportController {
    @Autowired
    private  XlsxParseService xlsxParseService;



    @PostMapping("/wechat-xlsx")
    public Result<BillProcessResultVO> importWechatBill(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.isEmpty()){
            return Result.error("文件为空");
        }
        if(!file.getOriginalFilename().endsWith(".xlsx")){
            return Result.error("文件格式错误");
        }

        // 解析文件
        BillProcessResultVO billProcessResultVO = xlsxParseService.parseXlsxFile(file);
        return Result.success(billProcessResultVO);
    }
}
