package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.UserAdminProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserInformationController {
    @Autowired
    private UserAdminProfileService userAdminProfileService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> profile(@PathVariable Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userAdminProfileService.getUserProfile(userId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Entity Error: " +e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " +e.getMessage());
        }
    }
}
