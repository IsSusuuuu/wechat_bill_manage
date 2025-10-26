-- 调试规则创建问题

-- 1. 检查category_rule表是否存在
SHOW TABLES LIKE 'category_rule';

-- 2. 检查表结构
DESCRIBE category_rule;

-- 3. 检查bill_category表数据
SELECT id, category_name, type, level, parent_id FROM bill_category ORDER BY id LIMIT 5;

-- 4. 检查是否有数据
SELECT COUNT(*) as category_count FROM bill_category;
SELECT COUNT(*) as rule_count FROM category_rule;

-- 5. 测试简单插入（不包含category_path）
INSERT INTO category_rule (
    rule_name, description, keyword, match_type,
    first_category_id, priority, is_enabled, is_system,
    match_count, success_count, success_rate,
    create_time, update_time
) VALUES (
    '测试规则-简单', '简单测试', '测试', 1,
    1, 100, 1, 0,
    0, 0, 0.0,
    NOW(), NOW()
);

-- 6. 检查插入结果
SELECT * FROM category_rule WHERE rule_name = '测试规则-简单';

-- 7. 清理测试数据
DELETE FROM category_rule WHERE rule_name = '测试规则-简单';

-- 8. 检查表约束
SHOW CREATE TABLE category_rule;
