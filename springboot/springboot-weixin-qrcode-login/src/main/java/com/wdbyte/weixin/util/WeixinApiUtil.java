package com.wdbyte.weixin.util;

import java.net.URI;
import java.time.LocalDateTime;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import com.wdbyte.weixin.model.WeixinQrCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author https://www.wdbyte.com
 * @date 2024/01/16
 */
@Slf4j
@Component
public class WeixinApiUtil {

    @Value("${weixin.appid}")
    public String appId;

    @Value("${weixin.appsecret}")
    public String appSecret;

    private static String QR_CODE_URL_PREFIX = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    private static String ACCESS_TOKEN = null;
    private static LocalDateTime ACCESS_TOKEN_EXPIRE_TIME = null;

    /**
     * 获取唯一 access token
     *
     * @return
     */
    public synchronized String getAccessToken() {
        if (ACCESS_TOKEN != null && ACCESS_TOKEN_EXPIRE_TIME.isAfter(LocalDateTime.now())) {
            return ACCESS_TOKEN;
        }
        String api = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String result = HttpUtil.get(api);
        JSONObject jsonObject = JSON.parseObject(result);
        ACCESS_TOKEN = jsonObject.getString("access_token");
        ACCESS_TOKEN_EXPIRE_TIME = LocalDateTime.now().plusSeconds(jsonObject.getLong("expires_in") - 10);
        return ACCESS_TOKEN;
    }

    /**
     * https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html
     *
     * @return
     */
    public WeixinQrCode getQrCode() {
        String api = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + getAccessToken();
        int timeout = 10 * 60 ;
        String jsonBody = String.format("{\n"
            + "  \"expire_seconds\": %d,\n"
            + "  \"action_name\": \"QR_STR_SCENE\",\n"
            + "  \"action_info\": {\n"
            + "    \"scene\": {\n"
            + "      \"scene_str\": \"%s\"\n"
            + "    }\n"
            + "  }\n"
            + "}", timeout, KeyUtils.uuid32());
        String result = HttpUtil.post(api, jsonBody);
        log.info("get qr code params:{}", jsonBody);
        log.info("get qr code result:{}", result);
        WeixinQrCode weixinQrCode = JSON.parseObject(result, WeixinQrCode.class);
        weixinQrCode.setQrCodeUrl(QR_CODE_URL_PREFIX + URI.create(weixinQrCode.getTicket()).toASCIIString());
        return weixinQrCode;
    }
}
