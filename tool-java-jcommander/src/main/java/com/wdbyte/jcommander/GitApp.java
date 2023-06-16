package com.wdbyte.jcommander;

import java.nio.file.Path;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 * Git APP
 *
 * @author niulang
 * @date 2023/06/07
 */
public class GitApp {
    private static GitCommandOptions commandOptions = new GitCommandOptions();
    private static GitCommandCommit commandCommit = new GitCommandCommit();
    private static GitCommandAdd commandAdd = new GitCommandAdd();
    private static JCommander commander;

    static {
        commander = JCommander.newBuilder()
            .programName("GitApp")
            .addObject(commandOptions)
            .addCommand(commandAdd)
            .addCommand(commandCommit)
            .build();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            commander.usage();
            return;
        }
        try {
            commander.parse(args);
            if (commandOptions.isHelp()) {
                commander.usage();
                return;
            }
            if (commandOptions.isVersion()) {
                System.out.println("git version 2.24.3 (Apple Git-128)");
            }
            if (commandOptions.getCloneUrl() != null) {
                System.out.printf("开始克隆远程仓库数据：%s%n", commandOptions.getCloneUrl());
                return;
            }
            String parsedCommand = commander.getParsedCommand();
            if (GitCommandCommit.COMMAND.equals(parsedCommand)) {
                System.out.printf("提交暂存的文件并注释：%s%n", commandCommit.getComment());
                return;
            }
            if (GitCommandAdd.COMMAND.equals(parsedCommand)) {
                for (Path file : commandAdd.getFiles()) {
                    System.out.printf("暂存文件：%s%n", file);
                }
                return;
            }
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            commander.usage();
        }
    }

}
