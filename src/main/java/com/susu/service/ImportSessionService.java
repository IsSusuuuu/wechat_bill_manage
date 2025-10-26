package com.susu.service;

import com.susu.domain.entity.ImportSession;

import java.util.List;
import java.util.Map;

/**
 * 导入批次服务接口
 */
public interface ImportSessionService {
    
    /**
     * 添加导入会话
     * @param importSession 导入会话信息
     * @return 影响行数
     */
    int addImportSession(ImportSession importSession);
    
    /**
     * 获取所有导入批次
     * @return 导入批次列表
     */
    List<ImportSession> getAllImportSessions();
    
    /**
     * 根据ID获取导入批次
     * @param id 批次ID
     * @return 导入批次信息
     */
    ImportSession getImportSessionById(Integer id);
    
    /**
     * 获取导入统计信息
     * @return 统计信息
     */
    Map<String, Object> getImportStatistics();
    
    /**
     * 获取指定批次的账单数据
     * @param sessionId 批次ID
     * @return 账单数据列表
     */
    List<Map<String, Object>> getBillsBySessionId(Integer sessionId);
    
    /**
     * 删除导入批次及其相关数据
     * @param id 批次ID
     */
    void deleteImportSession(Integer id);
    
    /**
     * 重新处理导入批次
     * @param id 批次ID
     * @return 处理结果
     */
    Map<String, Object> reprocessImportSession(Integer id);
    
    /**
     * 获取导入趋势分析
     * @param days 分析天数
     * @return 趋势数据
     */
    Map<String, Object> getImportTrends(int days);
}
