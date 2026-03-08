package com.example.controller;

import com.example.bilet.BiletApplication;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BiletApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public
class UserControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldSaveUser_andRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "ali")
                        .param("email", "ali@test.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void register_duplicateUsername_shouldShowErrorOnRegisterPage() throws Exception {
        // first register
        mockMvc.perform(post("/register")
                        .param("username", "dup")
                        .param("email", "dup1@test.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection());

        // second register same username
        mockMvc.perform(post("/register")
                        .param("username", "dup")
                        .param("email", "dup2@test.com")
                        .param("password", "5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout/layout"))
                .andExpect(model().attributeExists("error"));
    }
}