-- 修复分类路径的SQL脚本

-- 1. 更新一级分类的路径
UPDATE bill_category 
SET category_path = CONCAT(
    CASE type 
        WHEN 0 THEN '支出'
        WHEN 1 THEN '收入' 
        WHEN 2 THEN '转账'
        ELSE '未知'
    END, '/', category_name
) 
WHERE level = 1;

-- 2. 更新二级分类的路径（使用正确的MySQL语法）
UPDATE bill_category bc2 
INNER JOIN bill_category bc1 ON bc2.parent_id = bc1.id
SET bc2.category_path = CONCAT(
    CASE bc1.type 
        WHEN 0 THEN '支出'
        WHEN 1 THEN '收入' 
        WHEN 2 THEN '转账'
        ELSE '未知'
    END, '/', bc1.category_name, '/', bc2.category_name
) 
WHERE bc2.level = 2;

-- 3. 验证路径更新结果
SELECT 
    id,
    category_name,
    parent_id,
    level,
    CASE type 
        WHEN 0 THEN '支出'
        WHEN 1 THEN '收入' 
        WHEN 2 THEN '转账'
        ELSE '未知'
    END as type_name,
    category_path,
    sort
FROM bill_category 
ORDER BY type, level, sort;

-- 4. 检查是否有路径为空的记录
SELECT 
    id,
    category_name,
    level,
    category_path
FROM bill_category 
WHERE category_path IS NULL OR category_path = '';

-- 5. 手动修复任何路径为空的记录（如果需要）
-- 示例：如果发现某个分类路径为空，可以手动更新
-- UPDATE bill_category SET category_path = '支出/餐饮美食' WHERE id = 1 AND category_path IS NULL;
