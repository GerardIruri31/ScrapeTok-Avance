package com.example.scrapetok.config;

import com.example.scrapetok.application.UserAdminProfileService;
import com.example.scrapetok.domain.DTO.AdminProfileResponseDTO;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.exception.ResourceNotFoundException;
import com.example.scrapetok.infraestructure.AdminInformationController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AdminInformationController.class)
public class AdminInformationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserAdminProfileService userAdminProfileService;

    // Si en tu configuración de seguridad tienes un filtro de JWT, mockéalo también:
    @MockitoBean
    private com.example.scrapetok.security.JwtRequestFilter jwtRequestFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAdminProfile_Success() throws Exception {
        Long adminId = 1L;

        AdminProfileResponseDTO dto = new AdminProfileResponseDTO();
        dto.setId(adminId);
        dto.setEmail("admin@example.com");
        dto.setFirstname("María");
        dto.setLastname("López");
        dto.setUsername("marial");
        dto.setRole(Role.ADMIN);
        dto.setCreationDate(LocalDate.of(2021, 5, 10));
        dto.setAdmisionToAdminDate(LocalDate.of(2022, 1, 1));
        dto.setAdmisionToAdminTime(LocalTime.of(9, 30));
        dto.setTotalQuestionsAnswered(42);
        dto.setIsActive(true);
        // Para listas de mapas puedes simular:
        dto.setQuestionAndAnswer(List.of(
                Map.of("¿Pregunta?", "Respuesta"),
                Map.of("¿Otra?", "Otra respuesta")
        ));
        dto.setEmmitedAlerts(List.of(
                Map.of(100L, "2025-05-26"),
                Map.of(101L, "2025-05-27")
        ));

        when(userAdminProfileService.getAdminProfile(adminId)).thenReturn(dto);

        mockMvc.perform(get("/admin/profile/{adminId}", adminId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                // Ejemplos de comprobaciones individuales:
                .andExpect(jsonPath("$.id").value(adminId))
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstname").value("María"))
                .andExpect(jsonPath("$.totalQuestionsAnswered").value(42))
                .andExpect(jsonPath("$.questionAndAnswer", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.emmitedAlerts", Matchers.hasSize(2)));
    }

    @Test
    void getAdminProfile_NotFound_Returns404() throws Exception {
        Long adminId = 999L;

        when(userAdminProfileService.getAdminProfile(adminId))
                .thenThrow(new ResourceNotFoundException("Admin with id not found"));

        mockMvc.perform(get("/admin/profile/{adminId}", adminId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Admin with id not found"));
    }
}