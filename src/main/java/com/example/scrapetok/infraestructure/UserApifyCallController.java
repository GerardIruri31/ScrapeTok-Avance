package com.example.scrapetok.infraestructure;

import com.example.scrapetok.application.UserApifyCallService;
import com.example.scrapetok.domain.UserFiltersDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserApifyCallController {
    @Autowired
    private UserApifyCallService userApifyCallService;


    @PostMapping("/apifycall")
    public ResponseEntity<Map<String,Object>> makeApifyCall(@RequestBody @Valid UserFiltersDTO request) {
        return ResponseEntity.ok(userApifyCallService.apifyconnection(request));
    }
}
