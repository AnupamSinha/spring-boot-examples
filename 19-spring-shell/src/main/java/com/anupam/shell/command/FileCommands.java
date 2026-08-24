package com.anupam.shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File-related shell commands: listing directories, getting file info, and counting words.
 */
@ShellComponent
public class FileCommands {

    @ShellMethod(key = "list-files", value = "List files in a directory")
    public String listFiles(
            @ShellOption(defaultValue = ".") String directory) throws IOException {

        Path dir = Path.of(directory);
        if (!Files.isDirectory(dir)) {
            return "Not a directory: " + directory;
        }

        try (Stream<Path> paths = Files.list(dir)) {
            return paths
                    .map(path -> {
                        String type = Files.isDirectory(path) ? "[DIR]  " : "[FILE] ";
                        return type + path.getFileName();
                    })
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
    }

    @ShellMethod(key = "file-info", value = "Display file information")
    public String fileInfo(String file) throws IOException {
        Path path = Path.of(file);
        if (!Files.exists(path)) {
            return "File not found: " + file;
        }

        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        return """
                File:     %s
                Size:     %d bytes
                Created:  %s
                Modified: %s
                Type:     %s
                """.formatted(
                path.toAbsolutePath(),
                attrs.size(),
                attrs.creationTime(),
                attrs.lastModifiedTime(),
                attrs.isDirectory() ? "directory" : "file"
        );
    }

    @ShellMethod(key = "word-count", value = "Count words in a file")
    public String wordCount(String file) throws IOException {
        Path path = Path.of(file);
        if (!Files.exists(path)) {
            return "File not found: " + file;
        }

        String content = Files.readString(path);
        long lines = content.lines().count();
        long words = content.isBlank() ? 0 :
                Stream.of(content.split("\\s+")).count();
        long chars = content.length();

        return "%d lines, %d words, %d characters".formatted(lines, words, chars);
    }
}
