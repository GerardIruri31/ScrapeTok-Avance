package com.example.scrapetok.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpgradeToAdminRequestDTO {
    @NotBlank
    private Long userid;
    @NotBlank
    private Long adminId;
}
