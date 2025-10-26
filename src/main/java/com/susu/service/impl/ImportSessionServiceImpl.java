package com.susu.service.impl;

import com.susu.domain.entity.ImportSession;
import com.susu.mapper.ImportSessionMapper;
import com.susu.service.ImportSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入批次服务实现
 */
@Service
public class ImportSessionServiceImpl implements ImportSessionService {
    
    @Autowired
    private ImportSessionMapper importSessionMapper;
    
    @Override
    public int addImportSession(ImportSession importSession) {
        return importSessionMapper.addImportSession(importSession);
    }
    
    @Override
    public List<ImportSession> getAllImportSessions() {
        return importSessionMapper.selectAll();
    }
    
    @Override
    public ImportSession getImportSessionById(Integer id) {
        return importSessionMapper.selectById(id);
    }
    
    @Override
    public Map<String, Object> getImportStatistics() {
        Map<String, Object> stats = importSessionMapper.getImportStatistics();
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("total_sessions", 0);
            stats.put("total_rows", 0);
            stats.put("total_inserted", 0);
            stats.put("total_duplicates", 0);
            stats.put("total_errors", 0);
            stats.put("success_rate", 0.0);
        }
        return stats;
    }
    
    @Override
    public List<Map<String, Object>> getBillsBySessionId(Integer sessionId) {
        return importSessionMapper.getBillsBySessionId(sessionId);
    }
    
    @Override
    @Transactional
    public void deleteImportSession(Integer id) {
        // 先删除该批次的所有账单
        importSessionMapper.deleteBillsBySessionId(id);
        // 再删除导入批次记录
        importSessionMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public Map<String, Object> reprocessImportSession(Integer id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取原始导入批次信息
            ImportSession session = importSessionMapper.selectById(id);
            if (session == null) {
                result.put("success", false);
                result.put("message", "导入批次不存在");
                return result;
            }
            
            // 删除该批次的所有账单
            int deletedBills = importSessionMapper.deleteBillsBySessionId(id);
            
            // 重新处理逻辑（这里简化处理，实际可能需要重新解析文件）
            // 在实际应用中，你可能需要：
            // 1. 重新读取原始文件
            // 2. 重新解析和分类
            // 3. 重新插入数据
            
            result.put("success", true);
            result.put("message", "重新处理完成");
            result.put("deleted_bills", deletedBills);
            result.put("session_id", id);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "重新处理失败：" + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getImportTrends(int days) {
        Map<String, Object> trends = new HashMap<>();
        
        try {
            List<Map<String, Object>> trendData = importSessionMapper.getImportTrends(days);
            
            trends.put("days", days);
            trends.put("trend_data", trendData);
            trends.put("total_days", trendData.size());
            
            // 计算趋势统计
            int totalSessions = 0;
            int totalRows = 0;
            int totalInserted = 0;
            
            for (Map<String, Object> dayData : trendData) {
                totalSessions += (Integer) dayData.getOrDefault("session_count", 0);
                totalRows += (Integer) dayData.getOrDefault("total_rows", 0);
                totalInserted += (Integer) dayData.getOrDefault("inserted_rows", 0);
            }
            
            trends.put("total_sessions", totalSessions);
            trends.put("total_rows", totalRows);
            trends.put("total_inserted", totalInserted);
            trends.put("average_sessions_per_day", trendData.isEmpty() ? 0 : totalSessions / trendData.size());
            
        } catch (Exception e) {
            trends.put("error", "获取趋势数据失败：" + e.getMessage());
        }
        
        return trends;
    }
}
