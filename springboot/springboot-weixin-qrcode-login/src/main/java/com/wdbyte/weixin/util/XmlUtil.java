package com.wdbyte.weixin.util;

import com.alibaba.fastjson2.JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author https://www.wdbyte.com
 * @date 2024/01/18
 */
@Slf4j
public class XmlUtil {

    public static String xml2json(String requestBody) {
        requestBody = StringUtils.trim(requestBody);
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = null;
        try {
            node = xmlMapper.readTree(requestBody.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);
        } catch (Exception e) {
            log.error("xml 2 json error,msg:" + e.getMessage(), e);
        }
        return null;
    }
}
