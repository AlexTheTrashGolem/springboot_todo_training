package com.example.demo.todo.service;

import com.example.demo.todo.model.TodoItem;
import com.example.demo.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // ✅ Create a TodoItem
    public TodoItem createTodo(String text) {
        TodoItem newItem = new TodoItem(text, null);
        return todoRepository.save(newItem);
    }

    // ✅ Get all TodoItems
    public List<TodoItem> getAllTodos() {
        return todoRepository.findAll();
    }

    // ✅ Get a TodoItem by ID
    public Optional<TodoItem> getTodoById(String id) {
        return todoRepository.findById(id);
    }

    // ✅ Update a TodoItem
    public Optional<TodoItem> updateTodo(String id, String text) {
        return todoRepository.findById(id).map(todo -> {
            todo.setText(text);
            return todoRepository.save(todo);
        });
    }

    // ✅ Set file path for a TodoItem
    @Transactional
    public Optional<TodoItem> attachFileToTodo(String id, String filePath) {
        return todoRepository.findById(id).map(todo -> {
            todo.setFileName(filePath);
            return todoRepository.save(todo);
        });
    }

    // ✅ Remove file from TodoItem
    @Transactional
    public Optional<TodoItem> removeFileFromTodo(String id) {
        return todoRepository.findById(id).map(todo -> {
            todo.setFileName(null);
            return todoRepository.save(todo);
        });
    }

    // ✅ Soft delete a TodoItem (file is NOT deleted automatically)
    public boolean deleteTodo(String id) {
        return todoRepository.findById(id).map(todo -> {
            todo.setDeleted(true);
            todoRepository.save(todo);
            return true;
        }).orElse(false);
    }
}
