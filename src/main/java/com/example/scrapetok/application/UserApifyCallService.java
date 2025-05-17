package com.example.scrapetok.application;

import com.example.scrapetok.application.apifyservice.ApifyServerConnection;
import com.example.scrapetok.application.apifyservice.JsonProcessor;
import com.example.scrapetok.domain.UserAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.domain.UserApifyFilters;
import com.example.scrapetok.domain.DTO.UserFiltersDTO;
import com.example.scrapetok.repository.UserAccountRepository;
import com.example.scrapetok.repository.UserApifyCallHistorialRepository;
import com.example.scrapetok.repository.UserApifyFilterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserApifyCallService {
    @Autowired
    private UserApifyFilterRepository userApifyFilterRepository;
    @Autowired
    private UserApifyCallHistorialRepository userApifyCallHistorialRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ApifyServerConnection apifyServerConnection;
    @Autowired
    private JsonProcessor jsonProcessor;
    @Autowired
    private ModelMapper modelMapper;

    public List<Map<String,Object>> apifyconnection(UserFiltersDTO request) throws Exception {
        UserAccount user = userAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserApifyCallHistorial historial = userApifyCallHistorialRepository.findByUserAccount_Email(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User has no historial"));
        UserApifyFilters filter = modelMapper.map(historial, UserApifyFilters.class);
        filter.setHistorial(historial);
        userApifyFilterRepository.save(filter);
        historial.setAmountScrappedAccount(historial.getAmountScrappedAccount()+1);
        userApifyCallHistorialRepository.save(historial);

        // Hacer llamado a APIFY
        Map<String, Object> jsonInput = new HashMap<>();
        // TOKEN ADMINISTRADOR DE APIFY
        String apifyToken = "apify_api_89Xx79YhvkBxEWUnnAyVuQpsolqN943YHcqo";
        jsonInput.put("apifyToken", apifyToken);
        jsonInput.put("resultsPerPage", request.getNlastPostByHashtags());

        jsonInput.put("excludePinnedPosts", true);
        if (request.getHashtags() != null && !request.getHashtags().isEmpty() && request.getNlastPostByHashtags() != null) {
            List<String> hashtags = Arrays.stream(request.getHashtags().split(",")).map(String::trim).collect(Collectors.toList());
            jsonInput.put("hashtags", hashtags);
        }
        if ((request.getDateFrom() != null) && (request.getDateTo() != null)) {
            jsonInput.put("oldestPostDate", request.getDateFrom());
            jsonInput.put("newestPostDate", request.getDateTo());
        }
        if ((request.getMinLikes() != null) && (request.getMaxLikes() != null)) {
            jsonInput.put("leastDiggs", request.getMinLikes());
            jsonInput.put("mostDiggs", request.getMaxLikes());
        }
        if (request.getTiktokAccount() != null) {
            String lowerTiktokAccounts = request.getTiktokAccount().toLowerCase();
            List<String> tiktokUsername = Arrays.stream(lowerTiktokAccounts.split(",")).map(String::trim).collect(Collectors.toList());
            jsonInput.put("profiles", tiktokUsername);
        }
        // DEBUG: Mostrar el JSON que se enviará
        System.out.println("JSON enviado: " + jsonInput);
        Map<String, Object> ApifyResponse = apifyServerConnection.fetchDataFromApify(jsonInput, filter.getId());
        return jsonProcessor.processJson(ApifyResponse, user, filter.getId());
    }
}
