package com.example.scrapetok.domain.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TopGlobalEmailDTO {
    @NotBlank
    private Long adminId;
    @NotBlank
    private String usedHashTag;
    @NotBlank
    private LocalDate datePosted;
    @NotBlank
    private String usernameTiktokAccount;
    @NotBlank
    private String postURL;
    @NotBlank
    @Min(value = 1, message = "At least 1 view must be requested")
    private Integer views;
    @NotBlank
    @Min(value = 1, message = "At least 1 like must be requested")
    private Integer likes;
    @NotBlank
    private Double engagement;
}
