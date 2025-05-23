package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.QuestionsAndAnswersService;
import com.example.scrapetok.domain.DTO.UserQuestionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class QuestionsController {
    @Autowired
    private QuestionsAndAnswersService questionsAndAnswersService;

    @PostMapping("/creatQuestion")
    public ResponseEntity<?> makeQuestion(@RequestBody @Valid UserQuestionRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(questionsAndAnswersService.assignQuestion(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Unexpected Error: " + e.getMessage());
        }
    };

    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestion() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(questionsAndAnswersService.getQuestions());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("❌ Unexpected Error: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " + e.getMessage());
        }
    };
}
