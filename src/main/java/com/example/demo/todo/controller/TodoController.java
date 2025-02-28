package com.example.demo.todo.controller;

import com.example.demo.todo.model.TodoItem;
import com.example.demo.todo.repository.TodoRepository;
import com.example.demo.todo.service.FileDAO;
import com.example.demo.todo.service.TodoService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;


	@Autowired
	private FileDAO fileDAO;

    @PostMapping
    public ResponseEntity<TodoItem> createTodo(@RequestParam String text) {
        TodoItem newItem = todoService.createTodo(text);
        if (newItem == null) {
            throw new RuntimeException("Database unavailable");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    

    // ✅ Get all TodoItems
    @GetMapping
    public ResponseEntity<List<TodoItem>> getAllTodos() {
        List<TodoItem> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    // ✅ Get a TodoItem by ID
    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoById(@PathVariable String id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Database unavailable"));
    }
    
    // ✅ Update a TodoItem
    @PutMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodo(@PathVariable String id, @RequestParam String text) {
        return todoService.updateTodo(id, text)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Database unavailable"));
    }
    

    // ✅ Delete a TodoItem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id) {
        if (todoService.deleteTodo(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new RuntimeException("Database unavailable");
    }
    

    // ✅ Upload a file and link it to a TodoItem
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = id + "_" + file.getOriginalFilename();
            String filePath = fileDAO.writeFile(inputStream, fileName);
            todoService.attachFileToTodo(id, filePath);
            return ResponseEntity.ok("File uploaded and linked successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed!");
        }
    }

    // ✅ Download a file
    @GetMapping("/{id}/download")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) {
		Optional<TodoItem> todoOpt = todoService.getTodoById(id);
		if (todoOpt.isEmpty() || todoOpt.get().getFileName() == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			InputStream fileStream = fileDAO.readFile(todoOpt.get().getFileName());
			InputStreamResource resource = new InputStreamResource(fileStream);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id + "_file");

			return ResponseEntity.ok()
					.headers(headers)
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

    // ✅ Delete a file
    @DeleteMapping("/{id}/delete-file")
    public ResponseEntity<String> deleteFile(@PathVariable String id) {
        Optional<TodoItem> todoOpt = todoService.getTodoById(id);
        if (todoOpt.isEmpty() || todoOpt.get().getFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            fileDAO.deleteFile(todoOpt.get().getFileName());
            todoService.removeFileFromTodo(id);
            return ResponseEntity.ok("File deleted successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file!");
        }
    }
}