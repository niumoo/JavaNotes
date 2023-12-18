package com.wdbyte.io.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2023/12/18
 */
public class FileDelete {

    @Test
    public void deleteFile() {
        Path path = Paths.get("/Users/darcy/wdbyte/test.txt");
        try {
            Files.delete(path);
            System.out.println("文件删除成功");
        } catch (Exception e) {
            System.out.println("文件删除失败");
            e.printStackTrace();
        }
    }

    @Test
    public void deleteIfExists() {
        Path path = Paths.get("/Users/darcy/wdbyte/test.txt");
        try {
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                System.out.println("文件删除成功");
            } else {
                System.out.println("文件不存在或者无法删除");
            }
        } catch (Exception e) {
            System.out.println("文件删除失败");
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        File file = new File("/Users/darcy/wdbyte/test.txt");
        if (file.delete()) {
            System.out.println("文件删除成功");
        } else {
            System.out.println("文件删除失败");
        }
    }

    @Test
    public void deleteOnExit() {
        File file = new File("/Users/darcy/wdbyte/test.txt");
        file.deleteOnExit();
    }

    @Test
    public void forceDelete() {
        File file = new File("/Users/darcy/wdbyte/test.txt");
        try {
            FileUtils.forceDelete(file);
            System.out.println("文件删除成功");
        } catch (Exception e) {
            System.out.println("文件删除失败");
            e.printStackTrace();
        }
    }

}
