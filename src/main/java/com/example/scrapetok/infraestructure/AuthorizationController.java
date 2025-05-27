package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.AuthorizationService;
import com.example.scrapetok.domain.DTO.LoginRequestDTO;
import com.example.scrapetok.domain.DTO.LoginResponseDTO;
import com.example.scrapetok.domain.DTO.UpgradeToAdminRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import com.example.scrapetok.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createUser(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signupadmin")
    public ResponseEntity<?> adminRegistration(@RequestBody @Valid UserSignUpRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.createAdmin(request));
    }

    // Falta SignIn



    // Admin concede rol de admin a User
    @PatchMapping("/upgradetoadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> upgradeToAdmin(@RequestBody @Valid UpgradeToAdminRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorizationService.upgrade(request));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authorizationService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error: " + e.getMessage());
        }
    }
}
