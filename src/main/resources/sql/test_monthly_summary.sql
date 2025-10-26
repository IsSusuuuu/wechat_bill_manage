-- 测试本月汇总功能

-- 1. 检查wechat_bill表数据
SELECT 
    product_type,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    DATE(transaction_time) as date
FROM wechat_bill 
WHERE status = '支付成功'
GROUP BY product_type, DATE(transaction_time)
ORDER BY date DESC, product_type;

-- 2. 测试本月收入统计
SELECT 
    '本月收入' as type,
    COALESCE(SUM(amount), 0) as total_amount
FROM wechat_bill 
WHERE product_type = '收入' 
    AND DATE(transaction_time) BETWEEN DATE_FORMAT(CURDATE(), '%Y-%m-01') AND CURDATE()
    AND status = '支付成功';

-- 3. 测试本月支出统计
SELECT 
    '本月支出' as type,
    COALESCE(SUM(amount), 0) as total_amount
FROM wechat_bill 
WHERE product_type = '支出' 
    AND DATE(transaction_time) BETWEEN DATE_FORMAT(CURDATE(), '%Y-%m-01') AND CURDATE()
    AND status = '支付成功';

-- 4. 测试今日收入统计
SELECT 
    '今日收入' as type,
    COALESCE(SUM(amount), 0) as total_amount
FROM wechat_bill 
WHERE product_type = '收入' 
    AND DATE(transaction_time) = CURDATE()
    AND status = '支付成功';

-- 5. 测试今日支出统计
SELECT 
    '今日支出' as type,
    COALESCE(SUM(amount), 0) as total_amount
FROM wechat_bill 
WHERE product_type = '支出' 
    AND DATE(transaction_time) = CURDATE()
    AND status = '支付成功';

-- 6. 查看本月数据概览
SELECT 
    DATE(transaction_time) as date,
    product_type,
    COUNT(*) as count,
    SUM(amount) as total_amount
FROM wechat_bill 
WHERE DATE(transaction_time) BETWEEN DATE_FORMAT(CURDATE(), '%Y-%m-01') AND CURDATE()
    AND status = '支付成功'
GROUP BY DATE(transaction_time), product_type
ORDER BY date DESC, product_type;

-- 7. 检查数据完整性
SELECT 
    '总记录数' as metric,
    COUNT(*) as value
FROM wechat_bill
UNION ALL
SELECT 
    '成功支付记录数' as metric,
    COUNT(*) as value
FROM wechat_bill
WHERE status = '支付成功'
UNION ALL
SELECT 
    '收入记录数' as metric,
    COUNT(*) as value
FROM wechat_bill
WHERE product_type = '收入' AND status = '支付成功'
UNION ALL
SELECT 
    '支出记录数' as metric,
    COUNT(*) as value
FROM wechat_bill
WHERE product_type = '支出' AND status = '支付成功';
