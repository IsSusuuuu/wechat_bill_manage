# 导入批次功能使用指南

## 📋 功能概述

基于现有的导入批次功能，我们扩展了一套完整的导入批次管理系统，可以帮你实现以下实用功能：

## 🚀 核心功能

### 1. 导入批次管理
- **批次列表查看**：查看所有导入批次的基本信息
- **批次详情查看**：查看特定批次的详细信息和账单数据
- **批次删除**：删除指定批次及其所有相关账单
- **批次重新处理**：重新处理指定批次的数据

### 2. 统计分析功能
- **导入统计概览**：总批次、总行数、成功率等关键指标
- **趋势分析**：按日期分析导入趋势和模式
- **成功率分析**：分析各批次的导入成功率

### 3. 数据追溯功能
- **按批次查询**：根据导入批次查询特定时间导入的数据
- **数据完整性检查**：验证导入数据的完整性
- **错误数据分析**：分析导入失败的数据

## 🎯 实际应用场景

### 1. 数据质量管理
```sql
-- 查看导入成功率低的批次
SELECT * FROM v_import_session_stats 
WHERE success_rate < 90 
ORDER BY import_time DESC;

-- 分析重复数据最多的批次
SELECT * FROM import_session 
WHERE duplicate_rows > 10 
ORDER BY duplicate_rows DESC;
```

### 2. 数据追溯和审计
```sql
-- 查看特定日期的所有导入数据
SELECT wb.*, is.file_name, is.import_time
FROM wechat_bill wb
JOIN import_session is ON wb.session_id = is.id
WHERE DATE(is.import_time) = '2024-10-01'
ORDER BY wb.row_index;

-- 查看特定批次的分类情况
SELECT 
    c1.category_name as first_category,
    c2.category_name as second_category,
    COUNT(*) as count
FROM wechat_bill wb
LEFT JOIN bill_category c1 ON wb.first_category_id = c1.id
LEFT JOIN bill_category c2 ON wb.second_category_id = c2.id
WHERE wb.session_id = 1
GROUP BY c1.category_name, c2.category_name
ORDER BY count DESC;
```

### 3. 导入性能分析
```sql
-- 分析每日导入趋势
SELECT * FROM v_daily_import_stats 
ORDER BY import_date DESC 
LIMIT 30;

-- 找出导入数据最多的日期
SELECT * FROM v_daily_import_stats 
ORDER BY total_rows DESC 
LIMIT 10;
```

### 4. 数据清理和维护
```sql
-- 删除特定日期的所有导入数据
DELETE wb FROM wechat_bill wb
JOIN import_session is ON wb.session_id = is.id
WHERE DATE(is.import_time) = '2024-10-01';

-- 删除导入失败的批次
DELETE FROM import_session 
WHERE inserted_rows = 0;
```

## 🔧 API接口

### 1. 批次管理接口
- `GET /api/import-sessions` - 获取所有导入批次
- `GET /api/import-sessions/{id}` - 获取批次详情
- `GET /api/import-sessions/{id}/bills` - 获取批次账单数据
- `DELETE /api/import-sessions/{id}` - 删除批次
- `POST /api/import-sessions/{id}/reprocess` - 重新处理批次

### 2. 统计分析接口
- `GET /api/import-sessions/statistics` - 获取导入统计
- `GET /api/import-sessions/trends?days=30` - 获取趋势分析

## 📊 前端页面

### 导入批次管理页面 (`/import-sessions.html`)
- 统计概览仪表板
- 批次列表和搜索
- 批次详情查看
- 趋势分析图表
- 批次操作（删除、重新处理）

## 🎨 扩展功能建议

### 1. 数据验证和修复
```java
// 可以添加数据验证规则
public class DataValidationService {
    public ValidationResult validateSession(Integer sessionId) {
        // 验证数据完整性
        // 检查必填字段
        // 验证数据格式
        // 检查业务规则
    }
}
```

### 2. 自动分类优化
```java
// 基于历史数据优化分类规则
public class ClassificationOptimizer {
    public void optimizeRulesForSession(Integer sessionId) {
        // 分析批次中的分类情况
        // 建议新的分类规则
        // 优化现有规则优先级
    }
}
```

### 3. 数据导出功能
```java
// 按批次导出数据
public class SessionExportService {
    public void exportSessionData(Integer sessionId, String format) {
        // 导出特定批次的数据
        // 支持多种格式（Excel、CSV、JSON）
        // 包含分类和统计信息
    }
}
```

### 4. 批量操作
```java
// 批量处理多个批次
public class BatchOperationService {
    public void batchReprocess(List<Integer> sessionIds) {
        // 批量重新处理
        // 批量删除
        // 批量导出
    }
}
```

## 📈 监控和告警

### 1. 导入质量监控
- 成功率低于阈值时告警
- 重复数据过多时提醒
- 导入失败时通知

### 2. 性能监控
- 导入速度监控
- 数据库性能影响分析
- 系统资源使用情况

## 🔍 使用示例

### 1. 查看最近的导入情况
```javascript
// 前端调用
fetch('/api/import-sessions/statistics')
  .then(response => response.json())
  .then(data => {
    console.log('总批次:', data.data.total_sessions);
    console.log('成功率:', data.data.success_rate + '%');
  });
```

### 2. 分析特定批次的分类效果
```javascript
// 获取批次详情
fetch('/api/import-sessions/1/bills')
  .then(response => response.json())
  .then(data => {
    // 分析分类分布
    const categories = {};
    data.data.forEach(bill => {
      const category = bill.first_category_name;
      categories[category] = (categories[category] || 0) + 1;
    });
    console.log('分类分布:', categories);
  });
```

## 🎯 最佳实践

1. **定期清理**：定期删除过期的导入批次数据
2. **监控质量**：设置导入质量监控和告警
3. **数据备份**：重要批次数据定期备份
4. **规则优化**：基于导入结果持续优化分类规则
5. **性能优化**：大数据量导入时考虑分批处理

通过这些功能，你可以更好地管理和分析你的财务数据导入过程，提高数据质量和管理效率！
