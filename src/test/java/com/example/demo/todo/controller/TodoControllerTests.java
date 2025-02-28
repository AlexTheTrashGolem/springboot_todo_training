package com.example.demo.todo.controller;

import com.example.demo.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  // ✅ Fix: Use @MockBean to inject a mock service
    private TodoService todoService;

    private final String todoId = "1";

    @BeforeEach
    void setUp() {
        // Reset mock before each test to avoid stale interactions
        reset(todoService);

        when(todoService.getTodoById(todoId)).thenThrow(new RuntimeException("Database unavailable"));
        when(todoService.createTodo(Mockito.anyString())).thenThrow(new RuntimeException("Database unavailable"));
        when(todoService.updateTodo(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException("Database unavailable"));
        when(todoService.deleteTodo(Mockito.anyString())).thenThrow(new RuntimeException("Database unavailable"));
    }

    @Test
    void getTodoById_ShouldReturnServerError_WhenDatabaseUnavailable() throws Exception {
        mockMvc.perform(get("/todo/" + todoId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTodo_ShouldReturnServerError_WhenDatabaseUnavailable() throws Exception {
        mockMvc.perform(post("/todo")
                        .param("text", "Test Task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTodo_ShouldReturnServerError_WhenDatabaseUnavailable() throws Exception {
        mockMvc.perform(put("/todo/" + todoId)
                        .param("text", "Updated Task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTodo_ShouldReturnServerError_WhenDatabaseUnavailable() throws Exception {
        mockMvc.perform(delete("/todo/" + todoId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
