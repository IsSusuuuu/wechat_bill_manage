package com.susu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.susu.Result.PageResult;
import com.susu.domain.dto.BillQueryDTO;
import com.susu.domain.entity.BillCategory;
import com.susu.domain.entity.WechatBill;
import com.susu.domain.vo.BillCategoryVO;
import com.susu.domain.vo.BillPreviewVO;
import com.susu.mapper.BillCategoryMapper;
import com.susu.mapper.WechatBillMapper;
import com.susu.service.BillService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private WechatBillMapper wechatBillMapper;
    @Override
    public BillPreviewVO getBillPreview() {
        BillPreviewVO vo = new BillPreviewVO();
        return null;
    }

    @Override
    public PageResult pageQuery(BillQueryDTO billQueryDTO) {
        PageHelper.startPage(billQueryDTO.getPage(), billQueryDTO.getPageSize());
        Page<BillPreviewVO> page = wechatBillMapper.pageQuery(billQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}