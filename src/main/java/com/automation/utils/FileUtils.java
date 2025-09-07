package com.automation.utils;

import com.automation.logger.LoggerManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for file operations
 * Provides methods to handle various file operations like copy, move, delete, etc.
 */
public class FileUtils {
    
    private static final Logger logger = LoggerManager.getLogger(FileUtils.class);
    
    /**
     * Check if file exists
     * @param filePath Path to the file
     * @return true if file exists
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        boolean exists = file.exists();
        logger.debug("File exists check for {}: {}", filePath, exists);
        return exists;
    }
    
    /**
     * Check if directory exists
     * @param dirPath Path to the directory
     * @return true if directory exists
     */
    public static boolean directoryExists(String dirPath) {
        File dir = new File(dirPath);
        boolean exists = dir.exists() && dir.isDirectory();
        logger.debug("Directory exists check for {}: {}", dirPath, exists);
        return exists;
    }
    
    /**
     * Create directory if it doesn't exist
     * @param dirPath Path to the directory
     * @return true if directory was created or already exists
     */
    public static boolean createDirectory(String dirPath) {
        try {
            File dir = new File(dirPath);
            boolean created = dir.mkdirs();
            if (created) {
                logger.info("Created directory: {}", dirPath);
            } else {
                logger.debug("Directory already exists: {}", dirPath);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error creating directory: {}", dirPath, e);
            return false;
        }
    }
    
    /**
     * Delete file
     * @param filePath Path to the file
     * @return true if file was deleted successfully
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("Deleted file: {}", filePath);
            } else {
                logger.warn("Failed to delete file: {}", filePath);
            }
            return deleted;
        } catch (Exception e) {
            logger.error("Error deleting file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Delete directory and all its contents
     * @param dirPath Path to the directory
     * @return true if directory was deleted successfully
     */
    public static boolean deleteDirectory(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.exists()) {
                org.apache.commons.io.FileUtils.deleteDirectory(dir);
                logger.info("Deleted directory: {}", dirPath);
                return true;
            } else {
                logger.warn("Directory does not exist: {}", dirPath);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error deleting directory: {}", dirPath, e);
            return false;
        }
    }
    
