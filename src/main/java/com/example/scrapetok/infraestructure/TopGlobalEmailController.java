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
        topGlobalEmailService.sendTopGlobalTextEmail(request);
        return ResponseEntity.ok("✅ Top daily global emails have been sent successfully.");
    }
}
