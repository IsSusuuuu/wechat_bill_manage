package com.susu.service;

import com.susu.Result.PageResult;
import com.susu.domain.dto.RuleQueryDTO;
import com.susu.domain.entity.CategoryRule;
import java.util.List;
import java.util.Map;

/**
 * 分类规则服务接口
 */
public interface CategoryRuleService {
    
    /**
     * 获取所有规则
     */
    List<CategoryRule> getAllRules();
    
    /**
     * 获取启用的规则
     */
    List<CategoryRule> getEnabledRules();
    
    /**
     * 根据ID获取规则
     */
    CategoryRule getRuleById(Long id);
    
    /**
     * 创建规则
     */
    CategoryRule createRule(CategoryRule rule);
    
    /**
     * 更新规则
     */
    CategoryRule updateRule(CategoryRule rule);
    
    /**
     * 删除规则
     */
    void deleteRule(Long id);
    
    /**
     * 测试规则匹配
     */
    Map<String, Object> testRule(Long ruleId, String testText);
    
    /**
     * 获取规则统计信息
     */
    List<Map<String, Object>> getRuleStatistics();
    
    /**
     * 刷新规则缓存
     */
    void refreshRuleCache();
    
    /**
     * 批量导入规则
     */
    Map<String, Object> batchImportRules(List<CategoryRule> rules);
    
    /**
     * 导出规则
     */
    String exportRules(String format);
    
    /**
     * 根据关键词搜索规则
     */
    List<CategoryRule> searchRules(String keyword);
    
    /**
     * 分页查询规则
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(RuleQueryDTO queryDTO);
}
