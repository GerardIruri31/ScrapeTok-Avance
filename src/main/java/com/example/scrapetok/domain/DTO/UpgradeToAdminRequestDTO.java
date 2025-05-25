package com.example.scrapetok.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpgradeToAdminRequestDTO {
    @NotNull
    private Long userid;
    @NotNull
    private Long adminId;
}