    /**
     * Copy file from source to destination
     * @param sourcePath Source file path
     * @param destPath Destination file path
     * @return true if copy was successful
     */
    public static boolean copyFile(String sourcePath, String destPath) {
        try {
            File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);
            
            // Create parent directories if they don't exist
            createDirectory(destFile.getParent());
            
            org.apache.commons.io.FileUtils.copyFile(sourceFile, destFile);
            logger.info("Copied file from {} to {}", sourcePath, destPath);
            return true;
        } catch (Exception e) {
            logger.error("Error copying file from {} to {}", sourcePath, destPath, e);
            return false;
        }
    }
    
    /**
     * Copy directory from source to destination
     * @param sourcePath Source directory path
     * @param destPath Destination directory path
     * @return true if copy was successful
     */
    public static boolean copyDirectory(String sourcePath, String destPath) {
        try {
            File sourceDir = new File(sourcePath);
            File destDir = new File(destPath);
            
            org.apache.commons.io.FileUtils.copyDirectory(sourceDir, destDir);
            logger.info("Copied directory from {} to {}", sourcePath, destPath);
            return true;
        } catch (Exception e) {
            logger.error("Error copying directory from {} to {}", sourcePath, destPath, e);
            return false;
        }
    }
    
    /**
     * Move file from source to destination
     * @param sourcePath Source file path
     * @param destPath Destination file path
     * @return true if move was successful
     */
    public static boolean moveFile(String sourcePath, String destPath) {
        try {
            File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);
            
            // Create parent directories if they don't exist
            createDirectory(destFile.getParent());
            
            org.apache.commons.io.FileUtils.moveFile(sourceFile, destFile);
            logger.info("Moved file from {} to {}", sourcePath, destPath);
            return true;
        } catch (Exception e) {
            logger.error("Error moving file from {} to {}", sourcePath, destPath, e);
            return false;
        }
    }
    
    /**
     * Move directory from source to destination
     * @param sourcePath Source directory path
     * @param destPath Destination directory path
     * @return true if move was successful
     */
    public static boolean moveDirectory(String sourcePath, String destPath) {
        try {
            File sourceDir = new File(sourcePath);
            File destDir = new File(destPath);
            
            org.apache.commons.io.FileUtils.moveDirectory(sourceDir, destDir);
            logger.info("Moved directory from {} to {}", sourcePath, destPath);
            return true;
        } catch (Exception e) {
            logger.error("Error moving directory from {} to {}", sourcePath, destPath, e);
            return false;
        }
    }
    
    /**
     * Read file content as string
     * @param filePath Path to the file
     * @return File content as string
     */
    public static String readFileAsString(String filePath) {
        try {
            String content = org.apache.commons.io.FileUtils.readFileToString(new File(filePath), "UTF-8");
            logger.debug("Read file content from: {} ({} characters)", filePath, content.length());
            return content;
        } catch (Exception e) {
            logger.error("Error reading file: {}", filePath, e);
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
    
    /**
     * Write string content to file
     * @param filePath Path to the file
     * @param content Content to write
     * @return true if write was successful
     */
    public static boolean writeStringToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            
            // Create parent directories if they don't exist
            createDirectory(file.getParent());
            
            org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8");
            logger.info("Written content to file: {} ({} characters)", filePath, content.length());
            return true;
        } catch (Exception e) {
            logger.error("Error writing to file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Append string content to file
     * @param filePath Path to the file
     * @param content Content to append
     * @return true if append was successful
     */
    public static boolean appendStringToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            
            // Create parent directories if they don't exist
            createDirectory(file.getParent());
            
            org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8", true);
            logger.info("Appended content to file: {} ({} characters)", filePath, content.length());
            return true;
        } catch (Exception e) {
            logger.error("Error appending to file: {}", filePath, e);
            return false;
        }
    }

    /**
     * Write Map as JSON to file using JsonUtil
     * @param filePath Destination file path
     * @param content Map content to serialize
     * @return true if write successful
     */
    public static boolean writeJson(String filePath, Map<String, Object> content) {
        String json = JsonUtil.objectToJson(content);
        return writeStringToFile(filePath, json);
    }
    
    /**
     * Get file size in bytes
     * @param filePath Path to the file
     * @return File size in bytes
     */
    public static long getFileSize(String filePath) {
        try {
            File file = new File(filePath);
            long size = file.length();
            logger.debug("File size for {}: {} bytes", filePath, size);
            return size;
        } catch (Exception e) {
            logger.error("Error getting file size: {}", filePath, e);
            return -1;
        }
    }
    
    /**
     * Get file extension
     * @param filePath Path to the file
     * @return File extension (without dot)
     */
    public static String getFileExtension(String filePath) {
        String extension = FilenameUtils.getExtension(filePath);
        logger.debug("File extension for {}: {}", filePath, extension);
        return extension;
    }
    
    /**
     * Get file name without extension
     * @param filePath Path to the file
     * @return File name without extension
     */
    public static String getFileNameWithoutExtension(String filePath) {
        String name = FilenameUtils.getBaseName(filePath);
        logger.debug("File name without extension for {}: {}", filePath, name);
        return name;
    }
    
    /**
     * Get file name with extension
     * @param filePath Path to the file
     * @return File name with extension
     */
    public static String getFileName(String filePath) {
        String name = FilenameUtils.getName(filePath);
        logger.debug("File name for {}: {}", filePath, name);
        return name;
    }
    
    /**
     * Get list of files in directory
     * @param dirPath Path to the directory
     * @return List of file names
     */
    public static List<String> getFilesInDirectory(String dirPath) {
        List<String> files = new ArrayList<>();
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] fileList = dir.listFiles();
                if (fileList != null) {
                    for (File file : fileList) {
                        if (file.isFile()) {
                            files.add(file.getName());
                        }
                    }
                }
            }
            logger.debug("Found {} files in directory: {}", files.size(), dirPath);
        } catch (Exception e) {
            logger.error("Error getting files in directory: {}", dirPath, e);
        }
        return files;
    }
    
    /**
     * Get list of subdirectories in directory
     * @param dirPath Path to the directory
     * @return List of subdirectory names
     */
    public static List<String> getSubdirectories(String dirPath) {
        List<String> directories = new ArrayList<>();
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] fileList = dir.listFiles();
                if (fileList != null) {
                    for (File file : fileList) {
                        if (file.isDirectory()) {
                            directories.add(file.getName());
                        }
                    }
                }
            }
            logger.debug("Found {} subdirectories in directory: {}", directories.size(), dirPath);
        } catch (Exception e) {
            logger.error("Error getting subdirectories in directory: {}", dirPath, e);
        }
        return directories;
    }
    
    /**
     * Create ZIP file from directory
     * @param sourceDirPath Source directory path
     * @param zipFilePath Output ZIP file path
     * @return true if ZIP creation was successful
     */
    public static boolean createZipFile(String sourceDirPath, String zipFilePath) {
        try {
            File sourceDir = new File(sourceDirPath);
            if (!sourceDir.exists() || !sourceDir.isDirectory()) {
                logger.error("Source directory does not exist or is not a directory: {}", sourceDirPath);
                return false;
            }
            
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                
                addDirectoryToZip(sourceDir, sourceDir.getName(), zos);
            }
            
            logger.info("Created ZIP file: {} from directory: {}", zipFilePath, sourceDirPath);
            return true;
        } catch (Exception e) {
            logger.error("Error creating ZIP file: {} from directory: {}", zipFilePath, sourceDirPath, e);
            return false;
        }
    }
    
    /**
     * Helper method to add directory to ZIP
     * @param dir Directory to add
     * @param baseName Base name for ZIP entries
     * @param zos ZipOutputStream
     */
    private static void addDirectoryToZip(File dir, String baseName, ZipOutputStream zos) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addDirectoryToZip(file, baseName + "/" + file.getName(), zos);
                } else {
                    addFileToZip(file, baseName + "/" + file.getName(), zos);
                }
            }
        }
    }
    
    /**
     * Helper method to add file to ZIP
     * @param file File to add
     * @param entryName Entry name in ZIP
     * @param zos ZipOutputStream
     */
    private static void addFileToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            
            zos.closeEntry();
        }
    }
    
    /**
     * Extract ZIP file to directory
     * @param zipFilePath Path to the ZIP file
     * @param destDirPath Destination directory path
     * @return true if extraction was successful
     */
    public static boolean extractZipFile(String zipFilePath, String destDirPath) {
        try {
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) {
                logger.error("ZIP file does not exist: {}", zipFilePath);
                return false;
            }
            
            // Create destination directory if it doesn't exist
            createDirectory(destDirPath);
            
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
                ZipEntry entry = zis.getNextEntry();
                
                while (entry != null) {
                    String filePath = destDirPath + File.separator + entry.getName();
                    File newFile = new File(filePath);
                    
                    // Create parent directories if they don't exist
                    createDirectory(newFile.getParent());
                    
                    if (!entry.isDirectory()) {
                        try (FileOutputStream fos = new FileOutputStream(newFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }
                    }
                    
                    entry = zis.getNextEntry();
                }
            }
            
            logger.info("Extracted ZIP file: {} to directory: {}", zipFilePath, destDirPath);
            return true;
        } catch (Exception e) {
            logger.error("Error extracting ZIP file: {} to directory: {}", zipFilePath, destDirPath, e);
            return false;
        }
    }
    
    /**
     * Get file modification time
     * @param filePath Path to the file
     * @return Last modified time in milliseconds
     */
    public static long getFileModificationTime(String filePath) {
        try {
            File file = new File(filePath);
            long lastModified = file.lastModified();
            logger.debug("File modification time for {}: {}", filePath, lastModified);
            return lastModified;
        } catch (Exception e) {
            logger.error("Error getting file modification time: {}", filePath, e);
            return -1;
        }
    }
    
    /**
     * Check if file is readable
     * @param filePath Path to the file
     * @return true if file is readable
     */
    public static boolean isFileReadable(String filePath) {
        try {
            File file = new File(filePath);
            boolean readable = file.canRead();
            logger.debug("File readable check for {}: {}", filePath, readable);
            return readable;
        } catch (Exception e) {
            logger.error("Error checking file readability: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Check if file is writable
     * @param filePath Path to the file
     * @return true if file is writable
     */
    public static boolean isFileWritable(String filePath) {
        try {
            File file = new File(filePath);
            boolean writable = file.canWrite();
            logger.debug("File writable check for {}: {}", filePath, writable);
            return writable;
        } catch (Exception e) {
            logger.error("Error checking file writability: {}", filePath, e);
            return false;
        }
    }
}
