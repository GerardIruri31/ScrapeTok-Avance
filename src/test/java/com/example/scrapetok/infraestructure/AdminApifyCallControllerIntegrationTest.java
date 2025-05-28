package com.example.scrapetok.infraestructure;

import com.example.scrapetok.domain.DTO.AdminFilterRequestDTO;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AdminApifyCallControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GeneralAccountRepository generalAccountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long adminId;

    @BeforeEach
    public void setUp() {
        generalAccountRepository.deleteAll();

        GeneralAccount admin = new GeneralAccount();
        admin.setEmail("admin1@test.com");
        admin.setPassword("securePass123");
        admin.setFirstname("Admin");
        admin.setLastname("Tester");
        admin.setUsername("adminuser");
        admin.setRole(Role.ADMIN);
        admin.setCreationDate(java.time.LocalDate.now());

        adminId = generalAccountRepository.save(admin).getId();
    }

    @Test
    @Order(1)
    public void testAdminApifyCall() throws Exception {
        AdminFilterRequestDTO request = new AdminFilterRequestDTO();
        request.setAdminId(adminId);
        request.setHashtags("tiktok,funny");

        mockMvc.perform(post("/admin/apifycall")
                        .with(user("adminuser").roles("ADMIN")) // 👈 Simula autenticación como ADMIN
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void testAdminApifyCallWithInvalidId() throws Exception {
        AdminFilterRequestDTO request = new AdminFilterRequestDTO();
        request.setAdminId(99999L); // ID inválido
        request.setHashtags("fails");

        mockMvc.perform(post("/admin/apifycall")
                        .with(user("adminuser").roles("ADMIN")) // 👈 Simula autenticación como ADMIN
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Admin not found"));
    }
}
