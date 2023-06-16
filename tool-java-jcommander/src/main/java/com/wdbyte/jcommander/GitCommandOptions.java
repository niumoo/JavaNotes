package com.wdbyte.jcommander;

import java.net.URL;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.URLConverter;

/**
 * @author niulang
 * @date 2023/06/07
 */
public class GitCommandOptions {

    @Parameter(names = {"help", "-help", "-h"},
        description = "查看帮助信息",
        help = true)
    private boolean help;

    @Parameter(names = {"clone"},
        description = "克隆远程仓库数据",
        arity = 1)
    private String cloneUrl;

    @Parameter(names = {"version", "-version", "-v"},
        description = "显示当前版本号")
    private boolean version = false;

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }
}
