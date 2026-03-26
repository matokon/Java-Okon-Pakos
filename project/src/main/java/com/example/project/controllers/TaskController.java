package com.example.project.controller;

import com.example.project.entity.Task;
import com.example.project.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

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
}