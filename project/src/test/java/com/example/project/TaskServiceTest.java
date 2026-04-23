package com.example.project.service;

import com.example.project.entity.Task;
import com.example.project.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepoMock;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // czyszczenie przed kazdym testem zeby nie bylo przypalu
        taskRepoMock = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepoMock);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        // fejkowe zadania
        Task taskA = new Task();
        taskA.setTitle("Zrobic lab 3");

        Task taskB = new Task();
        taskB.setTitle("Wyslac sprawozdanie");

        when(taskRepoMock.findAll()).thenReturn(Arrays.asList(taskA, taskB));

        List<Task> zbadaneZadania = taskService.getAllTasks();

        assertEquals(2, zbadaneZadania.size());
        verify(taskRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task t = new Task();
        t.setTitle("Task testowy");

        when(taskRepoMock.save(t)).thenReturn(t);

        Task wynik = taskService.createTask(t);

        assertNotNull(wynik);
        assertEquals("Task testowy", wynik.getTitle());
        verify(taskRepoMock, times(1)).save(t);
    }

    @Test
    @DisplayName("Should find task by ID")
    void testGetTaskById() {
        Task t = new Task();
        t.setTitle("Szukane zadanie");

        when(taskRepoMock.findById(99L)).thenReturn(Optional.of(t));

        // szukamy po id 99
        Optional<Task> znalezione = taskService.getTaskById(99L);

        assertTrue(znalezione.isPresent());
        assertEquals("Szukane zadanie", znalezione.get().getTitle());
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask() {
        // kasujemy task id 5
        taskService.deleteTask(5L);
        verify(taskRepoMock, times(1)).deleteById(5L);
    }
}