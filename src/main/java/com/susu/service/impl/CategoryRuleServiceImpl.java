package com.susu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.susu.Result.PageResult;
import com.susu.domain.dto.RuleQueryDTO;
import com.susu.domain.entity.CategoryRule;
import com.susu.domain.entity.BillCategory;
import com.susu.mapper.CategoryRuleMapper;
import com.susu.mapper.BillCategoryMapper;
import com.susu.service.CategoryRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 分类规则服务实现
 */
@Service
public class CategoryRuleServiceImpl implements CategoryRuleService {
    
    @Autowired
    private CategoryRuleMapper ruleMapper;
    
    @Autowired
    private BillCategoryMapper categoryMapper;
    
    @Override
    public List<CategoryRule> getAllRules() {
        return ruleMapper.selectAll();
    }
    
    @Override
    public List<CategoryRule> getEnabledRules() {
        return ruleMapper.selectAllEnabledRules();
    }
    
    @Override
    public CategoryRule getRuleById(Long id) {
        return ruleMapper.selectById(id);
    }
    
    @Override
    public CategoryRule createRule(CategoryRule rule) {
        System.out.println("开始创建规则: " + rule);
        
        // 设置默认值
        if (rule.getCreateTime() == null) {
            rule.setCreateTime(LocalDateTime.now());
        }
        if (rule.getUpdateTime() == null) {
            rule.setUpdateTime(LocalDateTime.now());
        }
        if (rule.getIsEnabled() == null) {
            rule.setIsEnabled(1);
        }
        if (rule.getIsSystem() == null) {
            rule.setIsSystem(0);
        }
        if (rule.getMatchCount() == null) {
            rule.setMatchCount(0);
        }
        if (rule.getSuccessCount() == null) {
            rule.setSuccessCount(0);
        }
        if (rule.getSuccessRate() == null) {
            rule.setSuccessRate(0.0);
        }
        if (rule.getPriority() == null) {
            rule.setPriority(100);
        }
        if (!StringUtils.hasText(rule.getMatchFields())) {
            rule.setMatchFields("counterparty,product,remark");
        }
        
        // 设置分类路径
        System.out.println("设置分类路径前: " + rule.getCategoryPath());
        setCategoryPath(rule);
        System.out.println("设置分类路径后: " + rule.getCategoryPath());
        
        // 验证规则
        System.out.println("开始验证规则...");
        validateRule(rule);
        System.out.println("规则验证通过");
        
        // 插入规则
        System.out.println("准备插入规则到数据库: " + rule);
        int result = ruleMapper.insert(rule);
        System.out.println("数据库插入结果: " + result);
        
        if (result > 0) {
            System.out.println("规则创建成功，ID: " + rule.getId());
            return rule;
        } else {
            System.err.println("数据库插入失败，影响行数: " + result);
            throw new RuntimeException("创建规则失败");
        }
    }
    
    @Override
    public CategoryRule updateRule(CategoryRule rule) {
        // 验证规则是否存在
        CategoryRule existingRule = ruleMapper.selectById(rule.getId());
        if (existingRule == null) {
            throw new RuntimeException("规则不存在");
        }
        
        // 验证规则
        validateRule(rule);
        
        // 设置更新时间
        rule.setUpdateTime(LocalDateTime.now());
        
        // 更新规则
        int result = ruleMapper.update(rule);
        if (result > 0) {
            return ruleMapper.selectById(rule.getId());
        } else {
            throw new RuntimeException("更新规则失败");
        }
    }
    
