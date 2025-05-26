package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.QuestionsAndAnswersService;
import com.example.scrapetok.domain.DTO.UserQuestionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class QuestionsController {
    @Autowired
    private QuestionsAndAnswersService questionsAndAnswersService;

    @PostMapping("/createQuestion")
    public ResponseEntity<?> makeQuestion(@RequestBody @Valid UserQuestionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionsAndAnswersService.assignQuestion(request));
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestion() {
        return ResponseEntity.ok(questionsAndAnswersService.getQuestions());
    }
}
