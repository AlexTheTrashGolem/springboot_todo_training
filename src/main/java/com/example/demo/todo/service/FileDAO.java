package com.example.demo.todo.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileDAO {
    
    private static final Path FILE_PATH = Paths.get("uploadDir");

    public FileDAO() throws IOException {
        if (!Files.exists(FILE_PATH)) {
            Files.createDirectories(FILE_PATH);
        }
    }

    // ✅ Read a file
    public InputStream readFile(String filePath) throws IOException {
        Path fullPath = Paths.get(filePath);
        if (!Files.exists(fullPath)) {
            throw new IOException("File not found: " + filePath);
        }
        return Files.newInputStream(fullPath);
    }

    // ✅ Write a file
    public String writeFile(InputStream file, String fileName) throws IOException {
        Path fileLocation = FILE_PATH.resolve(fileName);
        Files.copy(file, fileLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileLocation.toString();
    }

    // ✅ Delete a file
    public boolean deleteFile(String filePath) throws IOException {
        Path fileLocation = Paths.get(filePath);
        return Files.deleteIfExists(fileLocation);
    }
}
