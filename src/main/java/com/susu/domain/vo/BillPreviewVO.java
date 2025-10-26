package com.susu.domain.vo; // 建议放在 VO（View Object）包下，专门用于前端返回数据

import lombok.Data;
import java.io.Serializable;

/**
 * 前端所需的 preview 数组中的单个账单预览项
 */
@Data
public class BillPreviewVO implements Serializable {
    // 字段名需与前端要求完全一致（rowIndex、occurredAt 等）
    private Integer rowIndex;      // 行号（对应前端 preview 中的 rowIndex）
    private String occurredAt;     // 发生时间（建议用 String 避免日期格式问题，如 "2024-10-01 12:30:45"）
    private String recordType;     // 记录类型（如 "收入"、"支出"）
    private String categoryName;   // 分类名称（如 "餐饮"、"转账"）
    private String amount;         // 金额（用 String 避免小数精度问题，如 "100.00"）
    private String paymentMethod;    // 支付方式（如 "微信零钱"）
    private String merchant;       // 商户名称（如 "星巴克"，无则为 ""）
    private String note;           // 备注（无则为 ""）
    private String status;         // 状态（如 "成功"、"失败"）
}