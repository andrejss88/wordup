package com.wordup.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FileUtils {

    public static List<String> convertFileToStringList(String filePath) {
        String content;
        try {
            content = Files.readString(Paths.get(filePath), UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("", e);
        }
        return stream(content.split("\\n"))
                .map(String::strip)
                .collect(toList());
    }

    public static void write(String s, Path path) {
        try {
            Files.write(path, String.join("\n", s).getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
