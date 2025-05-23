package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.AdminApifyCallService;
import com.example.scrapetok.domain.DTO.AdminFilterRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminApifyCallController {
    @Autowired
    private AdminApifyCallService AdminApifyCallService;

    @PostMapping("/apifycall")
    public ResponseEntity<?> makeApifyCall(@RequestBody @Valid AdminFilterRequestDTO request) {
        try {
            // Retorna List<Map<String,Object>
            return ResponseEntity.ok(AdminApifyCallService.apifyconnection(request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("❌ Apify Error", "Could not connect to the Apify server. Please try again."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("❌ Unexpected Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("❌ Server Error", e.getMessage()));
        }
    }
}
