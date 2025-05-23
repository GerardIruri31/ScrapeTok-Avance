package com.example.scrapetok.domain.DTO;

import com.example.scrapetok.domain.enums.statusQA;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FullAnswerQuestionResponseDTO {
    @Enumerated(EnumType.STRING)
    private statusQA status;
    private String questionDescription;
    private LocalDate questionDate;
    private LocalTime questionHour;
    private String answerDescription;
    private LocalDate answerDate;
    private LocalTime  answerHour;
}
