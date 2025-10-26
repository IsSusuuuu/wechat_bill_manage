package com.susu.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Data
public class ImportSession {
    private int id;   //主键id
    private Integer curId;  //当前导入的账单id
    private String sourceType;  //文件类型
    private String fileName;  //文件名
    private Integer totalRows;  //总行数
    private Integer insertedRows;  //成功插入行数
    private Integer duplicateRows;  //重复行数
    private Integer errorRows;  //错误行数
    private LocalDateTime importTime;  //导入时间
}
