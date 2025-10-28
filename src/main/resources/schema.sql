-- 微信财务监控系统 - 数据库表结构
-- 使用 H2 数据库

-- 创建微信账单表
CREATE TABLE IF NOT EXISTS wechat_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_time TIMESTAMP NOT NULL,
    transaction_type VARCHAR(50),
    counterparty VARCHAR(100),
    product VARCHAR(200),
    product_type VARCHAR(50),
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(50),
    transaction_id VARCHAR(100),
    merchant_order_id VARCHAR(100),
    remark TEXT,
    bill_hash VARCHAR(64) UNIQUE,
    session_id INT,
    row_index INT,
    first_category_id BIGINT,
    second_category_id BIGINT,
    import_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建分类表
CREATE TABLE IF NOT EXISTS bill_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    level INT,
    type INT,
    is_default INT,
    sort INT DEFAULT 0,
    create_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建分类规则表
CREATE TABLE IF NOT EXISTS category_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100),
    description VARCHAR(255),
    keyword VARCHAR(200),
    match_type INT,
    regex_pattern VARCHAR(500),
    exclude_keyword VARCHAR(200),
    match_fields VARCHAR(200),
    first_category_id BIGINT,
    second_category_id BIGINT,
    category_path VARCHAR(255),
    priority INT DEFAULT 0,
    is_enabled INT DEFAULT 1,
    is_system INT DEFAULT 0,
    match_count INT DEFAULT 0,
    success_count INT DEFAULT 0,
    success_rate DOUBLE,
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    last_used_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建导入会话表
CREATE TABLE IF NOT EXISTS import_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cur_id INT,
    source_type VARCHAR(50),
    file_name VARCHAR(255),
    total_rows INT DEFAULT 0,
    inserted_rows INT DEFAULT 0,
    duplicate_rows INT DEFAULT 0,
    error_rows INT DEFAULT 0,
    import_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_bill_time ON wechat_bill(transaction_time);
CREATE INDEX IF NOT EXISTS idx_bill_hash ON wechat_bill(bill_hash);
CREATE INDEX IF NOT EXISTS idx_bill_first_category ON wechat_bill(first_category_id);
CREATE INDEX IF NOT EXISTS idx_bill_second_category ON wechat_bill(second_category_id);
CREATE INDEX IF NOT EXISTS idx_category_parent ON bill_category(parent_id);
CREATE INDEX IF NOT EXISTS idx_category_type ON bill_category(type);

