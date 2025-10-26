package com.susu.domain.dto;

import lombok.Data;

@Data
public class BillQueryDTO {
    private String type;         // 交易类型
    private String from;         // 开始日期
    private String to;           // 结束日期
    private String text;         // 关键词
    private String categoryId;   // 分类ID
    private Integer page = 1;    // 当前页（默认1）
    private Integer pageSize = 10; // 每页条数（默认10）
}