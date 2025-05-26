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
    public ResponseEntity<?> makeApifyCall(@RequestBody @Valid AdminFilterRequestDTO request) throws Exception {
        return ResponseEntity.ok(AdminApifyCallService.apifyconnection(request));
    }
}
