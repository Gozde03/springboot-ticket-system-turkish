package com.example.service;

import com.example.bilet.BiletApplication;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; 
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") 
@SpringBootTest(classes = BiletApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {

    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=USER",
    
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    
    "spring.jpa.hibernate.ddl-auto=create-drop",
    
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    
    "spring.jpa.show-sql=true",
    "spring.jpa.properties.hibernate.format_sql=true"
})
public
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");
        user.setEmail("testuser@test.com");

        User saved = userService.registerUser(user);

        assertNotNull(saved.getId(), "Kayıt sonrası ID null olmamalı");
        assertEquals("testuser", saved.getUsername());
        assertEquals("testuser@test.com", saved.getEmail());
    }

    @Test
    void registerUser_duplicateUsername_throwsException() {
        User user1 = new User();
        user1.setUsername("duplicate");
        user1.setPassword("1234");
        user1.setEmail("dup1@test.com");
        userService.registerUser(user1);

        User user2 = new User();
        user2.setUsername("duplicate");
        user2.setPassword("5678");
        user2.setEmail("dup2@test.com");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user2);
        });
        assertEquals("Username already exists!", ex.getMessage());
    }
}