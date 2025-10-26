-- 测试重复账单问题的修复效果

-- 1. 查看当前账单数据
SELECT 
    b.id,
    b.counterparty,
    b.amount,
    b.transaction_time,
    b.first_category_id,
    b.second_category_id,
    c1.category_name as first_category_name,
    c2.category_name as second_category_name
FROM wechat_bill b
LEFT JOIN bill_category c1 ON b.first_category_id = c1.id
LEFT JOIN bill_category c2 ON b.second_category_id = c2.id
WHERE b.counterparty LIKE '%河南工业大学%'
ORDER BY b.transaction_time DESC
LIMIT 10;

-- 2. 测试修复后的查询（模拟前端查询）
SELECT
    DATE_FORMAT(b.transaction_time, '%Y-%m-%d %H:%i:%s') AS occurredAt,
    b.product_type AS recordType,
    CASE 
        WHEN c2.category_name IS NOT NULL THEN c2.category_name
        WHEN c1.category_name IS NOT NULL THEN c1.category_name
        ELSE ''
    END AS categoryName,
    FORMAT(b.amount, 2) AS amount,
    b.payment_method AS paymentMethod,
    IFNULL(b.counterparty, '') AS merchant,
    IFNULL(b.remark, '') AS note,
    b.status AS status
FROM wechat_bill b
LEFT JOIN bill_category c1 ON b.first_category_id = c1.id
LEFT JOIN bill_category c2 ON b.second_category_id = c2.id
WHERE b.counterparty LIKE '%河南工业大学%'
ORDER BY b.transaction_time DESC
LIMIT 5;

-- 3. 检查是否有重复的账单记录
SELECT 
    counterparty,
    amount,
    transaction_time,
    COUNT(*) as duplicate_count
FROM wechat_bill 
WHERE counterparty LIKE '%河南工业大学%'
GROUP BY counterparty, amount, transaction_time
HAVING COUNT(*) > 1;

-- 4. 检查分类分配情况
SELECT 
    b.counterparty,
    COUNT(*) as bill_count,
    c1.category_name as first_category,
    c2.category_name as second_category
FROM wechat_bill b
LEFT JOIN bill_category c1 ON b.first_category_id = c1.id
LEFT JOIN bill_category c2 ON b.second_category_id = c2.id
WHERE b.counterparty LIKE '%河南工业大学%'
GROUP BY b.counterparty, c1.category_name, c2.category_name
ORDER BY bill_count DESC;
