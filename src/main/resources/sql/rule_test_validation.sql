-- 规则测试和验证脚本

-- 1. 查看所有分类
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

-- 2. 查看所有规则
SELECT 
    r.id,
    r.rule_name,
    r.description,
    r.keyword,
    CASE r.match_type
        WHEN 1 THEN '包含'
        WHEN 2 THEN '等于'
        WHEN 3 THEN '正则'
        WHEN 4 THEN '开头'
        WHEN 5 THEN '结尾'
        ELSE '未知'
    END as match_type_name,
    r.match_fields,
    r.regex_pattern,
    r.priority,
    r.is_enabled,
    r.is_system,
    CONCAT(c1.category_name, 
           CASE WHEN c2.category_name IS NOT NULL THEN ' > ' || c2.category_name ELSE '' END
    ) as category_path,
    r.create_time
FROM category_rule r
LEFT JOIN bill_category c1 ON r.first_category_id = c1.id
LEFT JOIN bill_category c2 ON r.second_category_id = c2.id
ORDER BY r.priority ASC, r.id ASC;

-- 3. 测试规则匹配效果（模拟自动分类）
-- 这里我们创建一个临时表来测试规则匹配
CREATE TEMPORARY TABLE temp_bill_test AS
SELECT 
    id,
    transaction_type,
    counterparty,
    product,
    amount,
    payment_method,
    status,
    transaction_id,
    merchant_order_id,
    remark,
    bill_hash,
    transaction_time
FROM wechat_bill 
WHERE session_id = 'test_session_1'
ORDER BY transaction_time DESC;

-- 4. 测试规则匹配逻辑
SELECT 
    b.id,
    b.transaction_type,
    b.counterparty,
    b.product,
    b.amount,
    -- 测试河南工业大学规则
    CASE 
        WHEN b.counterparty LIKE '%河南工业大学%' THEN '餐饮美食 > 学校食堂'
        WHEN b.counterparty LIKE '%京东%' THEN '购物消费 > 综合电商'
        WHEN b.counterparty LIKE '%家联华超市%' THEN '购物消费 > 超市'
        WHEN b.counterparty LIKE '%青程出行%' THEN '交通出行 > 校园交通'
        WHEN b.counterparty LIKE '%中国移动%' THEN '生活服务 > 通讯服务'
        WHEN b.counterparty LIKE '%杭州游卡%' THEN '娱乐休闲 > 游戏'
        WHEN b.counterparty LIKE '%麦克风茶%' THEN '餐饮美食 > 饮品'
        WHEN b.counterparty LIKE '%饺子馆%' THEN '餐饮美食 > 餐厅'
        WHEN b.counterparty LIKE '%赵一鸣%' THEN '购物消费 > 超市'
        WHEN b.transaction_type = '转账' THEN '其他收入 > 转账收入'
        WHEN b.transaction_type = '零钱提现' THEN '其他支出'
        WHEN b.transaction_type = '扫二维码付款' THEN '其他支出'
        ELSE '其他支出'
    END as predicted_category,
    b.transaction_time
FROM temp_bill_test b
ORDER BY b.transaction_time DESC;

-- 5. 统计各类别的消费金额
SELECT 
    CASE 
        WHEN b.counterparty LIKE '%河南工业大学%' THEN '餐饮美食 > 学校食堂'
        WHEN b.counterparty LIKE '%京东%' THEN '购物消费 > 综合电商'
        WHEN b.counterparty LIKE '%家联华超市%' THEN '购物消费 > 超市'
        WHEN b.counterparty LIKE '%青程出行%' THEN '交通出行 > 校园交通'
        WHEN b.counterparty LIKE '%中国移动%' THEN '生活服务 > 通讯服务'
        WHEN b.counterparty LIKE '%杭州游卡%' THEN '娱乐休闲 > 游戏'
        WHEN b.counterparty LIKE '%麦克风茶%' THEN '餐饮美食 > 饮品'
        WHEN b.counterparty LIKE '%饺子馆%' THEN '餐饮美食 > 餐厅'
        WHEN b.counterparty LIKE '%赵一鸣%' THEN '购物消费 > 超市'
        WHEN b.transaction_type = '转账' THEN '其他收入 > 转账收入'
        WHEN b.transaction_type = '零钱提现' THEN '其他支出'
        WHEN b.transaction_type = '扫二维码付款' THEN '其他支出'
        ELSE '其他支出'
    END as category,
    COUNT(*) as transaction_count,
    SUM(b.amount) as total_amount,
    AVG(b.amount) as avg_amount,
    MIN(b.amount) as min_amount,
    MAX(b.amount) as max_amount
FROM temp_bill_test b
GROUP BY 
    CASE 
        WHEN b.counterparty LIKE '%河南工业大学%' THEN '餐饮美食 > 学校食堂'
        WHEN b.counterparty LIKE '%京东%' THEN '购物消费 > 综合电商'
        WHEN b.counterparty LIKE '%家联华超市%' THEN '购物消费 > 超市'
        WHEN b.counterparty LIKE '%青程出行%' THEN '交通出行 > 校园交通'
        WHEN b.counterparty LIKE '%中国移动%' THEN '生活服务 > 通讯服务'
        WHEN b.counterparty LIKE '%杭州游卡%' THEN '娱乐休闲 > 游戏'
        WHEN b.counterparty LIKE '%麦克风茶%' THEN '餐饮美食 > 饮品'
        WHEN b.counterparty LIKE '%饺子馆%' THEN '餐饮美食 > 餐厅'
        WHEN b.counterparty LIKE '%赵一鸣%' THEN '购物消费 > 超市'
        WHEN b.transaction_type = '转账' THEN '其他收入 > 转账收入'
        WHEN b.transaction_type = '零钱提现' THEN '其他支出'
        WHEN b.transaction_type = '扫二维码付款' THEN '其他支出'
        ELSE '其他支出'
    END
ORDER BY total_amount DESC;

-- 6. 验证规则优先级
SELECT 
    r.id,
    r.rule_name,
    r.keyword,
    r.priority,
    r.is_enabled,
    CASE r.match_type
        WHEN 1 THEN '包含'
        WHEN 2 THEN '等于'
        WHEN 3 THEN '正则'
        WHEN 4 THEN '开头'
        WHEN 5 THEN '结尾'
        ELSE '未知'
    END as match_type_name,
    CONCAT(c1.category_name, 
           CASE WHEN c2.category_name IS NOT NULL THEN ' > ' || c2.category_name ELSE '' END
    ) as category_path
FROM category_rule r
LEFT JOIN bill_category c1 ON r.first_category_id = c1.id
LEFT JOIN bill_category c2 ON r.second_category_id = c2.id
WHERE r.is_enabled = 1
ORDER BY r.priority ASC, r.id ASC;

-- 7. 清理临时表
DROP TEMPORARY TABLE temp_bill_test;
