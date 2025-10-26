package com.susu.service;

import com.susu.domain.entity.WechatBill;
import com.susu.domain.vo.BillProcessResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述：
 *
 * @author 苏苏
 * @since 版本号
 */
public interface XlsxParseService {
    /**
     * 解析微信账单xlsx文件
     * @param file 微信账单xlsx文件
     */
    BillProcessResultVO parseXlsxFile(MultipartFile file) throws Exception;
}
