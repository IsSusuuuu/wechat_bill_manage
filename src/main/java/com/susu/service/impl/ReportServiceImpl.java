package com.susu.service.impl;

import com.susu.mapper.WechatBillMapper;
import com.susu.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报告统计服务实现
 */
@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private WechatBillMapper wechatBillMapper;
    
    @Override
    public Map<String, Object> getTodaySummary() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return getDaySummary(today);
    }
    
    @Override
    public Map<String, Object> getCurrentMonthSummary() {
        LocalDate now = LocalDate.now();
        return getMonthSummary(now.getYear(), now.getMonthValue());
    }
    
    @Override
    public Map<String, Object> getMonthSummary(int year, int month) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建月份的开始和结束日期
            String startDate = String.format("%04d-%02d-01", year, month);
            String endDate = String.format("%04d-%02d-%02d", year, month, 
                LocalDate.of(year, month, 1).lengthOfMonth());
            
            // 查询收入总额
            Double incomeAmount = wechatBillMapper.getAmountByTypeAndDateRange("收入", startDate, endDate);
            
            // 查询支出总额
            Double expenseAmount = wechatBillMapper.getAmountByTypeAndDateRange("支出", startDate, endDate);
            
            // 计算净额
            double income = incomeAmount != null ? incomeAmount : 0.0;
            double expense = expenseAmount != null ? expenseAmount : 0.0;
            double netAmount = income - expense;
            
            result.put("incomeAmount", income);
            result.put("expenseAmount", expense);
            result.put("netAmount", netAmount);
            result.put("year", year);
            result.put("month", month);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            
        } catch (Exception e) {
            // 如果查询失败，返回默认值
            result.put("incomeAmount", 0.0);
            result.put("expenseAmount", 0.0);
            result.put("netAmount", 0.0);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getDaySummary(String date) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查询收入总额
            Double incomeAmount = wechatBillMapper.getAmountByTypeAndDateRange("收入", date, date);
            
            // 查询支出总额
            Double expenseAmount = wechatBillMapper.getAmountByTypeAndDateRange("支出", date, date);
            
            // 计算净额
            double income = incomeAmount != null ? incomeAmount : 0.0;
            double expense = expenseAmount != null ? expenseAmount : 0.0;
            double netAmount = income - expense;
            
            result.put("incomeAmount", income);
            result.put("expenseAmount", expense);
            result.put("netAmount", netAmount);
            result.put("date", date);
            
        } catch (Exception e) {
            // 如果查询失败，返回默认值
            result.put("incomeAmount", 0.0);
            result.put("expenseAmount", 0.0);
            result.put("netAmount", 0.0);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getMonthlyStatistics(int year) {
        return wechatBillMapper.getMonthlyStatistics(year);
    }
    
    @Override
    public List<Map<String, Object>> getQuarterlyStatistics(int year) {
        return wechatBillMapper.getQuarterlyStatistics(year);
    }
    
    @Override
    public List<Map<String, Object>> getCategoryStatistics(String startDate, String endDate) {
        return wechatBillMapper.getCategoryStatistics(startDate, endDate);
    }
}
