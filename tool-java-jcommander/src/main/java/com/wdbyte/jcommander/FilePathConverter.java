package com.wdbyte.jcommander;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author https://www.wdbyte.com
 * @date 2023/06/15
 */
public class FilePathConverter implements IStringConverter<Path> {

    @Override
    public Path convert(String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return path;
        }
        throw new ParameterException(String.format("文件不存在，path:%s", filePath));
    }
}
