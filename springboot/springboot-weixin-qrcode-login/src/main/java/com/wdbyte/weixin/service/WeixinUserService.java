package com.wdbyte.weixin.service;

/**
 * @author niulang
 * @date 2024/03/24
 */
public interface WeixinUserService {

    void checkSignature(String signature, String timestamp, String nonce);

    String handleWeixinMsg(String body);

}
