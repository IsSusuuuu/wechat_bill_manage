package com.susu.controller;

import com.susu.Result.PageResult;
import com.susu.Result.Result;
import com.susu.domain.dto.BillQueryDTO;
import com.susu.domain.vo.BillPreviewVO;
import com.susu.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述：微信账单的相关接口
 *
 * @author 苏苏
 * @since 版本号
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    BillService billService;
    @GetMapping
    public Result<PageResult> bills(BillQueryDTO billQueryDTO) {
        PageResult pageResult = billService.pageQuery(billQueryDTO);
        return Result.success(pageResult);

    }
}
