package com.example.project.controller;

import com.example.project.entity.Project;
import com.example.project.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Operation(summary="Get all items")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new project")
    public Project addProject(
            @Parameter(description = "Project to add")
            @RequestBody Project project
    ) {
        return projectRepository.save(project);
    }
}