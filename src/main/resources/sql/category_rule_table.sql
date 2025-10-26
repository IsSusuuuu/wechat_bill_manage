-- 分类规则表创建脚本

-- 创建分类规则表
CREATE TABLE IF NOT EXISTS category_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    description TEXT COMMENT '规则描述',
    
    -- 匹配配置
    keyword VARCHAR(200) COMMENT '匹配关键字',
    match_type INT NOT NULL DEFAULT 1 COMMENT '匹配类型：1=包含,2=完全等于,3=正则,4=前缀,5=后缀',
    regex_pattern VARCHAR(500) COMMENT '正则表达式',
    exclude_keyword VARCHAR(200) COMMENT '排除关键字',
    match_fields VARCHAR(200) DEFAULT 'counterparty,product,remark' COMMENT '匹配字段',
    
    -- 分类配置
    first_category_id BIGINT NOT NULL COMMENT '一级分类ID',
    second_category_id BIGINT COMMENT '二级分类ID',
    category_path VARCHAR(200) COMMENT '分类路径',
    
    -- 优先级和状态
    priority INT DEFAULT 100 COMMENT '优先级（1最高）',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
    is_system TINYINT DEFAULT 0 COMMENT '是否系统规则（1=系统，0=用户）',
    
    -- 统计信息
    match_count INT DEFAULT 0 COMMENT '匹配次数',
    success_count INT DEFAULT 0 COMMENT '成功分类次数',
    success_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '成功率',
    
    -- 时间信息
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_used_time DATETIME COMMENT '最后使用时间',
    
    INDEX idx_priority (priority),
    INDEX idx_enabled (is_enabled),
    INDEX idx_category (first_category_id, second_category_id),
    INDEX idx_keyword (keyword),
    INDEX idx_match_type (match_type),
    INDEX idx_rule_name (rule_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类规则表';

-- 插入一些默认规则
INSERT INTO category_rule (rule_name, description, keyword, match_type, first_category_id, second_category_id, category_path, priority, is_system) VALUES

-- 餐饮相关规则
('美团外卖', '美团外卖订单自动分类', '美团', 1, 1, 1, '支出/餐饮美食/外卖', 10, 1),
('饿了么', '饿了么外卖订单自动分类', '饿了么', 1, 1, 1, '支出/餐饮美食/外卖', 10, 1),
('星巴克', '星巴克消费自动分类', '星巴克', 1, 1, 2, '支出/餐饮美食/咖啡', 10, 1),
('麦当劳', '麦当劳消费自动分类', '麦当劳', 1, 1, 3, '支出/餐饮美食/快餐', 10, 1),
('肯德基', '肯德基消费自动分类', '肯德基', 1, 1, 3, '支出/餐饮美食/快餐', 10, 1),
('海底捞', '海底捞消费自动分类', '海底捞', 1, 1, 4, '支出/餐饮美食/火锅', 10, 1),

-- 交通相关规则
('滴滴出行', '滴滴打车自动分类', '滴滴', 1, 2, 5, '支出/交通出行/打车', 10, 1),
('高德地图', '高德打车自动分类', '高德', 1, 2, 5, '支出/交通出行/打车', 10, 1),
('地铁', '地铁出行自动分类', '地铁', 1, 2, 6, '支出/交通出行/公共交通', 10, 1),
('公交', '公交出行自动分类', '公交', 1, 2, 6, '支出/交通出行/公共交通', 10, 1),
('出租车', '出租车自动分类', '出租车', 1, 2, 5, '支出/交通出行/打车', 10, 1),

-- 购物相关规则
('淘宝', '淘宝购物自动分类', '淘宝', 1, 3, 7, '支出/购物消费/综合电商', 10, 1),
('天猫', '天猫购物自动分类', '天猫', 1, 3, 7, '支出/购物消费/综合电商', 10, 1),
('京东', '京东购物自动分类', '京东', 1, 3, 7, '支出/购物消费/综合电商', 10, 1),
('拼多多', '拼多多购物自动分类', '拼多多', 1, 3, 7, '支出/购物消费/综合电商', 10, 1),
('苏宁', '苏宁购物自动分类', '苏宁', 1, 3, 7, '支出/购物消费/综合电商', 10, 1),

-- 娱乐相关规则
('腾讯视频', '腾讯视频会员自动分类', '腾讯视频', 1, 4, 8, '支出/娱乐休闲/视频会员', 10, 1),
('爱奇艺', '爱奇艺会员自动分类', '爱奇艺', 1, 4, 8, '支出/娱乐休闲/视频会员', 10, 1),
('优酷', '优酷会员自动分类', '优酷', 1, 4, 8, '支出/娱乐休闲/视频会员', 10, 1),
('网易云音乐', '网易云音乐会员自动分类', '网易云音乐', 1, 4, 9, '支出/娱乐休闲/音乐会员', 10, 1),
('QQ音乐', 'QQ音乐会员自动分类', 'QQ音乐', 1, 4, 9, '支出/娱乐休闲/音乐会员', 10, 1),

-- 收入相关规则
('工资', '工资收入自动分类', '工资', 1, 5, 10, '收入/工作收入/工资', 10, 1),
('奖金', '奖金收入自动分类', '奖金', 1, 5, 10, '收入/工作收入/奖金', 10, 1),
('红包', '微信红包自动分类', '红包', 1, 6, 11, '收入/其他收入/红包', 10, 1),
('转账', '转账收入自动分类', '转账', 1, 6, 12, '收入/其他收入/转账', 10, 1),

-- 生活服务相关规则
('水电费', '水电费自动分类', '水电费', 1, 7, 13, '支出/生活服务/水电费', 10, 1),
('燃气费', '燃气费自动分类', '燃气费', 1, 7, 13, '支出/生活服务/燃气费', 10, 1),
('物业费', '物业费自动分类', '物业费', 1, 7, 14, '支出/生活服务/物业费', 10, 1),
('网费', '网费自动分类', '网费', 1, 7, 15, '支出/生活服务/网费', 10, 1),

-- 医疗相关规则
('医院', '医院消费自动分类', '医院', 1, 8, 16, '支出/医疗健康/医院', 10, 1),
('药店', '药店消费自动分类', '药店', 1, 8, 17, '支出/医疗健康/药店', 10, 1),
('体检', '体检费用自动分类', '体检', 1, 8, 18, '支出/医疗健康/体检', 10, 1);

-- 创建视图：规则统计信息
CREATE VIEW v_category_rule_stats AS
SELECT 
    r.id,
    r.rule_name,
    r.keyword,
    r.match_type,
    r.priority,
    r.match_count,
    r.success_count,
    r.success_rate,
    r.last_used_time,
    r.is_enabled,
    CONCAT(c1.category_name, '-', c2.category_name) as category_name
FROM category_rule r
LEFT JOIN bill_category c1 ON r.first_category_id = c1.id
LEFT JOIN bill_category c2 ON r.second_category_id = c2.id
ORDER BY r.priority ASC, r.match_count DESC;
