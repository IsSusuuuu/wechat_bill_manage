package com.susu.mapper;

import com.github.pagehelper.Page;
import com.susu.domain.dto.CategoryQueryDTO;
import com.susu.domain.entity.BillCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Mapper
public interface BillCategoryMapper {
    /**
     * 根据分类ID获取分类名称
     * @param categoryId 分类ID
     * @return 分类名称
     */
    @Select("SELECT * FROM bill_category WHERE id = #{categoryId}")
    BillCategory selectById(Long categoryId);
    
    /**
     * 获取所有分类
     * @return 所有分类列表
     */
    @Select("SELECT * FROM bill_category")
    List<BillCategory> selectAll();
    
    /**
     * 插入新的分类
     * @param category 分类信息
     * @return 影响的行数
     */
    @Insert("INSERT INTO bill_category(category_name, parent_id, level, type, is_default, sort, create_time) " +
            "VALUES(#{categoryName}, #{parentId}, #{level}, #{type}, #{isDefault}, #{sort}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BillCategory category);
    
    /**
     * 更新分类信息
     * @param category 分类信息
     * @return 影响的行数
     */
    @Update("UPDATE bill_category SET category_name=#{categoryName}, parent_id=#{parentId}, level=#{level}, " +
            "type=#{type}, is_default=#{isDefault}, sort=#{sort} WHERE id=#{id}")
    int update(BillCategory category);
    
    /**
     * 删除分类
     * @param id 分类ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM bill_category WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 获取一级分类列表
     * @return 一级分类列表
     */
    @Select("SELECT * FROM bill_category WHERE level = 1")
    List<BillCategory> selectParentCategories();
    
    /**
     * 分页查询分类
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<BillCategory> pageQuery(CategoryQueryDTO queryDTO);
}