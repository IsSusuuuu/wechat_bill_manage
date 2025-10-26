package com.susu.controller;

import com.susu.Result.Result;
import com.susu.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 报告统计控制器
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "报告统计", description = "提供各种统计报告接口")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * 获取今日汇总数据
     */
    @GetMapping("/daily/summary/today")
    @Operation(summary = "今日汇总", description = "获取今日的收入、支出和净额汇总")
    public Result<Map<String, Object>> getTodaySummary() {
        try {
            Map<String, Object> summary = reportService.getTodaySummary();
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取今日汇总失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取本月汇总数据
     */
    @GetMapping("/monthly/summary/current")
    @Operation(summary = "本月汇总", description = "获取当前月份的收入、支出和净额汇总")
    public Result<Map<String, Object>> getCurrentMonthSummary() {
        try {
            Map<String, Object> summary = reportService.getCurrentMonthSummary();
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取本月汇总失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取指定月份的汇总数据
     */
    @GetMapping("/monthly/summary")
    @Operation(summary = "指定月份汇总", description = "获取指定月份的收入、支出和净额汇总")
    public Result<Map<String, Object>> getMonthSummary(@RequestParam int year, @RequestParam int month) {
        try {
            Map<String, Object> summary = reportService.getMonthSummary(year, month);
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取月份汇总失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取指定日期的汇总数据
     */
    @GetMapping("/daily/summary")
    @Operation(summary = "指定日期汇总", description = "获取指定日期的收入、支出和净额汇总")
    public Result<Map<String, Object>> getDaySummary(@RequestParam String date) {
        try {
            Map<String, Object> summary = reportService.getDaySummary(date);
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取日期汇总失败：" + e.getMessage());
        }
    }
    
    /**
     * 按月份统计收支数据
     */
    @GetMapping("/statistics/monthly")
    @Operation(summary = "月度统计", description = "按月份统计指定年份的收支数据，用于折线图展示")
    public Result<List<Map<String, Object>>> getMonthlyStatistics(@RequestParam int year) {
        try {
            List<Map<String, Object>> statistics = reportService.getMonthlyStatistics(year);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取月度统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 按季度统计收支数据
     */
    @GetMapping("/statistics/quarterly")
    @Operation(summary = "季度统计", description = "按季度统计指定年份的收支数据，用于折线图展示")
    public Result<List<Map<String, Object>>> getQuarterlyStatistics(@RequestParam int year) {
        try {
            List<Map<String, Object>> statistics = reportService.getQuarterlyStatistics(year);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取季度统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 按分类统计支出数据
     */
    @GetMapping("/statistics/category")
    @Operation(summary = "分类统计", description = "按分类统计指定时间范围内的支出数据，用于扇形图展示")
    public Result<List<Map<String, Object>>> getCategoryStatistics(@RequestParam String startDate, 
                                                                   @RequestParam String endDate) {
        try {
            List<Map<String, Object>> statistics = reportService.getCategoryStatistics(startDate, endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取分类统计失败：" + e.getMessage());
        }
    }
}
