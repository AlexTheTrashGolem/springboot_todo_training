package com.example.demo.todo.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.demo.todo.model.TodoItem;

@Repository
public class TodoRepository {
    private final Map<String, TodoItem> todoMap = new ConcurrentHashMap<>();

    public TodoItem createRecord (TodoItem record) {
        todoMap.put(record.getId(), record);
        return record;
    }

    public Optional<TodoItem> findById (String id){
        TodoItem record = todoMap.get(id);
        return Optional.ofNullable(record);
    }

    public List<TodoItem> findAll (int page, int size){
        return todoMap.values().stream()
            .filter(todo -> !todo.isDeleted())
            .sorted(Comparator.comparing(TodoItem::getId))
            .skip((long)(page-1)*size)
            .limit(size)
            .toList();

    }

    public void deleteRecord (String id){
        todoMap.computeIfPresent(id, (key, todo) -> {todo.setDeleted(true); return todo;});
        
    }

    public Optional<TodoItem> updateRecord (String id, String text){
        TodoItem record = todoMap.get(id);
        if (record != null){
            record.setText(text);
        }
        return Optional.ofNullable(record);
    }


}
