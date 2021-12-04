package com.wdbyte.jmx;

/**
 * @author https://www.wdbyte.com
 */
public class MyMemory implements MyMemoryMBean {

    private long total;
    private long used;

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public long getUsed() {
        return used;
    }

    @Override
    public void setUsed(long used) {
        this.used = used;
    }

    @Override
    public String doMemoryInfo() {
        return String.format("使用内存: %dMB/%dMB", used, total);
    }

}
