-- 测试规则创建功能

-- 1. 检查category_rule表结构
DESCRIBE category_rule;

-- 2. 检查bill_category表数据
SELECT id, category_name, type, level FROM bill_category ORDER BY id LIMIT 10;

-- 3. 测试插入规则（模拟前端数据）
INSERT INTO category_rule (
    rule_name, description, keyword, match_type, regex_pattern,
    exclude_keyword, match_fields, first_category_id, second_category_id, category_path,
    priority, is_enabled, is_system, match_count, success_count, success_rate,
    create_time, update_time
) VALUES (
    '测试规则-手动插入', '手动测试规则', '测试', 1, NULL,
    NULL, 'counterparty,product,remark', 1, NULL, '支出/餐饮美食',
    100, 1, 0, 0, 0, 0.0,
    NOW(), NOW()
);

-- 4. 检查插入结果
SELECT * FROM category_rule WHERE rule_name = '测试规则-手动插入';

-- 5. 清理测试数据
DELETE FROM category_rule WHERE rule_name = '测试规则-手动插入';

-- 6. 检查是否有其他错误数据
SELECT COUNT(*) as total_rules FROM category_rule;
SELECT COUNT(*) as total_categories FROM bill_category;
