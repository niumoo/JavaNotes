package com.wdbyte.weixin.controller;

import com.wdbyte.weixin.model.WeixinQrCode;
import com.wdbyte.weixin.util.ApiResultUtil;
import com.wdbyte.weixin.util.JwtUtil;
import com.wdbyte.weixin.util.WeixinApiUtil;
import com.wdbyte.weixin.util.WeixinQrCodeCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://www.wdbyte.com
 * @date 2024/03/16
 */
@CrossOrigin(origins = {"https://www.wdbyte.com", "https://bing.wdbyte.com", "http://localhost:8000"})
@Slf4j
@RestController
public class WeixinUserController {

    @Autowired
    private WeixinApiUtil weixinApiUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/api/v1/user/qrcode")
    public String getQrCode() {
        WeixinQrCode qrCode = weixinApiUtil.getQrCode();
        qrCode.setUrl(null);
        qrCode.setExpireSeconds(null);
        return ApiResultUtil.success(qrCode);
    }

    /**
     * 校验是否扫描完成
     * 完成，返回 JWT
     * 未完成，返回 check faild
     *
     * @param ticket
     * @return
     */
    @GetMapping(value = "/api/v1/user/login/qrcode")
    public String userLogin(String ticket) {
        String openId = WeixinQrCodeCacheUtil.get(ticket);
        if (StringUtils.isNotEmpty(openId)) {
            log.info("login success,open id:{}", openId);
            return ApiResultUtil.success(jwtUtil.createToken(openId));
        }
        log.info("login error,ticket:{}", ticket);
        return ApiResultUtil.error("check faild");
    }

}
