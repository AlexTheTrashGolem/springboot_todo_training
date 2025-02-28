package com.example.demo.todo.service;

import com.example.demo.todo.model.TodoItem;
import com.example.demo.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTodo_ShouldSaveAndReturnTodoItem() {
        TodoItem mockItem = new TodoItem("Test Task", null);
        when(todoRepository.save(any(TodoItem.class))).thenReturn(mockItem);
        
        TodoItem result = todoService.createTodo("Test Task");
        
        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("Test Task");
        verify(todoRepository, times(1)).save(any(TodoItem.class));
    }

    @Test
    void getAllTodos_ShouldReturnTodoList() {
        List<TodoItem> mockList = List.of(new TodoItem("Task 1", null), new TodoItem("Task 2", null));
        when(todoRepository.findAll()).thenReturn(mockList);
        
        List<TodoItem> result = todoService.getAllTodos();
        
        assertThat(result).hasSize(2);
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodoById_ShouldReturnTodoItem_WhenExists() {
        TodoItem mockItem = new TodoItem("Test Task", null);
        when(todoRepository.findById("1")).thenReturn(Optional.of(mockItem));
        
        Optional<TodoItem> result = todoService.getTodoById("1");
        
        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo("Test Task");
        verify(todoRepository, times(1)).findById("1");
    }

    @Test
    void updateTodo_ShouldUpdateText_WhenTodoExists() {
        TodoItem mockItem = new TodoItem("Old Task", null);
        when(todoRepository.findById("1")).thenReturn(Optional.of(mockItem));
        when(todoRepository.save(any(TodoItem.class))).thenReturn(mockItem);
        
        Optional<TodoItem> result = todoService.updateTodo("1", "Updated Task");
        
        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo("Updated Task");
        verify(todoRepository, times(1)).findById("1");
        verify(todoRepository, times(1)).save(any(TodoItem.class));
    }

    @Test
    void deleteTodo_ShouldMarkTodoAsDeleted_WhenExists() {
        TodoItem mockItem = new TodoItem("Test Task", null);
        when(todoRepository.findById("1")).thenReturn(Optional.of(mockItem));
        
        boolean result = todoService.deleteTodo("1");
        
        assertThat(result).isTrue();
        assertThat(mockItem.isDeleted()).isTrue();
        verify(todoRepository, times(1)).save(mockItem);
    }

    @Test
    void getTodoById_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> todoService.getTodoById(null));
    }

    @Test
    void updateTodo_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> todoService.updateTodo(null, "Updated Task"));
    }

    @Test
    void deleteTodo_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> todoService.deleteTodo(null));
    }
}
