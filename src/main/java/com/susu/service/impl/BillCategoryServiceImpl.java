package com.susu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.susu.Result.PageResult;
import com.susu.domain.dto.CategoryQueryDTO;
import com.susu.domain.entity.BillCategory;
import com.susu.domain.entity.CategoryRule;
import com.susu.domain.entity.WechatBill;
import com.susu.mapper.BillCategoryMapper;
import com.susu.service.BillCategoryService;
import com.susu.utils.CategoryRuleHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Service
public class BillCategoryServiceImpl implements BillCategoryService {
    @Autowired
    private CategoryRuleHolder ruleHolder; // 缓存的规则
    @Autowired
    private BillCategoryMapper categoryMapper; // 分类字典DAO
    @Override
    public void autoClassify(WechatBill bill) {
        // 1. 提取账单中的关键信息（交易对方、商品说明、备注）
        String counterparty = bill.getCounterparty() != null ? bill.getCounterparty() : "";
        String product = bill.getProduct() != null ? bill.getProduct() : "";
        String remark = bill.getRemark() != null ? bill.getRemark() : "";

        // 合并所有可能包含关键字的字段，统一匹配
        String content = counterparty + "|" + product + "|" + remark;

       // 2. 遍历规则，按优先级匹配（高优先级先匹配）
        List<CategoryRule> rules = ruleHolder.getRuleList();
        for (CategoryRule rule : rules) {
            // 检查是否命中关键字
            boolean isMatch = matchKeyword(content, rule);
            if (isMatch) {
                // 3. 命中规则，查询分类名称（一级+二级）

                bill.setFirstCategoryId(rule.getFirstCategoryId());
                bill.setSecondCategoryId(rule.getSecondCategoryId());

                return; // 匹配到即终止（优先级高的规则优先）
            }
        }

        // 4. 未匹配到任何规则，默认归类为“其他支出-未分类”
        bill.setFirstCategoryId(23L); // 其他支出
        bill.setSecondCategoryId(23L); // 未分类

    }
    // 关键字匹配逻辑
    private boolean matchKeyword(String content, CategoryRule rule) {
        String keyword = rule.getKeyword();
        Integer matchType = rule.getMatchType(); // 1=包含，2=完全等于

        // 检查是否包含排除关键字（若有则不匹配）
        if (rule.getExcludeKeyword() != null && !rule.getExcludeKeyword().isEmpty()) {
            if (content.contains(rule.getExcludeKeyword())) {
                return false;
            }
        }

        // 匹配类型：包含关键字
        if (matchType == 1) {
            return content.contains(keyword);
        }
        // 匹配类型：完全等于（适用于精确匹配场景）
        else if (matchType == 2) {
            return content.equals(keyword);
        }
        return false;
    }
    // 根据分类ID查询一级+二级分类名称（如“购物消费-综合电商”）
    @Override
    public String getCategoryName(Long firstId, Long secondId) {
        BillCategory first = categoryMapper.selectById(firstId);
        BillCategory second = categoryMapper.selectById(secondId);
        if (first != null && second != null) {
            return first.getCategoryName() + "-" + second.getCategoryName();
        }
        return "未知分类";
    }

    @Override
    public BillCategory createCategory(BillCategory category) {
        // 设置创建时间
        category.setCreateTime(java.time.LocalDateTime.now());
        
        // 验证分类名称是否重复
        if (isCategoryNameExists(category.getCategoryName(), category.getParentId(), null)) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 插入分类
        int result = categoryMapper.insert(category);
        if (result > 0) {
            return category;
        } else {
            throw new RuntimeException("创建分类失败");
        }
    }

    @Override
    public BillCategory updateCategory(BillCategory category) {
        // 验证分类是否存在
        BillCategory existingCategory = categoryMapper.selectById(category.getId());
        if (existingCategory == null) {
            throw new RuntimeException("分类不存在");
        }
        
        // 验证分类名称是否重复（排除自己）
        if (isCategoryNameExists(category.getCategoryName(), category.getParentId(), category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 更新分类
        int result = categoryMapper.update(category);
        if (result > 0) {
            return categoryMapper.selectById(category.getId());
        } else {
            throw new RuntimeException("更新分类失败");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        // 验证分类是否存在
        BillCategory category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        // 检查是否有子分类
        List<BillCategory> childCategories = categoryMapper.selectAll().stream()
            .filter(c -> c.getParentId() != null && c.getParentId().equals(id))
            .collect(java.util.stream.Collectors.toList());
        
        if (!childCategories.isEmpty()) {
            throw new RuntimeException("该分类下还有子分类，无法删除");
        }
        
        // 检查是否有账单使用该分类
        // TODO: 这里需要添加检查账单是否使用该分类的逻辑
        
        // 删除分类
        int result = categoryMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除分类失败");
        }
    }

    @Override
    public List<BillCategory> getParentCategories() {
        return categoryMapper.selectParentCategories();
    }
    
    /**
     * 检查分类名称是否已存在
     */
    private boolean isCategoryNameExists(String categoryName, Long parentId, Long excludeId) {
        List<BillCategory> allCategories = categoryMapper.selectAll();
        return allCategories.stream()
            .anyMatch(c -> c.getCategoryName().equals(categoryName) 
                && (c.getParentId() == null ? parentId == null : c.getParentId().equals(parentId))
                && (excludeId == null || !c.getId().equals(excludeId)));
    }

    @Override
    public List<BillCategory> getAllCategories() {
         List<BillCategory> billCategories = categoryMapper.selectAll();
         return billCategories;
    }
    
    @Override
    public PageResult pageQuery(CategoryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<BillCategory> page = categoryMapper.pageQuery(queryDTO);
        
        // 转换为 VO，添加父分类名称
        List<BillCategory> allCategories = categoryMapper.selectAll();
        java.util.Map<Long, String> parentNameMap = new java.util.HashMap<>();
        for(BillCategory category : allCategories) {
            parentNameMap.put(category.getId(), category.getCategoryName());
        }
        
        List<com.susu.domain.vo.BillCategoryVO> voList = new java.util.ArrayList<>();
        for(BillCategory category : page.getResult()) {
            com.susu.domain.vo.BillCategoryVO vo = new com.susu.domain.vo.BillCategoryVO();
            vo.setId(category.getId());
            vo.setName(category.getCategoryName());
            vo.setParentId(category.getParentId());
            if(category.getParentId() != null) {
                vo.setParentName(parentNameMap.get(category.getParentId()));
            }
            vo.setLevel(category.getLevel());
            vo.setSort(category.getSort());
            vo.setIsDefault(category.getIsDefault());
            if(category.getCreateTime() != null) {
                vo.setCreateTime(category.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if(category.getType() == 0){
                vo.setCategoryType("支出");
            }else if(category.getType() == 1){
                vo.setCategoryType("收入");
            }else if(category.getType() == 2){
                vo.setCategoryType("转账");
            }
            voList.add(vo);
        }
        
        return new PageResult(page.getTotal(), voList);
    }
}