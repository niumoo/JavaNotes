package com.wdbyte.weixin.model;

import lombok.Data;

/**
 * @author https://www.wdbyte.com
 * @date 2024/03/15
 */
@Data
public class WeixinQrCode {

    private String ticket;
    private Long expireSeconds;
    private String url;
    private String qrCodeUrl;
}
