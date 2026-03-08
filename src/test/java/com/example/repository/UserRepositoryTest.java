package com.example.repository;

import com.example.bilet.BiletApplication;
import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BiletApplication.class)

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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }
    @Test
    void findByUsername_shouldReturnUser_whenExists() {
        User u = new User();
        u.setUsername("ali");
        u.setEmail("ali@test.com");
        u.setPassword("1234");
        userRepository.save(u);

        Optional<User> found = userRepository.findByUsername("ali");

        assertTrue(found.isPresent());
        assertEquals("ali", found.get().getUsername());
    }
    @Test
    void findByUsername_shouldReturnEmpty_whenNotExists() {
        assertTrue(userRepository.findByUsername("no-such-user").isEmpty());
    }
    @Test
    void existsByUsername_shouldReturnTrue_whenExists() {
        User u = new User();
        u.setUsername("veli");
        u.setEmail("veli@test.com");
        u.setPassword("1234");
        userRepository.save(u);
        assertTrue(userRepository.existsByUsername("veli"));
    }
    @Test
    void existsByUsername_shouldReturnFalse_whenNotExists() {
        assertFalse(userRepository.existsByUsername("nobody"));
    }
}