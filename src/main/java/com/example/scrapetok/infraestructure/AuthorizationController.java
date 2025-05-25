package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.AuthorizationService;
import com.example.scrapetok.domain.DTO.UpgradeToAdminRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createUser(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Client Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " + e.getMessage());
        }
    }


    @PostMapping("/signupadmin")
    public ResponseEntity<?> adminRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createAdmin(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Client Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " + e.getMessage());
        }
    }

    // Falta SignIn



    // Admin concede rol de admin a User
    @PatchMapping("/upgradetoadmin")
    public ResponseEntity<?> upgradeToAdmin(@RequestBody @Valid UpgradeToAdminRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.upgrade(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Client Error : " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Entity Error : " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " + e.getMessage());
        }
    }


}
