package com.wdbyte.jcommander.v5;

import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author https://www.wdbyte.com
 * @date 2023/06/15
 */
public class UrlParameterValidator implements IParameterValidator {
    @Override
    public void validate(String key, String value) throws ParameterException {
        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new ParameterException("参数 " + key + " 的值必须是 URL 格式");
        }
    }
}
