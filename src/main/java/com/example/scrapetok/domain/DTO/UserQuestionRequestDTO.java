package com.example.scrapetok.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserQuestionRequestDTO {
    @NotBlank
    private Long userId;
    @NotBlank
    private Long questionDescription;
}
