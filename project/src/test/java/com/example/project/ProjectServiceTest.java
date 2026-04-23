package com.example.project.service;

import com.example.project.entity.Project;
import com.example.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    // deklarujemy repo i serwis
    private ProjectRepository projectRepoMock;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        // mockujemy repo, bo tak kazali w pdfie do labów
        projectRepoMock = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepoMock);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        // tworzymy testowe obiekty z palca
        Project proj1 = new Project();
        proj1.setName("Projekt Zaliczeniowy");

        Project proj2 = new Project();
        proj2.setName("Projekt na poprawe");

        // jak ktos wola findAll, to zwracamy nasza liste
        when(projectRepoMock.findAll()).thenReturn(Arrays.asList(proj1, proj2));

        List<Project> result = projectService.getAllProjects();

        // musi zwrocic 2, inaczaj fail
        assertEquals(2, result.size());
        // weryfikacja czy wywolano raz
        verify(projectRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project nowyProjekt = new Project();
        nowyProjekt.setName("Nowy super projekt");

        // symulacja zapisu do bazy
        when(projectRepoMock.save(nowyProjekt)).thenReturn(nowyProjekt);

        Project zapisany = projectService.createProject(nowyProjekt);

        assertNotNull(zapisany);
        assertEquals("Nowy super projekt", zapisany.getName());
        verify(projectRepoMock, times(1)).save(nowyProjekt);
    }

    @Test
    @DisplayName("Should find project by ID")
    void testGetProjectById() {
        Project p = new Project();
        p.setName("Istniejacy projekt");

        // tu zawijamy w Optional, bo tak zwraca spring
        when(projectRepoMock.findById(1L)).thenReturn(Optional.of(p));

        Optional<Project> znaleziony = projectService.getProjectById(1L);

        assertTrue(znaleziony.isPresent());
        assertEquals("Istniejacy projekt", znaleziony.get().getName());
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        // po prostu odpalamy usuniecie
        projectService.deleteProject(1L);
        // i patrzymy czy faktycznie poszlo do repo
        verify(projectRepoMock, times(1)).deleteById(1L);
    }
}