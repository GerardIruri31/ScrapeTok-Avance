package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.UserAdminProfileService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
public class UserInformationController {
    @Autowired
    private UserAdminProfileService userAdminProfileService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> profile(@PathVariable @NotNull Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userAdminProfileService.getUserProfile(userId));
    }
}
