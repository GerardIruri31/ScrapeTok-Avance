package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.AuthorizationService;
import com.example.scrapetok.domain.DTO.UpgradeToAdminRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/signup")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createUser(request));
    }


    @PostMapping("/signupadmin")
    public ResponseEntity<?> adminRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createAdmin(request));
    }

    // Falta SignIn



    // Admin concede rol de admin a User
    @PatchMapping("/upgradetoadmin")
    public ResponseEntity<?> upgradeToAdmin(@RequestBody @Valid UpgradeToAdminRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.upgrade(request));
    }
}
