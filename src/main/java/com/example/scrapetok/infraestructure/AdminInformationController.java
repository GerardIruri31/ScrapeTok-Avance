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
@RequestMapping("/admin")
public class AdminInformationController {
    @Autowired
    private UserAdminProfileService userAdminProfileService;

    @GetMapping("/profile/{adminId}")
    public ResponseEntity<?> profile(@PathVariable Long adminId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userAdminProfileService.getAdminProfile(adminId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Entity Error: " +e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server Error: " +e.getMessage());
        }
    }
}
