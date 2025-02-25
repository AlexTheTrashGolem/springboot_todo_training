package com.example.demo.todo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.todo.model.TodoItem;
import com.example.demo.todo.repository.TodoRepository;


@RestController
@RequestMapping("/todos")
public class TodoController {

	private final TodoRepository repo;

	public TodoController(TodoRepository repo){
		this.repo = repo;
	}

	@GetMapping()
	List<TodoItem> findAll(@RequestParam (defaultValue = "1") int page,@RequestParam (defaultValue = "10") int size){
		return repo.findAll(page, size);
	} 

	@PostMapping()
	TodoItem createRecord(@RequestParam String text){
		TodoItem todo = new TodoItem(text);
		return repo.createRecord(todo);
		
	}

	@PostMapping("/{id}")
	ResponseEntity<TodoItem> updateRecord(@PathVariable String id, @RequestParam String text){
		Optional<TodoItem> todo = repo.updateRecord(id, text);
		
		return todo.isEmpty()?ResponseEntity.notFound().build() : ResponseEntity.ok(todo.get());
	}

	@DeleteMapping("/{id}")
	void deleteRecord(@PathVariable String id){
		repo.deleteRecord(id);
	}





}