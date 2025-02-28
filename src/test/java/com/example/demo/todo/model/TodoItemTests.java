package com.example.demo.todo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TodoItemTests {
    private TodoItem todoItem;
    
    @BeforeEach
    void setUp() {
        todoItem = new TodoItem("Test Task", "test.txt");
    }
    
    @Test
    void constructor_ShouldInitializeFields() {
        assertThat(todoItem.getId()).isNotNull();
        assertThat(todoItem.getText()).isEqualTo("Test Task");
        assertThat(todoItem.getFileName()).isEqualTo("test.txt");
        assertThat(todoItem.getCreatedAt()).isNotNull();
        assertThat(todoItem.getUpdatedAt()).isNotNull();
        assertThat(todoItem.isDeleted()).isFalse();
    }
    
    @Test
    void setText_ShouldUpdateTextAndTimestamp() {
        LocalDateTime beforeUpdate = todoItem.getUpdatedAt();
        
        todoItem.setText("Updated Task");
        
        assertThat(todoItem.getText()).isEqualTo("Updated Task");
        assertThat(todoItem.getUpdatedAt()).isAfterOrEqualTo(beforeUpdate);
    }
    
    @Test
    void setDeleted_ShouldUpdateFlagAndTimestamp() {
        LocalDateTime beforeUpdate = todoItem.getUpdatedAt();
        
        todoItem.setDeleted(true);
        
        assertThat(todoItem.isDeleted()).isTrue();
        assertThat(todoItem.getUpdatedAt()).isAfterOrEqualTo(beforeUpdate);
    }
    
    @Test
    void setFileName_ShouldUpdateFileName() {
        todoItem.setFileName("newFile.txt");
        
        assertThat(todoItem.getFileName()).isEqualTo("newFile.txt");
    }
}
