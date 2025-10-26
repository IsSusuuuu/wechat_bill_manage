-- 测试匹配规则增删改查功能

-- 1. 查看当前规则数据
SELECT 
    id, rule_name, keyword, match_type, 
    first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate
FROM category_rule 
ORDER BY priority ASC, id ASC;

-- 2. 测试新增规则
INSERT INTO category_rule (
    rule_name, description, keyword, match_type, 
    first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate,
    create_time, update_time
) VALUES (
    '测试规则-包含匹配', '测试包含匹配功能', '测试', 1,
    1, NULL, 50, 1, 0,
    0, 0, 0.0,
    NOW(), NOW()
);

-- 3. 测试正则表达式规则
INSERT INTO category_rule (
    rule_name, description, keyword, match_type, 
    regex_pattern, first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate,
    create_time, update_time
) VALUES (
    '测试规则-正则匹配', '测试正则表达式匹配', '正则', 3,
    '.*测试.*', 2, NULL, 60, 1, 0,
    0, 0, 0.0,
    NOW(), NOW()
);

-- 4. 测试前缀匹配规则
INSERT INTO category_rule (
    rule_name, description, keyword, match_type, 
    first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate,
    create_time, update_time
) VALUES (
    '测试规则-前缀匹配', '测试前缀匹配功能', '前缀', 4,
    3, NULL, 70, 1, 0,
    0, 0, 0.0,
    NOW(), NOW()
);

-- 5. 查看新增后的规则
SELECT 
    id, rule_name, keyword, match_type, regex_pattern,
    first_category_id, second_category_id, 
    priority, is_enabled, is_system
FROM category_rule 
WHERE rule_name LIKE '测试规则%'
ORDER BY priority ASC;

-- 6. 测试更新规则
UPDATE category_rule 
SET 
    description = '更新后的描述',
    priority = 45,
    is_enabled = 0
WHERE rule_name = '测试规则-包含匹配';

-- 7. 测试更新统计信息
UPDATE category_rule 
SET 
    match_count = 10,
    success_count = 8,
    success_rate = 80.0,
    last_used_time = NOW()
WHERE rule_name = '测试规则-包含匹配';

-- 8. 查看更新后的规则
SELECT 
    id, rule_name, description, keyword, match_type,
    first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate,
    last_used_time
FROM category_rule 
WHERE rule_name LIKE '测试规则%'
ORDER BY priority ASC;

-- 9. 测试搜索功能（模拟前端搜索）
SELECT * FROM category_rule 
WHERE keyword LIKE CONCAT('%', '测试', '%') 
   OR rule_name LIKE CONCAT('%', '测试', '%')
ORDER BY priority ASC;

-- 10. 测试按分类查询规则
SELECT * FROM category_rule 
WHERE first_category_id = 1 OR second_category_id = 1
ORDER BY priority ASC;

-- 11. 测试删除规则（删除测试规则）
DELETE FROM category_rule WHERE rule_name LIKE '测试规则%';

-- 12. 验证删除结果
SELECT COUNT(*) as remaining_test_rules 
FROM category_rule 
WHERE rule_name LIKE '测试规则%';

-- 13. 查看最终规则列表
SELECT 
    id, rule_name, keyword, match_type, 
    first_category_id, second_category_id, 
    priority, is_enabled, is_system,
    match_count, success_count, success_rate
FROM category_rule 
ORDER BY priority ASC, id ASC;
