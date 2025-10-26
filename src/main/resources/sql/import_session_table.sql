-- 导入批次表创建脚本

-- 创建导入批次表
CREATE TABLE IF NOT EXISTS import_session (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cur_id INT COMMENT '当前导入的账单id',
    source_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    total_rows INT NOT NULL DEFAULT 0 COMMENT '总行数',
    inserted_rows INT NOT NULL DEFAULT 0 COMMENT '成功插入行数',
    duplicate_rows INT NOT NULL DEFAULT 0 COMMENT '重复行数',
    error_rows INT NOT NULL DEFAULT 0 COMMENT '错误行数',
    import_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
    
    INDEX idx_import_time (import_time),
    INDEX idx_source_type (source_type),
    INDEX idx_file_name (file_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导入批次表';

-- 确保wechat_bill表有session_id字段
ALTER TABLE wechat_bill 
ADD COLUMN IF NOT EXISTS session_id INT COMMENT '导入批次ID',
ADD INDEX IF NOT EXISTS idx_session_id (session_id);

-- 插入一些示例数据（可选）
INSERT INTO import_session (cur_id, source_type, file_name, total_rows, inserted_rows, duplicate_rows, error_rows, import_time) VALUES
(1, '微信账单xlsx', '微信支付账单(2024-10-01).xlsx', 100, 95, 3, 2, '2024-10-01 10:30:00'),
(2, '微信账单xlsx', '微信支付账单(2024-10-02).xlsx', 150, 148, 1, 1, '2024-10-02 14:20:00'),
(3, '微信账单xlsx', '微信支付账单(2024-10-03).xlsx', 80, 78, 2, 0, '2024-10-03 09:15:00'),
(4, '微信账单xlsx', '微信支付账单(2024-10-04).xlsx', 200, 195, 3, 2, '2024-10-04 16:45:00'),
(5, '微信账单xlsx', '微信支付账单(2024-10-05).xlsx', 120, 118, 1, 1, '2024-10-05 11:30:00');

-- 创建视图：导入批次统计
CREATE OR REPLACE VIEW v_import_session_stats AS
SELECT 
    id,
    source_type,
    file_name,
    total_rows,
    inserted_rows,
    duplicate_rows,
    error_rows,
    ROUND(inserted_rows * 100.0 / total_rows, 2) as success_rate,
    import_time,
    DATE(import_time) as import_date
FROM import_session
ORDER BY import_time DESC;

-- 创建视图：每日导入统计
CREATE OR REPLACE VIEW v_daily_import_stats AS
SELECT 
    DATE(import_time) as import_date,
    COUNT(*) as session_count,
    SUM(total_rows) as total_rows,
    SUM(inserted_rows) as total_inserted,
    SUM(duplicate_rows) as total_duplicates,
    SUM(error_rows) as total_errors,
    ROUND(AVG(inserted_rows * 100.0 / total_rows), 2) as avg_success_rate
FROM import_session
GROUP BY DATE(import_time)
ORDER BY import_date DESC;
