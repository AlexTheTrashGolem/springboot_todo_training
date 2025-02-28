package com.example.demo.todo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileDAOTests {
    private FileDAO fileDAO;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        fileDAO = new FileDAO();
    }

    @Test
    void writeFile_ShouldWriteFileSuccessfully() throws IOException {
        String fileName = "testFile.txt";
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        
        String filePath = fileDAO.writeFile(inputStream, fileName);
        
        assertThat(Files.exists(Path.of(filePath))).isTrue();
    }

    @Test
    void readFile_ShouldReadFileSuccessfully() throws IOException {
        String fileName = "testRead.txt";
        Path testFile = tempDir.resolve(fileName);
        Files.writeString(testFile, "Sample Content");
        
        InputStream inputStream = fileDAO.readFile(testFile.toString());
        
        assertThat(inputStream).isNotNull();
    }

    @Test
    void readFile_ShouldThrowException_WhenFileNotFound() {
        String nonExistentFile = tempDir.resolve("missing.txt").toString();
        
        assertThrows(IOException.class, () -> fileDAO.readFile(nonExistentFile));
    }

    @Test
    void deleteFile_ShouldDeleteFileSuccessfully() throws IOException {
        String fileName = "testDelete.txt";
        Path testFile = tempDir.resolve(fileName);
        Files.writeString(testFile, "To be deleted");
        
        boolean isDeleted = fileDAO.deleteFile(testFile.toString());
        
        assertThat(isDeleted).isTrue();
        assertThat(Files.exists(testFile)).isFalse();
    }

    @Test
    void deleteFile_ShouldReturnFalse_WhenFileDoesNotExist() throws IOException {
        String nonExistentFile = tempDir.resolve("nonexistent.txt").toString();
        
        boolean isDeleted = fileDAO.deleteFile(nonExistentFile);
        
        assertThat(isDeleted).isFalse();
    }
}