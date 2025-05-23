package com.example.scrapetok.application;

import com.example.scrapetok.application.apifyservice.ApifyServerConnection;
import com.example.scrapetok.application.apifyservice.JsonProcessor;
import com.example.scrapetok.domain.AdminProfile;
import com.example.scrapetok.domain.DTO.AdminFilterRequestDTO;
import com.example.scrapetok.domain.DTO.HashtagTrendDTO;
import com.example.scrapetok.repository.AdminProfileRepository;
import com.example.scrapetok.repository.AdminTikTokMetricsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class AdminApifyCallService {
    @Autowired
    private ApifyServerConnection apifyServerConnection;
    @Autowired
    private JsonProcessor jsonProcessor;
    @Autowired
    private AdminTikTokMetricsRepository adminTikTokMetricsRepository;
    @Autowired
    private AdminProfileRepository adminProfileRepository;

    public List<Object> apifyconnection(AdminFilterRequestDTO request) throws Exception {
        List<Object> dataSet = new ArrayList<>();
        AdminProfile admin = adminProfileRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));

        // Hacer llamado a APIFY
        Map<String, Object> jsonInput = new HashMap<>();
        // TOKEN ADMINISTRADOR DE APIFY
        String apifyToken = "apify_api_89Xx79YhvkBxEWUnnAyVuQpsolqN943YHcqo";
        jsonInput.put("apifyToken", apifyToken);
        jsonInput.put("excludePinnedPosts", true);
        // Por defecto, ADMIN scrapea 20 videos por hashtags
        jsonInput.put("resultsPerPage", 10);
        List<String> hashtags = Arrays.stream(request.getHashtags().split(",")).map(String::trim).collect(Collectors.toList());
        jsonInput.put("hashtags", hashtags);

        // DEBUG: Mostrar el JSON que se enviará
        System.out.println("JSON enviado: " + jsonInput);

        // HACER LLAMADO A APIFY
        Map<String, Object> ApifyResponse = apifyServerConnection.fetchDataFromApify(jsonInput);

        // PROCESAR DATOS Y GUARDAR EN BD
        List<Map<String, Object>> data = jsonProcessor.processJson(ApifyResponse, admin);

        // CONSULTAS SQL A DATA RECIÉN GUARDADA
        List<Map<String,Object>> usernameVsViews = adminTikTokMetricsRepository.findViewsGroupedByUsernameForToday();
        List<Map<String,Object>> musicIdVsViews = adminTikTokMetricsRepository.findViewsGroupedByMusicIdForToday();
        List<Map<String,Object>> DatePostVsViews = adminTikTokMetricsRepository.findViewsGroupedByPostDateTrackedToday();
        List<HashtagTrendDTO> frecuencyAndViewsPerHashtag = adminTikTokMetricsRepository.findTopHashtagsTrendingToday();
        dataSet.add(data);
        dataSet.add(usernameVsViews);
        dataSet.add(musicIdVsViews);
        dataSet.add(DatePostVsViews);
        dataSet.add(frecuencyAndViewsPerHashtag);
        return dataSet;
    }
}

