package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.AuthorizationService;
import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpResponseDTO;
import com.example.scrapetok.domain.GeneralAccount;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/signup")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createUser(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error: " + e.getMessage());
        }
    }

    // Falta SignIn






}
