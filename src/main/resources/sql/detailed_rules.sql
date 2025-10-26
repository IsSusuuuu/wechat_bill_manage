-- 基于实际账单数据的详细匹配规则配置（修复版本）

-- 1. 餐饮相关规则（基于交易对方匹配）
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 河南工业大学食堂（高频消费）
('河南工业大学食堂消费', '河南工业大学食堂消费自动分类', '河南工业大学', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '学校食堂' AND level = 2),
 '支出/餐饮美食/学校食堂', 5, 1, 1, NOW(), NOW()),
-- 餐厅消费
('麦克风茶饮品', '麦克风茶饮品店消费', '麦克风茶', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '饮品' AND level = 2),
 '支出/餐饮美食/饮品', 5, 1, 1, NOW(), NOW()),
('饺子馆消费', '饺子馆餐厅消费', '饺子馆', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '餐饮美食' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '餐厅' AND level = 2),
 '支出/餐饮美食/餐厅', 5, 1, 1, NOW(), NOW());

-- 2. 购物消费规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 京东购物
('京东购物', '京东商城购物消费', '京东', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '综合电商' AND level = 2),
 '支出/购物消费/综合电商', 5, 1, 1, NOW(), NOW()),
-- 超市购物
('家联华超市', '家联华超市购物', '家联华超市', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '超市' AND level = 2),
 '支出/购物消费/超市', 5, 1, 1, NOW(), NOW()),
('赵一鸣零食', '赵一鸣零食店购物', '赵一鸣', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '超市' AND level = 2),
 '支出/购物消费/超市', 5, 1, 1, NOW(), NOW());

-- 3. 交通出行规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 校园交通
('青程出行', '青程出行校园交通', '青程出行', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '交通出行' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '校园交通' AND level = 2),
 '支出/交通出行/校园交通', 5, 1, 1, NOW(), NOW());

-- 4. 生活服务规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 通讯服务
('中国移动话费', '中国移动话费充值', '中国移动', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '生活服务' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '通讯服务' AND level = 2),
 '支出/生活服务/通讯服务', 5, 1, 1, NOW(), NOW());

-- 5. 娱乐休闲规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 游戏消费
('杭州游卡游戏', '杭州游卡游戏消费', '杭州游卡', 1, 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '娱乐休闲' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '游戏' AND level = 2),
 '支出/娱乐休闲/游戏', 5, 1, 1, NOW(), NOW());

-- 6. 收入规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 转账收入
('转账收入', '转账收入自动分类', '转账', 1, 'transactionType',
 (SELECT id FROM bill_category WHERE category_name = '其他收入' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '转账收入' AND level = 2),
 '收入/其他收入/转账收入', 5, 1, 1, NOW(), NOW());

-- 7. 特殊交易类型规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 零钱提现
('零钱提现', '零钱提现自动分类', '零钱提现', 1, 'transactionType',
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 5, 1, 1, NOW(), NOW()),
-- 扫二维码付款
('二维码付款', '扫二维码付款自动分类', '扫二维码付款', 1, 'transactionType',
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 5, 1, 1, NOW(), NOW());

-- 8. 基于商品说明的规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 话费充值（基于商品说明）
('话费充值商品', '基于商品说明的话费充值分类', '话费充值', 1, 'product',
 (SELECT id FROM bill_category WHERE category_name = '生活服务' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '通讯服务' AND level = 2),
 '支出/生活服务/通讯服务', 3, 1, 1, NOW(), NOW()),
-- 游戏消费（基于商品说明）
('游戏商品', '基于商品说明的游戏消费分类', '三国杀', 1, 'product',
 (SELECT id FROM bill_category WHERE category_name = '娱乐休闲' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '游戏' AND level = 2),
 '支出/娱乐休闲/游戏', 3, 1, 1, NOW(), NOW());

-- 9. 兜底规则（优先级最低）
INSERT INTO category_rule (rule_name, description, keyword, match_type, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 商户消费兜底规则
('商户消费兜底', '商户消费兜底分类规则', '商户消费', 1, 'transactionType',
 (SELECT id FROM bill_category WHERE category_name = '其他支出' AND level = 1),
 NULL,
 '支出/其他支出', 999, 1, 1, NOW(), NOW());

-- 10. 创建一些正则表达式规则（用于更复杂的匹配）
INSERT INTO category_rule (rule_name, description, keyword, match_type, regex_pattern, match_fields, first_category_id, second_category_id, category_path, priority, is_enabled, is_system, create_time, update_time) VALUES
-- 京东订单号匹配
('京东订单', '京东订单号正则匹配', NULL, 3, '京东.*订单编号\\d+', 'product',
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '综合电商' AND level = 2),
 '支出/购物消费/综合电商', 2, 1, 1, NOW(), NOW()),
-- 条码支付匹配
('条码支付', '条码支付正则匹配', NULL, 3, '条码支付.*', 'product',
 (SELECT id FROM bill_category WHERE category_name = '购物消费' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '超市' AND level = 2),
 '支出/购物消费/超市', 2, 1, 1, NOW(), NOW()),
-- 学校相关匹配
('学校相关', '学校相关消费正则匹配', NULL, 3, '河南工业大学.*', 'counterparty',
 (SELECT id FROM bill_category WHERE category_name = '教育学习' AND level = 1),
 (SELECT id FROM bill_category WHERE category_name = '学校费用' AND level = 2),
 '支出/教育学习/学校费用', 2, 1, 1, NOW(), NOW());
