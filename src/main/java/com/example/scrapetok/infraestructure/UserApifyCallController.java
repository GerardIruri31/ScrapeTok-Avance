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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserApifyCallController {
    @Autowired
    private UserApifyCallService userApifyCallService;

    @PostMapping("/apifycall")
    public ResponseEntity<?> makeApifyCall(@RequestBody @Valid UserFiltersRequestDTO request) {
        return ResponseEntity.ok(userApifyCallService.apifyconnection(request));
    }
}