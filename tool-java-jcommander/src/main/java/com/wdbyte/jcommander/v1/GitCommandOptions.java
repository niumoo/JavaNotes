package com.wdbyte.jcommander.v1;

import com.beust.jcommander.Parameter;

/**
 * @author https://www.wdbyte.com
 * @date 2023/06/07
 */
public class GitCommandOptions {
    @Parameter(names = {"clone"},
        description = "克隆远程仓库数据")
    private String cloneUrl;

    public String getCloneUrl() {
        return cloneUrl;
    }
}
