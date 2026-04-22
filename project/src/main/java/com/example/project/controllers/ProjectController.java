package com.example.project.controller;

import com.example.project.entity.Project;
import com.example.project.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;

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
    @Operation(summary = "Get all items")
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

    @PutMapping("/{id}")
    @Operation(
        summary = "Update existing project",
        description = "Updates a project by its ID. Returns 404 if not found."
    )
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,

            @Parameter(description = "Updated project data")
            @RequestBody Project updatedProject
    ) {
        return projectRepository.findById(id)
                .map(existing -> {
                    return ResponseEntity.ok(projectRepository.save(updatedProject));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a project",
        description = "Deletes a project by its ID. Returns 404 if not found, 204 on success."
    )
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id
    ) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}