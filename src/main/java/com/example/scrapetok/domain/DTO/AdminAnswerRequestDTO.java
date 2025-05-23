package com.example.scrapetok.domain.DTO;

import com.example.scrapetok.domain.enums.statusQA;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminAnswerRequestDTO {
    @NotBlank
    @Enumerated(EnumType.STRING)
    private statusQA status;
    @NotBlank
    private Long questionId;
    @NotBlank
    private Long adminId;
    @NotBlank
    private String answerDescription;
}
