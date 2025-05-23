package com.example.scrapetok.domain.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserFiltersRequestDTO {
    @NotBlank
    private Long userId;
    // Separador en string -> ','
    private String hashtags;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "The date must follow the format YYYY-MM-DD")
    private String dateFrom;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "The date must follow the format YYYY-MM-DD")
    private String dateTo;
    @Min(value = 0, message = "Minimum number of likes must be greater than or equal to 0")
    private Long minLikes;
    @Min(value = 0, message = "Maximum number of likes must be greater than or equal to 0")
    private Long maxLikes;
    @Min(value = 1, message = "At least 1 post must be requested")
    @Max(value = 100, message = "You cannot request more than 100 posts")
    private Long NlastPostByHashtags;
    // Separador en string -> ','
    private String tiktokAccount;

}
