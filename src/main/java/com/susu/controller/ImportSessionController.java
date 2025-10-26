package com.susu.controller;

import com.susu.Result.Result;
import com.susu.domain.entity.ImportSession;
import com.susu.service.ImportSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 导入批次管理控制器
 */
@RestController
@RequestMapping("/api/import-sessions")
@Tag(name = "导入批次管理", description = "提供导入批次相关的查询和管理功能")
public class ImportSessionController {
    
    @Autowired
    private ImportSessionService importSessionService;
    
    /**
     * 获取所有导入批次
     */
    @GetMapping
    @Operation(summary = "获取导入批次列表", description = "获取所有导入批次的详细信息")
    public Result<List<ImportSession>> getAllImportSessions() {
        try {
            List<ImportSession> sessions = importSessionService.getAllImportSessions();
            return Result.success(sessions);
        } catch (Exception e) {
            return Result.error("获取导入批次失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取导入批次详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取导入批次详情", description = "根据ID获取指定导入批次的详细信息")
    public Result<ImportSession> getImportSessionById(@PathVariable Integer id) {
        try {
            ImportSession session = importSessionService.getImportSessionById(id);
            if (session == null) {
                return Result.error("导入批次不存在");
            }
            return Result.success(session);
        } catch (Exception e) {
            return Result.error("获取导入批次详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取导入统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取导入统计", description = "获取导入批次的统计信息")
    public Result<Map<String, Object>> getImportStatistics() {
        try {
            Map<String, Object> statistics = importSessionService.getImportStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取导入统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取指定批次的账单数据
     */
    @GetMapping("/{id}/bills")
    @Operation(summary = "获取批次账单", description = "获取指定导入批次的所有账单数据")
    public Result<List<Map<String, Object>>> getBillsBySessionId(@PathVariable Integer id) {
        try {
            List<Map<String, Object>> bills = importSessionService.getBillsBySessionId(id);
            return Result.success(bills);
        } catch (Exception e) {
            return Result.error("获取批次账单失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除指定批次及其所有账单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除导入批次", description = "删除指定导入批次及其所有相关账单数据")
    public Result<Void> deleteImportSession(@PathVariable Integer id) {
        try {
            importSessionService.deleteImportSession(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除导入批次失败：" + e.getMessage());
        }
    }
    
    /**
     * 重新处理指定批次
     */
    @PostMapping("/{id}/reprocess")
    @Operation(summary = "重新处理批次", description = "重新处理指定导入批次的账单数据")
    public Result<Map<String, Object>> reprocessImportSession(@PathVariable Integer id) {
        try {
            Map<String, Object> result = importSessionService.reprocessImportSession(id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("重新处理批次失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取导入趋势分析
     */
    @GetMapping("/trends")
    @Operation(summary = "导入趋势分析", description = "获取导入数据的趋势分析")
    public Result<Map<String, Object>> getImportTrends(@RequestParam(defaultValue = "30") int days) {
        try {
            Map<String, Object> trends = importSessionService.getImportTrends(days);
            return Result.success(trends);
        } catch (Exception e) {
            return Result.error("获取导入趋势失败：" + e.getMessage());
        }
    }
}
