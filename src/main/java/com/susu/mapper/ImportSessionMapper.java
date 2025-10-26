package com.susu.mapper;

import com.susu.domain.entity.ImportSession;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 导入批次Mapper
 */
@Mapper
public interface ImportSessionMapper {
    
    @Select("select count(*) from import_session")
    Integer getImportSessionCount();

    /**
     * 添加导入会话
     * @param importSession 导入会话
     * @return 影响行数
     */
    @Insert("insert into import_session(source_type, file_name, total_rows, inserted_rows, duplicate_rows, error_rows, import_time,cur_id) " +
            "values(#{sourceType}, #{fileName}, #{totalRows}, #{insertedRows}, #{duplicateRows}, #{errorRows}, #{importTime},#{curId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addImportSession(ImportSession importSession);
    
    /**
     * 获取所有导入批次
     */
    @Select("SELECT * FROM import_session ORDER BY import_time DESC")
    List<ImportSession> selectAll();
    
    /**
     * 根据ID获取导入批次
     */
    @Select("SELECT * FROM import_session WHERE id = #{id}")
    ImportSession selectById(Integer id);
    
    /**
     * 删除导入批次
     */
    @Delete("DELETE FROM import_session WHERE id = #{id}")
    int deleteById(Integer id);
    
    /**
     * 获取导入统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as total_sessions, " +
            "SUM(total_rows) as total_rows, " +
            "SUM(inserted_rows) as total_inserted, " +
            "SUM(duplicate_rows) as total_duplicates, " +
            "SUM(error_rows) as total_errors, " +
            "AVG(inserted_rows * 100.0 / total_rows) as success_rate " +
            "FROM import_session")
    Map<String, Object> getImportStatistics();
    
    /**
     * 获取指定批次的账单数据
     */
    @Select("SELECT " +
            "wb.id, wb.transaction_time, wb.transaction_type, wb.counterparty, " +
            "wb.product, wb.amount, wb.payment_method, wb.status, wb.remark, " +
            "wb.row_index, wb.first_category_id, wb.second_category_id, " +
            "c1.category_name as first_category_name, " +
            "c2.category_name as second_category_name " +
            "FROM wechat_bill wb " +
            "LEFT JOIN bill_category c1 ON wb.first_category_id = c1.id " +
            "LEFT JOIN bill_category c2 ON wb.second_category_id = c2.id " +
            "WHERE wb.session_id = #{sessionId} " +
            "ORDER BY wb.row_index")
    List<Map<String, Object>> getBillsBySessionId(Integer sessionId);
    
    /**
     * 获取导入趋势数据
     */
    @Select("SELECT " +
            "DATE(import_time) as import_date, " +
            "COUNT(*) as session_count, " +
            "SUM(total_rows) as total_rows, " +
            "SUM(inserted_rows) as inserted_rows, " +
            "SUM(duplicate_rows) as duplicate_rows, " +
            "SUM(error_rows) as error_rows " +
            "FROM import_session " +
            "WHERE import_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "GROUP BY DATE(import_time) " +
            "ORDER BY import_date DESC")
    List<Map<String, Object>> getImportTrends(int days);
    
    /**
     * 删除指定批次的所有账单
     */
    @Delete("DELETE FROM wechat_bill WHERE session_id = #{sessionId}")
    int deleteBillsBySessionId(Integer sessionId);
}
