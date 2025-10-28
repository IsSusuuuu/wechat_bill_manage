-- 微信财务监控系统 - 默认分类数据
-- type: 0=支出, 1=收入, 2=转账

-- 清空旧分类数据
DELETE FROM bill_category;

-- 插入一级分类（支出）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(1, NULL, '餐饮美食', 1, 10, 0, 0, '2025-10-21 12:29:13'),
(2, NULL, '购物消费', 1, 20, 0, 0, '2025-10-21 12:29:13'),
(3, NULL, '交通出行', 1, 30, 0, 0, '2025-10-21 12:29:13'),
(4, NULL, '生活服务', 1, 40, 0, 0, '2025-10-21 12:29:13'),
(5, NULL, '娱乐休闲', 1, 50, 0, 0, '2025-10-21 12:29:13'),
(6, NULL, '教育学习', 1, 60, 0, 0, '2025-10-21 12:29:13'),
(7, NULL, '医疗健康', 1, 70, 0, 0, '2025-10-21 12:29:13'),
(8, NULL, '其他支出', 1, 999, 1, 0, '2025-10-21 12:29:13');

-- 插入一级分类（收入）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(9, NULL, '工作收入', 1, 10, 0, 1, '2025-10-21 12:29:13'),
(10, NULL, '其他收入', 1, 20, 0, 1, '2025-10-21 12:29:13');

-- 插入一级分类（转账）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(11, NULL, '转账', 1, 10, 0, 2, '2025-10-21 12:29:13');

-- 插入二级分类（餐饮美食的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(12, 1, '学校食堂', 2, 10, 0, 0, '2025-10-21 12:29:13'),
(13, 1, '餐厅', 2, 20, 0, 0, '2025-10-21 12:29:13'),
(14, 1, '饮品', 2, 30, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（购物消费的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(15, 2, '综合电商', 2, 10, 0, 0, '2025-10-21 12:29:13'),
(16, 2, '超市', 2, 20, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（交通出行的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(17, 3, '校园交通', 2, 10, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（生活服务的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(18, 4, '通讯服务', 2, 10, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（娱乐休闲的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(19, 5, '游戏', 2, 10, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（教育学习的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(20, 6, '学校费用', 2, 10, 0, 0, '2025-10-21 12:29:13');

-- 插入二级分类（其他收入的子分类）
INSERT INTO bill_category (id, parent_id, category_name, level, sort, is_default, type, create_time) VALUES
(21, 10, '转账收入', 2, 10, 0, 1, '2025-10-21 12:29:13');

-- 重置自增序列
ALTER TABLE bill_category ALTER COLUMN id RESTART WITH 22;

