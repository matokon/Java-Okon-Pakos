package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepoMock;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepoMock = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepoMock);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User u1 = new User(); u1.setUsername("User1");
        User u2 = new User(); u2.setUsername("User2");
        when(userRepoMock.findAll()).thenReturn(Arrays.asList(u1, u2));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        User u = new User(); u.setUsername("Nowy");
        when(userRepoMock.save(u)).thenReturn(u);
        assertNotNull(userService.createUser(u));
    }

    @Test
    @DisplayName("Should find by ID")
    void testFindById() {
        User u = new User(); u.setUsername("Szukany");
        when(userRepoMock.findById(1L)).thenReturn(Optional.of(u));
        assertTrue(userService.findById(1L).isPresent());
    }

    @Test
    @DisplayName("Should delete user")
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepoMock, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should check if exists")
    void testExistsById() {
        when(userRepoMock.existsById(1L)).thenReturn(true);
        assertTrue(userService.existsById(1L));
    }
}