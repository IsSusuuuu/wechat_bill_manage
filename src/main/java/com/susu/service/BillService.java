package com.susu.service;

import com.susu.Result.PageResult;
import com.susu.domain.dto.BillQueryDTO;
import com.susu.domain.vo.BillPreviewVO;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
public interface BillService {
    /**
     * 描述：获取账单预览
     *
     * @return 账单预览VO
     */
    BillPreviewVO getBillPreview();

    PageResult pageQuery(BillQueryDTO billQueryDTO);
}
