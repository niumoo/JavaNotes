package com.wdbyte.jcommander.v1;

import com.beust.jcommander.JCommander;

/**
 * @author https://www.wdbyte.com
 * @date 2023/06/15
 */
public class GitApp {

    public static void main(String[] args) {
        args = new String[]{"clone","http://www.wdbyte.com/test.git"};
        GitCommandOptions gitCommandOptions = new GitCommandOptions();
        JCommander commander = JCommander.newBuilder()
            .addObject(gitCommandOptions)
            .build();
        commander.parse(args);
        System.out.println("clone " + gitCommandOptions.getCloneUrl());
    }
}
