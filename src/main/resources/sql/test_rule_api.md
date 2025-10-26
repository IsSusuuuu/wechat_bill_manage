# 匹配规则API测试指南

## 基础URL
```
http://localhost:8080/api/rules
```

## 1. 获取所有规则
```bash
GET /api/rules
```

## 2. 获取启用的规则
```bash
GET /api/rules/enabled
```

## 3. 根据ID获取规则
```bash
GET /api/rules/{id}
```

## 4. 创建新规则
```bash
POST /api/rules
Content-Type: application/json

{
    "ruleName": "测试规则-API创建",
    "description": "通过API创建的测试规则",
    "keyword": "API测试",
    "matchType": 1,
    "firstCategoryId": 1,
    "secondCategoryId": null,
    "priority": 100,
    "isEnabled": 1
}
```

## 5. 更新规则
```bash
PUT /api/rules/{id}
Content-Type: application/json

{
    "ruleName": "测试规则-API更新",
    "description": "通过API更新的测试规则",
    "keyword": "API更新",
    "matchType": 1,
    "firstCategoryId": 1,
    "secondCategoryId": null,
    "priority": 90,
    "isEnabled": 1
}
```

## 6. 删除规则
```bash
DELETE /api/rules/{id}
```

## 7. 切换规则状态（启用/禁用）
```bash
PUT /api/rules/{id}/toggle
```

## 8. 测试规则匹配
```bash
POST /api/rules/{id}/test
Content-Type: application/json

{
    "text": "这是一个API测试文本"
}
```

## 9. 获取规则统计信息
```bash
GET /api/rules/statistics
```

## 10. 刷新规则缓存
```bash
POST /api/rules/refresh-cache
```

## 11. 批量导入规则
```bash
POST /api/rules/batch-import
Content-Type: application/json

[
    {
        "ruleName": "批量规则1",
        "keyword": "批量1",
        "matchType": 1,
        "firstCategoryId": 1,
        "priority": 100,
        "isEnabled": 1
    },
    {
        "ruleName": "批量规则2",
        "keyword": "批量2",
        "matchType": 1,
        "firstCategoryId": 2,
        "priority": 110,
        "isEnabled": 1
    }
]
```

## 12. 导出规则
```bash
GET /api/rules/export?format=json
GET /api/rules/export?format=csv
```

## 13. 批量删除规则
```bash
DELETE /api/rules/batch
Content-Type: application/json

[1, 2, 3]
```

## 14. 批量切换规则状态
```bash
PUT /api/rules/batch/toggle
Content-Type: application/json

{
    "ids": [1, 2, 3],
    "enabled": 1
}
```

## 15. 搜索规则
```bash
GET /api/rules/search?keyword=测试
```

## 测试用例

### 1. 完整CRUD测试流程
1. 创建规则 → 验证创建成功
2. 获取规则详情 → 验证数据正确
3. 更新规则 → 验证更新成功
4. 测试规则匹配 → 验证匹配逻辑
5. 切换规则状态 → 验证状态变更
6. 删除规则 → 验证删除成功

### 2. 批量操作测试
1. 批量创建多个规则
2. 批量启用/禁用规则
3. 批量删除规则
4. 验证操作结果统计

### 3. 搜索和过滤测试
1. 按关键词搜索规则
2. 按分类筛选规则
3. 按状态筛选规则

### 4. 导入导出测试
1. 导出规则为JSON格式
2. 导出规则为CSV格式
3. 批量导入规则
4. 验证导入结果

## 预期响应格式

### 成功响应
```json
{
    "code": 1,
    "msg": "操作成功",
    "data": { ... }
}
```

### 错误响应
```json
{
    "code": 0,
    "msg": "错误信息",
    "data": null
}
```

## 注意事项

1. 所有POST和PUT请求都需要设置正确的Content-Type
2. 规则名称不能重复
3. 系统规则（isSystem=1）不能删除
4. 正则表达式规则必须提供regexPattern字段
5. 优先级数字越小优先级越高
6. 批量操作会返回成功和失败的统计信息
