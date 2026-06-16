package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.FrameworkException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Utility class for file and directory operations.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    /**
     * Creates a directory if it does not exist.
     *
     * @param dirPath the directory path to create
     * @return the created directory path
     */
    public static Path createDirectoryIfNotExists(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.debug("Created directory: {}", path.toAbsolutePath());
            }
            return path;
        } catch (IOException e) {
            throw new FrameworkException("Failed to create directory: " + dirPath, e);
        }
    }

    /**
     * Cleans a directory by deleting all its contents (but not the directory itself).
     *
     * @param dirPath the directory path to clean
     */
    public static void cleanDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .filter(p -> !p.equals(path))
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException e) {
                                log.warn("Failed to delete: {}", p, e);
                            }
                        });
                log.debug("Cleaned directory: {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new FrameworkException("Failed to clean directory: " + dirPath, e);
        }
    }

    /**
     * Deletes a file if it exists.
     *
     * @param filePath the file path to delete
     * @return true if deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    /**
     * Gets the absolute path as a string from a relative path.
     *
     * @param relativePath the relative path
     * @return the absolute path string
     */
    public static String getAbsolutePath(String relativePath) {
        return Paths.get(relativePath).toAbsolutePath().toString();
    }

    /**
     * Checks if a file or directory exists.
     *
     * @param path the path to check
     * @return true if it exists
     */
    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    /**
     * Gets the file size in bytes.
     *
     * @param filePath the file path
     * @return the file size in bytes
     */
    public static long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            log.warn("Failed to get file size: {}", filePath, e);
            return 0;
        }
    }

    /**
     * Gets the file extension from a filename.
     *
     * @param filename the filename
     * @return the extension (e.g., "png", "txt")
     */
    public static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filename.substring(lastDot + 1);
    }
}
