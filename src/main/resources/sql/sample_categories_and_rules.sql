-- 基于实际账单数据生成的分类和匹配规则

-- 1. 首先创建分类数据
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) VALUES

-- 一级分类：支出
('餐饮美食', 0, 1, 0, 0, 10, NOW()),
('购物消费', 0, 1, 0, 0, 20, NOW()),
('交通出行', 0, 1, 0, 0, 30, NOW()),
('生活服务', 0, 1, 0, 0, 40, NOW()),
('娱乐休闲', 0, 1, 0, 0, 50, NOW()),
('教育学习', 0, 1, 0, 0, 60, NOW()),
('医疗健康', 0, 1, 0, 0, 70, NOW()),
('其他支出', 0, 1, 0, 1, 999, NOW()),

-- 一级分类：收入
('工作收入', 0, 1, 1, 0, 10, NOW()),
('其他收入', 0, 1, 1, 0, 20, NOW()),

-- 一级分类：转账
('转账', 0, 1, 2, 0, 10, NOW());

-- 2. 创建二级分类
-- 餐饮美食的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '学校食堂', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '餐饮美食' AND level = 1;

INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '餐厅', id, 2, 0, 0, 20, NOW() FROM bill_category WHERE category_name = '餐饮美食' AND level = 1;

INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '饮品', id, 2, 0, 0, 30, NOW() FROM bill_category WHERE category_name = '餐饮美食' AND level = 1;

-- 购物消费的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '综合电商', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '购物消费' AND level = 1;

INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '超市', id, 2, 0, 0, 20, NOW() FROM bill_category WHERE category_name = '购物消费' AND level = 1;

-- 交通出行的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '校园交通', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '交通出行' AND level = 1;

-- 生活服务的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '通讯服务', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '生活服务' AND level = 1;

-- 娱乐休闲的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '游戏', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '娱乐休闲' AND level = 1;

-- 教育学习的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '学校费用', id, 2, 0, 0, 10, NOW() FROM bill_category WHERE category_name = '教育学习' AND level = 1;

-- 其他收入的二级分类
INSERT INTO bill_category (category_name, parent_id, level, type, is_default, sort, create_time) 
SELECT '转账收入', id, 2, 1, 0, 10, NOW() FROM bill_category WHERE category_name = '其他收入' AND level = 1;

-- 3. 创建匹配规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, match_count, success_count, success_rate, create_time, update_time) VALUES

-- 河南工业大学相关规则（学校食堂）
('河南工业大学食堂', '河南工业大学食堂消费自动分类', '河南工业大学', 1, 
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '学校食堂' AND level = 2),
 '支出/餐饮美食/学校食堂', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 京东购物规则
('京东购物', '京东购物自动分类', '京东', 1,
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '综合电商' AND level = 2),
 '支出/购物消费/综合电商', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 家联华超市规则
('家联华超市', '家联华超市购物自动分类', '家联华超市', 1,
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '超市' AND level = 2),
 '支出/购物消费/超市', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 青程出行规则（校园交通）
('青程出行', '青程出行校园交通自动分类', '青程出行', 1,
 (SELECT id FROM bill_category WHERE category_name = '交通出行' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '校园交通' AND level = 2),
 '支出/交通出行/校园交通', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 中国移动话费充值规则
('中国移动话费', '中国移动话费充值自动分类', '中国移动', 1,
 (SELECT id FROM bill_category WHERE category_name = '生活服务' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '通讯服务' AND level = 2),
 '支出/生活服务/通讯服务', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 杭州游卡游戏规则
('杭州游卡游戏', '杭州游卡游戏消费自动分类', '杭州游卡', 1,
 (SELECT id FROM bill_category WHERE category_name = '娱乐休闲' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '游戏' AND level = 2),
 '支出/娱乐休闲/游戏', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 餐厅规则（麦克风茶、饺子馆等）
('餐厅消费', '餐厅消费自动分类', '麦克风茶', 1,
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '餐厅' AND level = 2),
 '支出/餐饮美食/餐厅', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

('饺子馆消费', '饺子馆消费自动分类', '饺子馆', 1,
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '餐厅' AND level = 2),
 '支出/餐饮美食/餐厅', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 赵一鸣零食店规则
('赵一鸣零食', '赵一鸣零食店自动分类', '赵一鸣', 1,
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '超市' AND level = 2),
 '支出/购物消费/超市', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 转账收入规则
('转账收入', '转账收入自动分类', '转账', 1,
 (SELECT id FROM bill_category WHERE category_name = '其他收入' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '转账收入' AND level = 2),
 '收入/其他收入/转账收入', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 零钱提现规则
('零钱提现', '零钱提现自动分类', '零钱提现', 1,
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 扫二维码付款规则
('二维码付款', '扫二维码付款自动分类', '扫二维码付款', 1,
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 10, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 商户消费通用规则（兜底规则）
('商户消费', '商户消费通用分类', '商户消费', 1,
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 100, 1, 1, 0, 0, 0.0, NOW(), NOW());

-- 4. 创建一些更精确的匹配规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, exclude_keyword, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, match_count, success_count, success_rate, create_time, update_time) VALUES

-- 话费充值规则（更精确）
('话费充值', '话费充值自动分类', '话费充值', 1, NULL,
 (SELECT id FROM bill_category WHERE category_name = '生活服务' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '通讯服务' AND level = 2),
 '支出/生活服务/通讯服务', 5, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 游戏消费规则（更精确）
('游戏消费', '游戏消费自动分类', '三国杀', 1, NULL,
 (SELECT id FROM bill_category WHERE category_name = '娱乐休闲' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '游戏' AND level = 2),
 '支出/娱乐休闲/游戏', 5, 1, 1, 0, 0, 0.0, NOW(), NOW()),

-- 学校相关规则（更精确）
('学校消费', '学校相关消费自动分类', '河南工业大学', 1, NULL,
 (SELECT id FROM bill_category WHERE category_name = '教育学习' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '学校费用' AND level = 2),
 '支出/教育学习/学校费用', 5, 1, 1, 0, 0, 0.0, NOW(), NOW());

-- 5. 更新分类路径
UPDATE bill_category SET category_path = CONCAT(
    CASE type 
        WHEN 0 THEN '支出'
        WHEN 1 THEN '收入' 
        WHEN 2 THEN '转账'
        ELSE '未知'
    END, '/', category_name
) WHERE level = 1;

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
