package com.susu.controller;

import com.susu.Result.PageResult;
import com.susu.Result.Result;
import com.susu.domain.dto.CategoryQueryDTO;
import com.susu.domain.entity.BillCategory;
import com.susu.domain.vo.BillCategoryVO;
import com.susu.service.BillCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private BillCategoryService billCategoryService;
    
    /**
     * 描述：获取所有分类
     *
     * @return 所有分类列表
     */
    @GetMapping
    public Result<List<BillCategoryVO>> getCategory() {
        List<BillCategory> allCategories = billCategoryService.getAllCategories();
        List<BillCategoryVO> categoryList = new ArrayList<>();
        
        // 创建一个 Map，方便根据 ID 快速查找分类
        java.util.Map<Long, BillCategory> categoryMap = new java.util.HashMap<>();
        for(BillCategory category : allCategories) {
            categoryMap.put(category.getId(), category);
        }
        
        for(BillCategory category : allCategories) {
            BillCategoryVO vo = new BillCategoryVO();
            vo.setId(category.getId());
            vo.setName(category.getCategoryName());
            vo.setParentId(category.getParentId());
            vo.setLevel(category.getLevel());
            vo.setSort(category.getSort());
            vo.setIsDefault(category.getIsDefault());
            
            // 查询并设置父分类名称
            if(category.getParentId() != null) {
                BillCategory parentCategory = categoryMap.get(category.getParentId());
                if(parentCategory != null) {
                    vo.setParentName(parentCategory.getCategoryName());
                }
            }
            
            if(category.getCreateTime() != null) {
                vo.setCreateTime(category.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if(category.getType() == 0){
                vo.setCategoryType("支出");
            }else if(category.getType() == 1){
                vo.setCategoryType("收入");
            }else if(category.getType() == 2){
                vo.setCategoryType("转账");
            }
            categoryList.add(vo);
        }
        return Result.success(categoryList);
    }
    
    /**
     * 创建分类
     * @param category 分类信息
     * @return 创建结果
     */
    @PostMapping
    public Result<BillCategoryVO> createCategory(@RequestBody BillCategory category) {
        try {
            BillCategory created = billCategoryService.createCategory(category);
            BillCategoryVO vo = convertToVO(created);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("创建分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新分类
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<BillCategoryVO> updateCategory(@PathVariable Long id, @RequestBody BillCategory category) {
        try {
            category.setId(id);
            BillCategory updated = billCategoryService.updateCategory(category);
            BillCategoryVO vo = convertToVO(updated);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("更新分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除分类
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        try {
            billCategoryService.deleteCategory(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("删除分类失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取一级分类列表
     * @return 一级分类列表
     */
    @GetMapping("/parents")
    public Result<List<BillCategoryVO>> getParentCategories() {
        List<BillCategory> parentCategories = billCategoryService.getParentCategories();
        List<BillCategoryVO> voList = new ArrayList<>();
        for(BillCategory category : parentCategories) {
            voList.add(convertToVO(category));
        }
        return Result.success(voList);
    }
    
    /**
     * 分页查询分类
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryQueryDTO queryDTO) {
        PageResult pageResult = billCategoryService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 将BillCategory转换为BillCategoryVO
     * @param category 分类实体
     * @return 分类VO
     */
    private BillCategoryVO convertToVO(BillCategory category) {
        BillCategoryVO vo = new BillCategoryVO();
        vo.setId(category.getId());
        vo.setName(category.getCategoryName());
        vo.setParentId(category.getParentId());
        vo.setLevel(category.getLevel());
        vo.setSort(category.getSort());
        vo.setIsDefault(category.getIsDefault());
        
        // 查询并设置父分类名称
        if(category.getParentId() != null) {
            List<BillCategory> allCategories = billCategoryService.getAllCategories();
            for(BillCategory c : allCategories) {
                if(c.getId().equals(category.getParentId())) {
                    vo.setParentName(c.getCategoryName());
                    break;
                }
            }
        }
        
        if(category.getCreateTime() != null) {
            vo.setCreateTime(category.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(category.getType() == 0){
            vo.setCategoryType("支出");
        }else if(category.getType() == 1){
            vo.setCategoryType("收入");
        }else if(category.getType() == 2){
            vo.setCategoryType("转账");
        }
        return vo;
    }
}