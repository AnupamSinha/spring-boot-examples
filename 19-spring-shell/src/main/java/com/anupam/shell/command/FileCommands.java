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
 * Shell commands for file system operations.
 * <p>
 * Provides commands to list directory contents, display file metadata,
 * and count words/lines/characters in a file.
 * </p>
 *
 * @author Anupam
 */
@ShellComponent
public class FileCommands {

    /**
     * Lists files and directories in the specified path.
     * <p>
     * Each entry is prefixed with [DIR] or [FILE] and results are sorted alphabetically.
     * </p>
     *
     * @param directory the directory path to list; defaults to the current directory
     * @return a formatted listing of files and directories
     * @throws IOException if an I/O error occurs while reading the directory
     */
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
                        // Prefix each entry with its type indicator
                        String type = Files.isDirectory(path) ? "[DIR]  " : "[FILE] ";
                        return type + path.getFileName();
                    })
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
    }

    /**
     * Displays detailed metadata about a file or directory.
     * <p>
     * Shows absolute path, size, creation time, modification time, and type.
     * </p>
     *
     * @param file the path to the file to inspect
     * @return formatted file information, or an error message if not found
     * @throws IOException if an I/O error occurs while reading attributes
     */
    @ShellMethod(key = "file-info", value = "Display file information")
    public String fileInfo(String file) throws IOException {
        Path path = Path.of(file);
        if (!Files.exists(path)) {
            return "File not found: " + file;
        }

        // Read basic file attributes for size and timestamps
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

    /**
     * Counts lines, words, and characters in the specified file.
     *
     * @param file the path to the file to analyze
     * @return a summary string with line, word, and character counts
     * @throws IOException if an I/O error occurs while reading the file
     */
    @ShellMethod(key = "word-count", value = "Count words in a file")
    public String wordCount(String file) throws IOException {
        Path path = Path.of(file);
        if (!Files.exists(path)) {
            return "File not found: " + file;
        }

        String content = Files.readString(path);
        long lines = content.lines().count();
        // Split on whitespace to count words; handle blank content
        long words = content.isBlank() ? 0 :
                Stream.of(content.split("\\s+")).count();
        long chars = content.length();

        return "%d lines, %d words, %d characters".formatted(lines, words, chars);
    }
}
