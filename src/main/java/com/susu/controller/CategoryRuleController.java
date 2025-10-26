package com.susu.controller;

import com.susu.Result.PageResult;
import com.susu.Result.Result;
import com.susu.domain.dto.RuleQueryDTO;
import com.susu.domain.entity.CategoryRule;
import com.susu.service.CategoryRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类规则管理控制器
 */
@RestController
@RequestMapping("/api/rules")
@Tag(name = "分类规则管理", description = "提供分类规则管理相关接口")
public class CategoryRuleController {
    
    @Autowired
    private CategoryRuleService ruleService;
    
    /**
     * 获取所有规则
     */
    @GetMapping
    @Operation(summary = "获取所有规则", description = "获取系统中所有的分类规则")
    public Result<List<CategoryRule>> getAllRules() {
        try {
            List<CategoryRule> rules = ruleService.getAllRules();
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error("获取规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取启用的规则
     */
    @GetMapping("/enabled")
    @Operation(summary = "获取启用的规则", description = "获取所有启用的分类规则")
    public Result<List<CategoryRule>> getEnabledRules() {
        try {
            List<CategoryRule> rules = ruleService.getEnabledRules();
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error("获取启用规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取规则
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取规则详情", description = "根据ID获取指定规则的详细信息")
    public Result<CategoryRule> getRuleById(@PathVariable Long id) {
        try {
            CategoryRule rule = ruleService.getRuleById(id);
            if (rule == null) {
                return Result.error("规则不存在");
            }
            return Result.success(rule);
        } catch (Exception e) {
            return Result.error("获取规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建规则
     */
    @PostMapping
    @Operation(summary = "创建规则", description = "创建新的分类规则")
    public Result<CategoryRule> createRule(@RequestBody CategoryRule rule) {
        try {
            // 添加详细日志
            System.out.println("收到创建规则请求: " + rule);
            
            CategoryRule created = ruleService.createRule(rule);
            System.out.println("规则创建成功: " + created);
            return Result.success(created);
        } catch (RuntimeException e) {
            System.err.println("创建规则业务异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            System.err.println("创建规则系统异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("创建规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新规则
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新规则", description = "更新指定的分类规则")
    public Result<CategoryRule> updateRule(@PathVariable Long id, @RequestBody CategoryRule rule) {
        try {
            rule.setId(id);
            CategoryRule updated = ruleService.updateRule(rule);
            return Result.success(updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("更新规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除规则
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除规则", description = "删除指定的分类规则（系统规则不可删除）")
    public Result<Void> deleteRule(@PathVariable Long id) {
        try {
            ruleService.deleteRule(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试规则匹配
     */
    @PostMapping("/{id}/test")
    @Operation(summary = "测试规则", description = "使用测试文本测试指定规则的效果")
    public Result<Map<String, Object>> testRule(@PathVariable Long id, @RequestBody Map<String, String> testData) {
        try {
            String testText = testData.get("text");
            if (testText == null || testText.trim().isEmpty()) {
                return Result.error("测试文本不能为空");
            }
            
            Map<String, Object> result = ruleService.testRule(id, testText);
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("测试规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取规则统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取规则统计", description = "获取分类规则的使用统计信息")
    public Result<List<Map<String, Object>>> getRuleStatistics() {
        try {
            List<Map<String, Object>> statistics = ruleService.getRuleStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取规则统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 刷新规则缓存
     */
    @PostMapping("/refresh-cache")
    @Operation(summary = "刷新规则缓存", description = "刷新分类规则缓存")
    public Result<Void> refreshRuleCache() {
        try {
            ruleService.refreshRuleCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error("刷新缓存失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量导入规则
     */
    @PostMapping("/batch-import")
    @Operation(summary = "批量导入规则", description = "批量导入分类规则")
    public Result<Map<String, Object>> batchImportRules(@RequestBody List<CategoryRule> rules) {
        try {
            if (rules == null || rules.isEmpty()) {
                return Result.error("导入规则不能为空");
            }
            
            Map<String, Object> result = ruleService.batchImportRules(rules);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("批量导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出规则
     */
    @GetMapping("/export")
    @Operation(summary = "导出规则", description = "导出所有分类规则")
    public Result<String> exportRules(@RequestParam(defaultValue = "json") String format) {
        try {
            if (!"json".equalsIgnoreCase(format) && !"csv".equalsIgnoreCase(format)) {
                return Result.error("不支持的导出格式，支持：json, csv");
            }
            
            String exportData = ruleService.exportRules(format);
            return Result.success(exportData);
        } catch (Exception e) {
            return Result.error("导出规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用规则
     */
    @PutMapping("/{id}/toggle")
    @Operation(summary = "切换规则状态", description = "启用或禁用指定规则")
    public Result<Void> toggleRule(@PathVariable Long id) {
        try {
            CategoryRule rule = ruleService.getRuleById(id);
            if (rule == null) {
                return Result.error("规则不存在");
            }
            
            rule.setIsEnabled(rule.getIsEnabled() == 1 ? 0 : 1);
            ruleService.updateRule(rule);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("切换规则状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量删除规则
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除规则", description = "批量删除指定的分类规则")
    public Result<Map<String, Object>> batchDeleteRules(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的规则");
            }
            
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();
            
            for (Long id : ids) {
                try {
                    ruleService.deleteRule(id);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("规则ID " + id + " 删除失败: " + e.getMessage());
                }
            }
            
            result.put("total", ids.size());
            result.put("success", successCount);
            result.put("fail", failCount);
            result.put("errors", errors);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量启用/禁用规则
     */
    @PutMapping("/batch/toggle")
    @Operation(summary = "批量切换规则状态", description = "批量启用或禁用指定规则")
    public Result<Map<String, Object>> batchToggleRules(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer enabled = (Integer) request.get("enabled");
            
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要操作的规则");
            }
            
            if (enabled == null || (enabled != 0 && enabled != 1)) {
                return Result.error("状态参数无效");
            }
            
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();
            
            for (Long id : ids) {
                try {
                    CategoryRule rule = ruleService.getRuleById(id);
                    if (rule == null) {
                        failCount++;
                        errors.add("规则ID " + id + " 不存在");
                        continue;
                    }
                    
                    rule.setIsEnabled(enabled);
                    ruleService.updateRule(rule);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("规则ID " + id + " 操作失败: " + e.getMessage());
                }
            }
            
            result.put("total", ids.size());
            result.put("success", successCount);
            result.put("fail", failCount);
            result.put("errors", errors);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("批量操作失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据关键词搜索规则
     */
    @GetMapping("/search")
    @Operation(summary = "搜索规则", description = "根据关键词搜索分类规则")
    public Result<List<CategoryRule>> searchRules(@RequestParam String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Result.error("搜索关键词不能为空");
            }
            
            List<CategoryRule> rules = ruleService.searchRules(keyword.trim());
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error("搜索规则失败：" + e.getMessage());
        }
    }
    
    /**
     * 分页查询规则
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询规则", description = "分页查询分类规则")
    public Result<PageResult> pageQuery(RuleQueryDTO queryDTO) {
        try {
            PageResult pageResult = ruleService.pageQuery(queryDTO);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("分页查询规则失败：" + e.getMessage());
        }
    }
}