    @Override
    public void deleteRule(Long id) {
        // 验证规则是否存在
        CategoryRule rule = ruleMapper.selectById(id);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        
        // 检查是否为系统规则
        if (rule.getIsSystem() != null && rule.getIsSystem() == 1) {
            throw new RuntimeException("系统规则不能删除");
        }
        
        // 删除规则
        int result = ruleMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除规则失败");
        }
    }
    
    @Override
    public Map<String, Object> testRule(Long ruleId, String testText) {
        CategoryRule rule = ruleMapper.selectById(ruleId);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("ruleId", ruleId);
        result.put("testText", testText);
        result.put("ruleName", rule.getRuleName());
        
        try {
            boolean isMatch = matchRule(testText, rule);
            result.put("isMatch", isMatch);
            result.put("matchType", getMatchTypeName(rule.getMatchType()));
            result.put("keyword", rule.getKeyword());
            result.put("excludeKeyword", rule.getExcludeKeyword());
            
            if (isMatch) {
                result.put("categoryId", rule.getFirstCategoryId());
                result.put("categoryPath", rule.getCategoryPath());
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("isMatch", false);
        }
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getRuleStatistics() {
        List<CategoryRule> rules = ruleMapper.selectAll();
        List<Map<String, Object>> statistics = new ArrayList<>();
        
        for (CategoryRule rule : rules) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("id", rule.getId());
            stat.put("ruleName", rule.getRuleName());
            stat.put("keyword", rule.getKeyword());
            stat.put("matchCount", rule.getMatchCount());
            stat.put("successCount", rule.getSuccessCount());
            stat.put("successRate", rule.getSuccessRate());
            stat.put("lastUsedTime", rule.getLastUsedTime());
            stat.put("isEnabled", rule.getIsEnabled());
            statistics.add(stat);
        }
        
        return statistics;
    }
    
    @Override
    public void refreshRuleCache() {
        // 这里可以通知CategoryRuleHolder刷新缓存
        // 由于CategoryRuleHolder是@Component，可以通过事件或直接调用
        // 暂时留空，后续可以通过ApplicationContext获取CategoryRuleHolder实例
    }
    
    @Override
    public List<CategoryRule> searchRules(String keyword) {
        return ruleMapper.selectByKeyword(keyword);
    }
    
    @Override
    public Map<String, Object> batchImportRules(List<CategoryRule> rules) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (CategoryRule rule : rules) {
            try {
                createRule(rule);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errors.add("规则 " + rule.getRuleName() + " 导入失败: " + e.getMessage());
            }
        }
        
        result.put("total", rules.size());
        result.put("success", successCount);
        result.put("fail", failCount);
        result.put("errors", errors);
        
        return result;
    }
    
    @Override
    public String exportRules(String format) {
        List<CategoryRule> rules = ruleMapper.selectAll();
        
        if ("json".equalsIgnoreCase(format)) {
            // 简单的JSON导出
            StringBuilder json = new StringBuilder();
            json.append("[\n");
            for (int i = 0; i < rules.size(); i++) {
                CategoryRule rule = rules.get(i);
                json.append("  {\n");
                json.append("    \"ruleName\": \"").append(rule.getRuleName()).append("\",\n");
                json.append("    \"keyword\": \"").append(rule.getKeyword()).append("\",\n");
                json.append("    \"matchType\": ").append(rule.getMatchType()).append(",\n");
                json.append("    \"firstCategoryId\": ").append(rule.getFirstCategoryId()).append(",\n");
                json.append("    \"secondCategoryId\": ").append(rule.getSecondCategoryId()).append(",\n");
                json.append("    \"priority\": ").append(rule.getPriority()).append("\n");
                json.append("  }");
                if (i < rules.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("]");
            return json.toString();
        } else {
            // CSV格式
            StringBuilder csv = new StringBuilder();
            csv.append("规则名称,关键词,匹配类型,一级分类ID,二级分类ID,优先级\n");
            for (CategoryRule rule : rules) {
                csv.append(rule.getRuleName()).append(",");
                csv.append(rule.getKeyword()).append(",");
                csv.append(rule.getMatchType()).append(",");
                csv.append(rule.getFirstCategoryId()).append(",");
                csv.append(rule.getSecondCategoryId()).append(",");
                csv.append(rule.getPriority()).append("\n");
            }
            return csv.toString();
        }
    }
    
    /**
     * 验证规则
     */
    private void validateRule(CategoryRule rule) {
        if (!StringUtils.hasText(rule.getRuleName())) {
            throw new RuntimeException("规则名称不能为空");
        }
        
        if (!StringUtils.hasText(rule.getKeyword()) && !StringUtils.hasText(rule.getRegexPattern())) {
            throw new RuntimeException("关键词或正则表达式不能同时为空");
        }
        
        if (rule.getMatchType() == null || rule.getMatchType() < 1 || rule.getMatchType() > 5) {
            throw new RuntimeException("匹配类型无效");
        }
        
        if (rule.getMatchType() == 3 && !StringUtils.hasText(rule.getRegexPattern())) {
            throw new RuntimeException("正则匹配类型必须提供正则表达式");
        }
        
        if (rule.getFirstCategoryId() == null) {
            throw new RuntimeException("一级分类ID不能为空");
        }
        
        if (rule.getPriority() == null || rule.getPriority() < 1 || rule.getPriority() > 999) {
            throw new RuntimeException("优先级必须在1-999之间");
        }
    }
    
    /**
     * 匹配规则
     */
    private boolean matchRule(String text, CategoryRule rule) {
        if (!StringUtils.hasText(text)) {
            return false;
        }
        
        // 检查排除关键词
        if (StringUtils.hasText(rule.getExcludeKeyword()) && text.contains(rule.getExcludeKeyword())) {
            return false;
        }
        
        // 根据匹配类型进行匹配
        switch (rule.getMatchType()) {
            case 1: // 包含
                return text.contains(rule.getKeyword());
            case 2: // 完全等于
                return text.equals(rule.getKeyword());
            case 3: // 正则表达式
                if (StringUtils.hasText(rule.getRegexPattern())) {
                    Pattern pattern = Pattern.compile(rule.getRegexPattern());
                    return pattern.matcher(text).find();
                }
                return false;
            case 4: // 前缀匹配
                return text.startsWith(rule.getKeyword());
            case 5: // 后缀匹配
                return text.endsWith(rule.getKeyword());
            default:
                return false;
        }
    }
    
    /**
     * 获取匹配类型名称
     */
    private String getMatchTypeName(Integer matchType) {
        if (matchType == null) return "未知";
        switch (matchType) {
            case 1: return "包含";
            case 2: return "完全等于";
            case 3: return "正则表达式";
            case 4: return "前缀匹配";
            case 5: return "后缀匹配";
            default: return "未知";
        }
    }
    
    /**
     * 设置分类路径
     */
    private void setCategoryPath(CategoryRule rule) {
        if (rule.getFirstCategoryId() == null) {
            return;
        }
        
        try {
            StringBuilder path = new StringBuilder();
            
            // 查询一级分类
            BillCategory firstCategory = categoryMapper.selectById(rule.getFirstCategoryId());
            if (firstCategory != null) {
                String typeName = getCategoryTypeName(firstCategory.getType());
                path.append(typeName).append("/").append(firstCategory.getCategoryName());
                
                // 如果有二级分类，查询二级分类
                if (rule.getSecondCategoryId() != null) {
                    BillCategory secondCategory = categoryMapper.selectById(rule.getSecondCategoryId());
                    if (secondCategory != null) {
                        path.append("/").append(secondCategory.getCategoryName());
                    }
                }
            } else {
                path.append("未知分类");
            }
            
            rule.setCategoryPath(path.toString());
        } catch (Exception e) {
            // 如果设置路径失败，使用默认值
            rule.setCategoryPath("未知分类");
        }
    }
    
    /**
     * 获取分类类型名称
     */
    private String getCategoryTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 0: return "支出";
            case 1: return "收入";
            case 2: return "转账";
            default: return "未知";
        }
    }
    
    @Override
    public PageResult pageQuery(RuleQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<CategoryRule> page = ruleMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
