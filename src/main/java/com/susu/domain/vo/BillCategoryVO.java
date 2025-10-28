package com.susu.domain.vo;

import lombok.Data;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Data
public class BillCategoryVO {
    private Long id;          // 分类ID（对应前端c.id）
    private String name;      // 分类名称（对应前端c.name）
    private String categoryType; // 分类类型（对应前端c.categoryType，如EXPENSE/INCOME/TRANSFER）
    private Long parentId;    // 父分类ID
    private String parentName; // 父分类名称
    private Integer level;    // 层级（1/2/3）
    private Integer sort;     // 排序
    private Integer isDefault; // 是否默认分类
    private String createTime; // 创建时间
}