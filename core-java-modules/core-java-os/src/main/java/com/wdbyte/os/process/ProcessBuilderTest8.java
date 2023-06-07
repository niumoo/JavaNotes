package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Java 9 中新增的管道操作
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest8 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder ls = new ProcessBuilder("/bin/bash", "-c", "ls -l");
        ProcessBuilder wc = new ProcessBuilder("wc", "-l");
        // 追加日志到文件
        File pipeLineLogFile = getFile(BASE_DIR + "/pipe_line_log.txt");
        wc.redirectOutput(Redirect.appendTo(pipeLineLogFile));

        List<Process> processes = ProcessBuilder.startPipeline(Arrays.asList(ls, wc));
        Process process = processes.get(processes.size() - 1);

        System.out.println("pid:" + process.pid());
        System.out.println("exitCode:" + process.waitFor());

        Files.lines(pipeLineLogFile.toPath()).forEach(System.out::println);
    }

    public static File getFile(String filePath) throws IOException {
        File logFile = new File(filePath);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        return logFile;
    }
}
