package com.example.scrapetok.infraestructure;

import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeneralAccountRepository accountRepository;

    @BeforeEach
    public void cleanDatabase() {
        accountRepository.deleteAll();
    }

    private UserSignUpRequestDTO buildUserRequest() {
        UserSignUpRequestDTO request = new UserSignUpRequestDTO();
        request.setEmail("testuser@example.com");
        request.setPassword("securePass123");
        request.setFirstname("Test");
        request.setLastname("User");
        request.setUsername("testuser");
        return request;
    }

    @Test
    @Order(1)
    public void testUserSignup() throws Exception {
        UserSignUpRequestDTO request = buildUserRequest();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", Matchers.is(request.getEmail())))
                .andExpect(jsonPath("$.username", Matchers.is(request.getUsername())));
    }

    @Test
    @Order(2)
    public void testDuplicateEmailSignupFails() throws Exception {
        UserSignUpRequestDTO request = buildUserRequest();

        // Primera vez: signup correcto
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Segundo intento: debe fallar por email duplicado
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("already in use")));
    }
}
