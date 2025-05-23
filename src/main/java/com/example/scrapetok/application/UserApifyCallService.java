package com.example.scrapetok.application;

import com.example.scrapetok.application.apifyservice.ApifyServerConnection;
import com.example.scrapetok.application.apifyservice.JsonProcessor;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.domain.UserApifyFilters;
import com.example.scrapetok.domain.DTO.UserFiltersRequestDTO;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.example.scrapetok.repository.UserApifyCallHistorialRepository;
import com.example.scrapetok.repository.UserApifyFilterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserApifyCallService {
    @Autowired
    private UserApifyFilterRepository userApifyFilterRepository;
    @Autowired
    private UserApifyCallHistorialRepository userApifyCallHistorialRepository;
    @Autowired
    private GeneralAccountRepository generalAccountRepository;
    @Autowired
    private ApifyServerConnection apifyServerConnection;
    @Autowired
    private JsonProcessor jsonProcessor;
    @Autowired
    private ModelMapper modelMapper;

    public List<Map<String,Object>> apifyconnection(UserFiltersRequestDTO request) throws Exception {
        // Obtener usuario que hace request
        GeneralAccount user = generalAccountRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Obtener historial del usuario
        UserApifyCallHistorial historial = user.getHistorial();
        // Crear nuevo filtro del usuario
        UserApifyFilters filter = modelMapper.map(request, UserApifyFilters.class);
        // le asigno historial al que pertenece filtro
        filter.setHistorial(historial);

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
        Map<String, Object> ApifyResponse = apifyServerConnection.fetchDataFromApify(jsonInput, filter);

        // historial.setAmountScrappedAccount(historial.getAmountScrappedAccount()+1);
        List<Map<String, Object>> processedData = jsonProcessor.processJson(ApifyResponse, user, historial, filter);

        // Guardar filter con todos los cambios
        userApifyFilterRepository.save(filter);
        // Guardar historial
        userApifyCallHistorialRepository.save(historial);
        // Guardar UserAccount
        generalAccountRepository.save(user);
        return processedData;
    }
}
