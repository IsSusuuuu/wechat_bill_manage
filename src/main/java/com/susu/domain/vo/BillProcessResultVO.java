package com.susu.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 前端所需的财务账单处理结果结构（主数据类）
 */
@Data
public class BillProcessResultVO implements Serializable {
    // 与前端要求的字段完全对应
    private Integer total;         // 总条数（如 100）
    private Integer inserted;      // 插入成功条数（如 95）
    private Integer duplicates;    // 重复条数（如 5）
    private Integer errors;        // 错误条数（如 0）
    private List<BillPreviewVO> preview; // 预览数据列表（前端需要的 preview 数组）
}