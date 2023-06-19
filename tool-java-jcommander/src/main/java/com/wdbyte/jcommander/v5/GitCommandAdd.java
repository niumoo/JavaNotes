package com.wdbyte.jcommander.v5;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * git add file1 file2
 *
 * @author https://www.wdbyte.com
 * @date 2023/06/07
 */
@Parameters(commandDescription = "暂存文件", commandNames = "add", separators = " ")
public class GitCommandAdd {
    public static final String COMMAND = "add";
    @Parameter(description = "暂存文件列表")
    private List<String> files;

    public List<String> getFiles() {
        return files;
    }
}
