package com.susu.mapper;

import com.github.pagehelper.Page;
import com.susu.Result.PageResult;
import com.susu.domain.dto.BillQueryDTO;
import com.susu.domain.entity.WechatBill;
import com.susu.domain.vo.BillPreviewVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Mapper
public interface WechatBillMapper {
    /**
     * 统计账单哈希值出现次数
     * @param billHash 账单哈希值
     * @return 出现次数
     */
    @Select("SELECT COUNT(*) FROM wechat_bill WHERE bill_hash = #{billHash}")
    int countByBillHash(String billHash);
    /**
     * 插入微信账单
     * @param wechatBill 微信账单实体
     */

    void insertWechatBill(WechatBill wechatBill);

    Page<BillPreviewVO> pageQuery(BillQueryDTO billQueryDTO);
    
    /**
     * 根据交易类型和日期范围查询金额总和
     * @param productType 交易类型（收入/支出）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 金额总和
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM wechat_bill " +
            "WHERE product_type = #{productType} " +
            "AND DATE(transaction_time) BETWEEN #{startDate} AND #{endDate} " +
            "AND status = '支付成功'")
    Double getAmountByTypeAndDateRange(@Param("productType") String productType, 
                                     @Param("startDate") String startDate, 
                                     @Param("endDate") String endDate);
    
    /**
     * 按月份统计收支数据
     * @param year 年份
     * @return 按月份统计的数据列表
     */
    @Select("SELECT " +
            "DATE_FORMAT(transaction_time, '%Y-%m') as month, " +
            "COALESCE(SUM(CASE WHEN product_type = '收入' THEN amount ELSE 0 END), 0) as income, " +
            "COALESCE(SUM(CASE WHEN product_type = '支出' THEN amount ELSE 0 END), 0) as expense " +
            "FROM wechat_bill " +
            "WHERE YEAR(transaction_time) = #{year} AND status = '支付成功' " +
            "GROUP BY DATE_FORMAT(transaction_time, '%Y-%m') " +
            "ORDER BY month ASC")
    List<Map<String, Object>> getMonthlyStatistics(@Param("year") int year);
    
    /**
     * 按季度统计收支数据
     * @param year 年份
     * @return 按季度统计的数据列表
     */
    List<Map<String, Object>> getQuarterlyStatistics(@Param("year") int year);
    
    /**
     * 按分类统计支出数据（用于饼图）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 按分类统计的数据列表
     */
    @Select("SELECT " +
            "COALESCE(c.category_name, '未分类') as categoryName, " +
            "COALESCE(SUM(b.amount), 0) as amount " +
            "FROM wechat_bill b " +
            "LEFT JOIN bill_category c ON b.second_category_id = c.id OR (b.second_category_id IS NULL AND b.first_category_id = c.id) " +
            "WHERE b.product_type = '支出' " +
            "AND DATE(b.transaction_time) BETWEEN #{startDate} AND #{endDate} " +
            "AND b.status = '支付成功' " +
            "GROUP BY c.id, c.category_name " +
            "ORDER BY amount DESC")
    List<Map<String, Object>> getCategoryStatistics(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate);
}