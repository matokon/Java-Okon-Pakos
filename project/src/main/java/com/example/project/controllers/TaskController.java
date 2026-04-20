package com.example.project.controller;

import com.example.project.entity.Task;
import com.example.project.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {

        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task updatedTask
    ) {
        return taskRepository.findById(id)
                .map(existing -> {
                    updatedTask.setId(id);
                    return ResponseEntity.ok(taskRepository.save(updatedTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}