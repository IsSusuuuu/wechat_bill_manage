package com.susu.domain.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 微信账单实体类（对应 XLSX 中的一条账单条目）
 */
@Data // Lombok 注解，自动生成 getter/setter/toString，需引入 Lombok 依赖
public class WechatBill {
    // 微信账单固定列：根据实际账单调整字段（以下为常见列，可按需增删）
    private Date transactionTime;    // 交易时间（对应账单“交易时间”列）
    private String transactionType;  // 交易类型（对应“交易类型”列，如“微信红包”“转账”）
    private String counterparty;     // 交易对方（对应“交易对方”列，如好友昵称、商户名）
    private String product;          // 商品说明（对应“商品说明”列）
    private String productType;       // 收支类型（自定义：根据金额正负判断“收入”/“支出”）
    private BigDecimal amount;       // 金额（对应“金额(元)”列，区分收入/支出）
    private String paymentMethod;    // 支付方式（对应“支付方式”列，如“零钱”“银行卡”）
    private String status;           // 当前状态（支付成功/已全额退款等）
    private String transactionId;    // 交易单号（对应“交易单号”列）
    private String merchantOrderId;  // 商户单号（对应“商户单号”列，非必选）
    private String remark;           // 备注（对应“备注”列，非必选）

    private Integer id;             // 主键ID（自增）
    private Integer sessionId;        // 导入批次ID（对应 import_session.id）
    private Integer rowIndex;        // 导入行号（对应原始XLSX中的行号，从1开始）
    private String billHash;         // 账单哈希值（唯一标识一条账单，用于去重）
    private LocalDateTime importTime; // 导入时间（记录导入到数据库的时间）
    private Long firstCategoryId;         // 一级分类ID（对应账单分类，如“餐饮美食”“交通出行”）
    private Long secondCategoryId;         // 二级分类ID（对应账单分类，如“餐饮美食”“交通出行”）
}