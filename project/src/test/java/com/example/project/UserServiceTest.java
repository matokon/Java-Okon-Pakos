package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        // tworzenie symulowanych rekordów
        User user1 = new User();
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setUsername("TestUser2");

        // gdy wywołane zostanie findAll(), zwróć listę
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // wywołanie testowanej metody
        List<User> users = userService.getAllUsers();

        // sprawdzenie czy zwrócono 2 użytkowników
        assertEquals(2, users.size());

        //sprawdzenie czy metoda findAll() została wywołana dokładnie raz
        verify(userRepository, times(1)).findAll();
    }
}