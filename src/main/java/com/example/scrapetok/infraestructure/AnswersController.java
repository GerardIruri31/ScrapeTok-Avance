package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.QuestionsAndAnswersService;
import com.example.scrapetok.domain.DTO.AdminAnswerRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
public class AnswersController {
    @Autowired
    private QuestionsAndAnswersService questionsAndAnswersService;

    @PatchMapping("/answerQuestion")
    public ResponseEntity<?> answerQuestion(@RequestBody @Valid AdminAnswerRequestDTO request) {
        return ResponseEntity.status(200).body(questionsAndAnswersService.replyQuestion(request));
    }
}
