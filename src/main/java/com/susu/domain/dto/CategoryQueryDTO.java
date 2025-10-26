package com.susu.domain.dto;

import lombok.Data;

@Data
public class CategoryQueryDTO {
    private String name;         // 分类名称（模糊搜索）
    private String type;         // 分类类型（支出、收入、转账）
    private Integer page = 1;    // 当前页（默认1）
    private Integer pageSize = 10; // 每页条数（默认10）
}
