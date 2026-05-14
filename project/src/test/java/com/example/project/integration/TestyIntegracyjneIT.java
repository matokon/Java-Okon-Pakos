package com.example.project.integration;

import com.example.project.entity.Project;
import com.example.project.entity.User;
import com.example.project.repository.ProjectRepository;
import com.example.project.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class TestyIntegracyjneIT {

    // tymczasowa basa PostgreSQL
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // podmianka neona na lokalna baze zeby nie smiecic w glownej bazie (z tego co czytalem tak sie robi na produkcji)
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private com.example.project.repository.TaskRepository taskRepository;

    @Autowired
    private com.example.project.service.ProjectService projectService;

    @Autowired
    private com.example.project.service.TaskService taskService;

    @Autowired
    private com.example.project.service.UserService userService;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Cel: Sprawdzenie poprawności relacji między użytkownikami a projektami")
    void shouldAssignUserToProject() throws Exception {

        User user = new User("Franciszek");
        user = userRepository.save(user);

        Project project = new Project("Testowy Projekt", "Opis projektu");
        project = projectRepository.save(project);

        //MockMvc
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))


                .andExpect(status().isOk());


        Project updatedProject = projectRepository.findById(project.getId()).orElseThrow();
        boolean hasUser = updatedProject.getUsers().stream()
                .anyMatch(u -> u.getUsername().equals("Franciszek"));

        assertTrue(hasUser, "Użytkownik powinien być na liście w projekcie!");
    }
    @Test
    @DisplayName("Cel: Sprawdzenie pobierania wszystkich projektów (GET)")
    void shouldGetAllProjects() throws Exception {

        Project project = new Project("Projekt 1", "Opis 1");
        projectRepository.save(project);


        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Projekt 1"));
    }

    @Test
    @DisplayName("Cel: Sprawdzenie dodawania nowego projektu (POST)")
    void shouldCreateProject() throws Exception {

        Project newProject = new Project("Nowy Projekt", "Nowy Opis");


        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nowy Projekt"));


        assertEquals(1, projectRepository.count());
    }

    @Test
    @DisplayName("Cel: Sprawdzenie aktualizacji projektu (PUT)")
    void shouldUpdateProject() throws Exception {

        Project project = projectRepository.save(new Project("Stary Projekt", "Stary Opis"));
        Project updatedData = new Project("Zaktualizowany", "Nowy Opis");


        mockMvc.perform(put("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zaktualizowany"));
    }

    // testy Entity


    @Test
    @DisplayName("Cel: Sprawdzenie pobierania wszystkich zadań (GET)")
    void shouldGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cel: Sprawdzenie dodawania nowego zadania (POST)")
    void shouldCreateTask() throws Exception {
        String taskJson = "{\"title\":\"Ważne zadanie\", \"description\":\"Zrobić na wczoraj\"}";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

    // testy User

    @Test
    @DisplayName("Cel: Sprawdzenie pobierania użytkowników (GET)")
    void shouldGetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cel: Sprawdzenie dodawania użytkownika (POST)")
    void shouldCreateUser() throws Exception {
        String userJson = "{\"username\":\"NowyOkon\"}";

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))

                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cel: Sprawdzenie usuwania użytkownika (DELETE)")
    void shouldDeleteUser() throws Exception {
        User user = new User("PakosDoUsuniecia");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }
    // testy Serwisow

    @Test
    @DisplayName("Cel: Pełne pokrycie metod ProjectService")
    void shouldCoverProjectService() {

        Project p = new Project("Projekt Serwisowy", "Opis");
        p = projectService.createProject(p);
        assertNotNull(p.getId());


        assertTrue(projectService.getProjectById(p.getId()).isPresent());
        assertFalse(projectService.getAllProjects().isEmpty());


        Project updateData = new Project("Zmieniony Projekt", "Nowy Opis");
        Project updated = projectService.updateProject(p.getId(), updateData);
        assertEquals("Zmieniony Projekt", updated.getName());


        assertNull(projectService.updateProject(9999L, updateData));


        projectService.deleteProject(p.getId());
        assertTrue(projectService.getProjectById(p.getId()).isEmpty());
    }

    @Test
    @DisplayName("Cel: Pełne pokrycie metod TaskService")
    void shouldCoverTaskService() {

        com.example.project.entity.Task t = new com.example.project.entity.Task();
        t.setTitle("Zadanie Serwisowe");
        t.setDescription("Opis zadania");
        t = taskService.createTask(t);
        assertNotNull(t.getId());


        assertTrue(taskService.getTaskById(t.getId()).isPresent());
        assertFalse(taskService.getAllTasks().isEmpty());


        com.example.project.entity.Task updateData = new com.example.project.entity.Task();
        updateData.setTitle("Zmienione Zadanie");
        com.example.project.entity.Task updated = taskService.updateTask(t.getId(), updateData);
        assertEquals("Zmienione Zadanie", updated.getTitle());


        assertNull(taskService.updateTask(9999L, updateData));


        taskService.deleteTask(t.getId());
        assertTrue(taskService.getTaskById(t.getId()).isEmpty());
    }

    @Test
    @DisplayName("Cel: Pełne pokrycie metod UserService")
    void shouldCoverUserService() {

        User u = new User("UserSerwisowy");
        u = userService.createUser(u);
        assertNotNull(u.getId());

        assertTrue(userService.findById(u.getId()).isPresent());
        assertFalse(userService.getAllUsers().isEmpty());
        assertTrue(userService.existsById(u.getId()));


        User updateData = new User("ZmienionyUser");
        User updated = userService.updateUser(u.getId(), updateData);
        assertEquals("ZmienionyUser", updated.getUsername());


        assertNull(userService.updateUser(9999L, updateData));


        userService.deleteUser(u.getId());
        assertFalse(userService.existsById(u.getId()));
    }
    @Test
    @DisplayName("Cel: Sprawdzenie usuwania projektu (DELETE)")
    void shouldDeleteProject() throws Exception {

        Project project = projectRepository.save(new Project("Do usunięcia", "Opis"));


        mockMvc.perform(delete("/api/projects/" + project.getId()))
                .andExpect(status().isNoContent());


        assertEquals(0, projectRepository.count());
    }
}