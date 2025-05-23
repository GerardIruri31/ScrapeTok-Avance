package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.TopGlobalEmailService;
import com.example.scrapetok.domain.DTO.TopGlobalEmailDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class TopGlobalEmailController {
    @Autowired
    private TopGlobalEmailService topGlobalEmailService;

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid List<TopGlobalEmailDTO> request) {
        try {
            topGlobalEmailService.sendTopGlobalTextEmail(request);
            return ResponseEntity.status(HttpStatus.OK).body("✅ Top daily global emails have been sent successfully.\"");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Entity not found Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Failed to send top global emails: " + e.getMessage());
        }
    }
}
