package com.susu.domain.dto;

import java.time.LocalDateTime;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
public class RecordDTO {
    private Long id;
    private LocalDateTime txnTime;
    private String txnType;
    private double amount;
    private String note;
    private String counterparty;
}
