package com.susu.domain.dto;

import lombok.Data;

@Data
public class RuleQueryDTO {
    private String keyword;      // 关键词（模糊搜索规则名或关键词）
    private Integer page = 1;    // 当前页（默认1）
    private Integer pageSize = 10; // 每页条数（默认10）
}
