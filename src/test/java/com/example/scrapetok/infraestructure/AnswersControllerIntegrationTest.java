package com.example.scrapetok.infraestructure;

import com.example.scrapetok.domain.DTO.AdminAnswerRequestDTO;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.QuestAndAnswer;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.domain.enums.statusQA;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.example.scrapetok.repository.QuestionAndAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GeneralAccountRepository accountRepository;

    @Autowired
    private QuestionAndAnswerRepository qaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long adminId;
    private Long questionId;

    @BeforeEach
    public void setUp() {
        // 🧹 Limpiar base de datos
        accountRepository.deleteAll();
        qaRepository.deleteAll();

        // 👨‍💼 Crear Admin
        GeneralAccount admin = new GeneralAccount();
        admin.setEmail("admin@answer.com");
        admin.setPassword("pass123");
        admin.setFirstname("Admin");
        admin.setLastname("Respondiendo");
        admin.setUsername("respondeYA");
        admin.setRole(Role.ADMIN);
        admin.setCreationDate(LocalDate.now());
        adminId = accountRepository.save(admin).getId();

        // 🙋‍♂️ Crear Usuario
        GeneralAccount user = new GeneralAccount();
        user.setEmail("user@question.com");
        user.setPassword("1234");
        user.setFirstname("User");
        user.setLastname("Preguntón");
        user.setUsername("pregunta");
        user.setRole(Role.USER);
        user.setCreationDate(LocalDate.now());
        user = accountRepository.save(user);

        // ❓ Crear pregunta pendiente
        QuestAndAnswer question = new QuestAndAnswer();
        question.setUser(user);
        question.setQuestionDescription("¿Qué es ScrapeTok?");
        question.setStatus(statusQA.PENDING);
        question.setQuestionDate(LocalDate.now());
        question.setQuestionHour(LocalTime.now());
        questionId = qaRepository.save(question).getId();
    }

    @Test
    @Order(1)
    public void testAnswerQuestionSuccessfully() throws Exception {
        AdminAnswerRequestDTO request = new AdminAnswerRequestDTO();
        request.setAdminId(adminId);
        request.setQuestionId(questionId);
        request.setAnswerDescription("ScrapeTok es una app que hace scraping de TikTok y genera métricas.");

        mockMvc.perform(patch("/admin/answerQuestion")
                        .with(user("respondeYA").roles("ADMIN")) // Simulación de sesión ADMIN
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answerDescription").value("ScrapeTok es una app que hace scraping de TikTok y genera métricas."))
                .andExpect(jsonPath("$.status").value("ANSWERED"));
    }

    @Test
    @Order(2)
    public void testAnswerQuestionWithWrongAdminId() throws Exception {
        AdminAnswerRequestDTO request = new AdminAnswerRequestDTO();
        request.setAdminId(99999L); // ID inválido
        request.setQuestionId(questionId);
        request.setAnswerDescription("Intento inválido");

        mockMvc.perform(patch("/admin/answerQuestion")
                        .with(user("respondeYA").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void testAnswerQuestionAlreadyAnswered() throws Exception {
        // Marcar como respondida
        QuestAndAnswer question = qaRepository.findById(questionId).orElseThrow();
        question.setStatus(statusQA.ANSWERED);
        question.setAnswerDescription("Respuesta inicial");
        qaRepository.save(question);

        AdminAnswerRequestDTO request = new AdminAnswerRequestDTO();
        request.setAdminId(adminId);
        request.setQuestionId(questionId);
        request.setAnswerDescription("Intento de respuesta duplicada");

        mockMvc.perform(patch("/admin/answerQuestion")
                        .with(user("respondeYA").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("This question has already been answered."));
    }
}
