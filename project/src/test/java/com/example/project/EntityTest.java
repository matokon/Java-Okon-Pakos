package com.example.project.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    @DisplayName("Should test all methods in Project entity")
    void testProject() {
        // Test pustego konstruktora i setterów
        Project p1 = new Project();
        p1.setName("P1");
        p1.setDescription("D1");

        assertNull(p1.getId()); // ID jest nullem przed zapisem do bazy
        assertEquals("P1", p1.getName());
        assertEquals("D1", p1.getDescription());

        // Test konstruktora z parametrami
        Project p2 = new Project("P2", "D2");
        assertEquals("P2", p2.getName());
        assertEquals("D2", p2.getDescription());
    }

    @Test
    @DisplayName("Should test all methods in Task entity")
    void testTask() {
        Task t = new Task();
        t.setId(99L);
        t.setTitle("Tytul");
        t.setDescription("Opis");
        t.setTaskType(TaskType.HIGH_PRIORITY);

        Project p = new Project();
        t.setProject(p);

        User u = new User();
        t.setUser(u);

        assertEquals(99L, t.getId());
        assertEquals("Tytul", t.getTitle());
        assertEquals("Opis", t.getDescription());
        assertEquals(TaskType.HIGH_PRIORITY, t.getTaskType());
        assertEquals(p, t.getProject());
        assertEquals(u, t.getUser());
    }

    @Test
    @DisplayName("Should test all methods in User entity")
    void testUser() {
        // Test pustego
        User u1 = new User();
        u1.setUsername("Okon");

        assertNull(u1.getId());
        assertEquals("Okon", u1.getUsername());

        // Test z parametrem
        User u2 = new User("Pakos");
        assertEquals("Pakos", u2.getUsername());
    }

    @Test
    @DisplayName("Should test Enum TaskType")
    void testTaskType() {
        // Enum
        TaskType[] types = TaskType.values();
        assertEquals(3, types.length);
        assertEquals(TaskType.MEDIUM_PRIORITY, TaskType.valueOf("MEDIUM_PRIORITY"));
    }
}