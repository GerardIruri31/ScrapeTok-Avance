package com.example.scrapetok.domain.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminFilterRequestDTO {
    @NotNull
    private Long adminId;
    // Separador en string -> ','
    @NotBlank
    private String hashtags;
}
