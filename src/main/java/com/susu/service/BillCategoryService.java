package com.susu.service;

import com.susu.Result.PageResult;
import com.susu.domain.dto.CategoryQueryDTO;
import com.susu.domain.entity.BillCategory;
import com.susu.domain.entity.WechatBill;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */

public interface BillCategoryService {
    /**
     * 自动分类
     * @param bill 微信账单
     */
    public void autoClassify(WechatBill bill);
    
    /**
     * 描述：获取所有分类
     *
     * @return 所有分类列表
     */
     List<BillCategory> getAllCategories();
     
     /**
     * 描述：根据分类ID查询一级+二级分类名称（如“购物消费-综合电商”）
     *
     * @param firstId 一级分类ID
     * @param secondId 二级分类ID
     * @return 一级+二级分类名称
     */
     String getCategoryName(Long firstId, Long secondId);
     
     /**
      * 创建分类
      * @param category 分类信息
      * @return 创建后的分类
      */
     BillCategory createCategory(BillCategory category);
     
     /**
      * 更新分类
      * @param category 分类信息
      * @return 更新后的分类
      */
     BillCategory updateCategory(BillCategory category);
     
     /**
      * 删除分类
      * @param id 分类ID
      */
     void deleteCategory(Long id);
     
     /**
      * 获取一级分类列表
      * @return 一级分类列表
      */
     List<BillCategory> getParentCategories();
     
     /**
      * 分页查询分类
      * @param queryDTO 查询条件
      * @return 分页结果
      */
     PageResult pageQuery(CategoryQueryDTO queryDTO);
}