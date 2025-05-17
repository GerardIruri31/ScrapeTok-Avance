package com.example.scrapetok.domain.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminFilterDTO {
    @NotBlank
    private String email;
    // Separador en string -> ','
    @NotBlank
    private String hashtags;
}
