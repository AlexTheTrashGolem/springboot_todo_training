package com.example.demo.todo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TodoItem {
    @Id
    private String id;
    
    private String text;
    
    private boolean isDeleted;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;


    private String fileName;

    public TodoItem() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public TodoItem(String text, String fileName) {
        this();
        this.text = text;
        this.fileName = fileName;
    }


    public String getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; this.updatedAt = LocalDateTime.now(); }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { this.isDeleted = deleted; this.updatedAt = LocalDateTime.now(); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

}
