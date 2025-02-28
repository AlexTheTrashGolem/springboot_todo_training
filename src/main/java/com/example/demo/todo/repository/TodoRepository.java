package com.example.demo.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.todo.model.TodoItem;

public interface TodoRepository extends JpaRepository<TodoItem, String> {
}