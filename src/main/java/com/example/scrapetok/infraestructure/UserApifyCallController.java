package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.UserApifyCallService;
import com.example.scrapetok.domain.DTO.UserFiltersRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserApifyCallController {
    @Autowired
    private UserApifyCallService userApifyCallService;

    @PostMapping("/apifycall")
    public ResponseEntity<?> makeApifyCall(@RequestBody @Valid UserFiltersRequestDTO request) {
        try {
            // Retorna List<Map<String,Object>
            System.out.println(request.getNlastPostByHashtags());
            return ResponseEntity.ok(userApifyCallService.apifyconnection(request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("❌ Apify Error", "Could not connect to the Apify server. Please try again."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("❌ Unexpected Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("❌ Server Error", e.getMessage()));
        }
    }
}
