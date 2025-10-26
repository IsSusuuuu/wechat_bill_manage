-- 测试修复后的查询是否返回完整数据

-- 1. 测试修复后的完整查询
SELECT
    b.row_index AS rowIndex,
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

-- 2. 检查所有字段是否都有值
SELECT 
    COUNT(*) as total_records,
    COUNT(row_index) as row_index_count,
    COUNT(transaction_time) as transaction_time_count,
    COUNT(product_type) as product_type_count,
    COUNT(first_category_id) as first_category_count,
    COUNT(second_category_id) as second_category_count,
    COUNT(amount) as amount_count,
    COUNT(payment_method) as payment_method_count,
    COUNT(counterparty) as counterparty_count,
    COUNT(remark) as remark_count,
    COUNT(status) as status_count
FROM wechat_bill
WHERE counterparty LIKE '%河南工业大学%';

-- 3. 检查分类关联是否正确
SELECT 
    b.id,
    b.counterparty,
    b.first_category_id,
    b.second_category_id,
    c1.category_name as first_category_name,
    c2.category_name as second_category_name,
    CASE 
        WHEN c2.category_name IS NOT NULL THEN c2.category_name
        WHEN c1.category_name IS NOT NULL THEN c1.category_name
        ELSE '无分类'
    END AS final_category_name
FROM wechat_bill b
LEFT JOIN bill_category c1 ON b.first_category_id = c1.id
LEFT JOIN bill_category c2 ON b.second_category_id = c2.id
WHERE b.counterparty LIKE '%河南工业大学%'
ORDER BY b.transaction_time DESC
LIMIT 3;

-- 4. 检查是否有空值或NULL值
SELECT 
    'row_index' as field_name,
    COUNT(*) as total,
    COUNT(row_index) as non_null_count,
    COUNT(*) - COUNT(row_index) as null_count
FROM wechat_bill
UNION ALL
SELECT 
    'transaction_time' as field_name,
    COUNT(*) as total,
    COUNT(transaction_time) as non_null_count,
    COUNT(*) - COUNT(transaction_time) as null_count
FROM wechat_bill
UNION ALL
SELECT 
    'amount' as field_name,
    COUNT(*) as total,
    COUNT(amount) as non_null_count,
    COUNT(*) - COUNT(amount) as null_count
FROM wechat_bill;

