package com.example.demo.todo.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TodoItem {
    private String id;
    private String text;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TodoItem(String text) {
        this.id = UUID.randomUUID().toString(); // Generate unique ID
        this.text = text;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; this.updatedAt = LocalDateTime.now(); }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { this.isDeleted = deleted; this.updatedAt = LocalDateTime.now(); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}