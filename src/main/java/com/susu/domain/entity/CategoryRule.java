package com.susu.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryRule {
    private Long id;
    private String ruleName;         // 规则名称
    private String description;      // 规则描述
    
    // 匹配配置
    private String keyword;          // 匹配关键字
    private Integer matchType;       // 匹配类型（1=包含，2=完全等于，3=正则，4=前缀，5=后缀）
    private String regexPattern;     // 正则表达式
    private String excludeKeyword;   // 排除关键字
    private String matchFields;      // 匹配字段（counterparty,product,remark）
    
    // 分类配置
    private Long firstCategoryId;    // 一级分类ID
    private Long secondCategoryId;   // 二级分类ID
    private String categoryPath;     // 分类路径
    
    // 优先级和状态
    private Integer priority;        // 优先级（1最高）
    private Integer isEnabled;       // 是否启用（1=启用，0=禁用）
    private Integer isSystem;        // 是否系统规则（1=系统，0=用户）
    
    // 统计信息
    private Integer matchCount;      // 匹配次数
    private Integer successCount;    // 成功分类次数
    private Double successRate;      // 成功率
    
    // 时间信息
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastUsedTime; // 最后使用时间
}
