package com.wdbyte.jmx;

/**
 * @author https://www.wdbyte.com
 */
public interface MyMemoryMBean {

    long getTotal();

    void setTotal(long total);

    long getUsed();

    void setUsed(long used);

    String doMemoryInfo();
}
