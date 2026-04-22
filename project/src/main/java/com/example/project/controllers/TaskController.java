package com.example.project.controller;

import com.example.project.entity.Task;
import com.example.project.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations")
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new task")
    public Task addTask(
            @Parameter(description = "Task to add")
            @RequestBody Task task
    ) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update existing task",
        description = "Updates a task by its ID. Returns 404 if not found."
    )
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,

            @Parameter(description = "Updated task data")
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
    @Operation(
        summary = "Delete a task",
        description = "Deletes a task by its ID. Returns 404 if not found, 204 on success."
    )
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id
    ) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}