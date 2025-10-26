package com.susu.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillCategory {
    private Long id;
    private String categoryName; // 分类名称（如“综合电商”）
    private Long parentId;       // 父分类ID
    private Integer level; // 层级（1/2）
    private Integer type;   //0:支出 1:收入 2:转账
    private Integer isDefault; // 是否默认分类 0:否 1:是
    private Integer sort; // 排序
    private LocalDateTime createTime; // 创建时间
}