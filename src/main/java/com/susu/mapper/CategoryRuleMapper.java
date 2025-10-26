package com.susu.mapper;

import com.github.pagehelper.Page;
import com.susu.domain.dto.RuleQueryDTO;
import com.susu.domain.entity.CategoryRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 分类规则Mapper
 *
 * @author 苏苏
 * @since 版本号
 */
@Mapper
public interface CategoryRuleMapper {
    
    /**
     * 查询所有启用的规则
     */
    @Select("SELECT * FROM category_rule WHERE is_enabled = 1 ORDER BY priority ASC")
    List<CategoryRule> selectAllEnabledRules();
    
    /**
     * 查询所有规则
     */
    @Select("SELECT * FROM category_rule ORDER BY priority ASC")
    List<CategoryRule> selectAll();
    
    /**
     * 根据ID查询规则
     */
    @Select("SELECT * FROM category_rule WHERE id = #{id}")
    CategoryRule selectById(Long id);
    
    /**
     * 插入新规则
     */
    @Insert("INSERT INTO category_rule(rule_name, description, keyword, match_type, regex_pattern, " +
            "exclude_keyword, match_fields, first_category_id, second_category_id, category_path, " +
            "priority, is_enabled, is_system, match_count, success_count, success_rate, " +
            "create_time, update_time) " +
            "VALUES(#{ruleName}, #{description}, #{keyword}, #{matchType}, #{regexPattern}, " +
            "#{excludeKeyword}, #{matchFields}, #{firstCategoryId}, #{secondCategoryId}, #{categoryPath}, " +
            "#{priority}, #{isEnabled}, #{isSystem}, #{matchCount}, #{successCount}, #{successRate}, " +
            "#{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CategoryRule rule);
    
    /**
     * 更新规则
     */
    @Update("UPDATE category_rule SET rule_name=#{ruleName}, description=#{description}, " +
            "keyword=#{keyword}, match_type=#{matchType}, regex_pattern=#{regexPattern}, " +
            "exclude_keyword=#{excludeKeyword}, match_fields=#{matchFields}, " +
            "first_category_id=#{firstCategoryId}, second_category_id=#{secondCategoryId}, " +
            "category_path=#{categoryPath}, priority=#{priority}, is_enabled=#{isEnabled}, " +
            "match_count=#{matchCount}, success_count=#{successCount}, success_rate=#{successRate}, " +
            "update_time=#{updateTime} WHERE id=#{id}")
    int update(CategoryRule rule);
    
    /**
     * 删除规则
     */
    @Delete("DELETE FROM category_rule WHERE id = #{id} AND is_system = 0")
    int deleteById(Long id);
    
    /**
     * 更新规则统计信息
     */
    @Update("UPDATE category_rule SET match_count=#{matchCount}, success_count=#{successCount}, " +
            "success_rate=#{successRate}, last_used_time=#{lastUsedTime} WHERE id=#{id}")
    int updateStatistics(CategoryRule rule);
    
    /**
     * 根据分类ID查询规则
     */
    @Select("SELECT * FROM category_rule WHERE first_category_id = #{categoryId} OR second_category_id = #{categoryId}")
    List<CategoryRule> selectByCategoryId(Long categoryId);
    
    /**
     * 根据关键词查询规则
     */
    @Select("SELECT * FROM category_rule WHERE keyword LIKE CONCAT('%', #{keyword}, '%') OR rule_name LIKE CONCAT('%', #{keyword}, '%')")
    List<CategoryRule> selectByKeyword(String keyword);
    
    /**
     * 分页查询规则
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<CategoryRule> pageQuery(RuleQueryDTO queryDTO);
}
