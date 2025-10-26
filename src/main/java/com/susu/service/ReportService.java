package com.susu.service;

import java.util.List;
import java.util.Map;

/**
 * 报告统计服务接口
 */
public interface ReportService {
    
    /**
     * 获取今日汇总数据
     * @return 包含收入、支出、净额的汇总数据
     */
    Map<String, Object> getTodaySummary();
    
    /**
     * 获取当前月份汇总数据
     * @return 包含收入、支出、净额的汇总数据
     */
    Map<String, Object> getCurrentMonthSummary();
    
    /**
     * 获取指定月份汇总数据
     * @param year 年份
     * @param month 月份（1-12）
     * @return 包含收入、支出、净额的汇总数据
     */
    Map<String, Object> getMonthSummary(int year, int month);
    
    /**
     * 获取指定日期汇总数据
     * @param date 日期字符串（格式：yyyy-MM-dd）
     * @return 包含收入、支出、净额的汇总数据
     */
    Map<String, Object> getDaySummary(String date);
    
    /**
     * 按月份统计收支数据
     * @param year 年份
     * @return 按月份统计的收支数据列表
     */
    List<Map<String, Object>> getMonthlyStatistics(int year);
    
    /**
     * 按季度统计收支数据
     * @param year 年份
     * @return 按季度统计的收支数据列表
     */
    List<Map<String, Object>> getQuarterlyStatistics(int year);
    
    /**
     * 按分类统计支出数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 按分类统计的支出数据列表
     */
    List<Map<String, Object>> getCategoryStatistics(String startDate, String endDate);
}
