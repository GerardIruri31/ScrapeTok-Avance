package com.example.scrapetok.application.apifyservice;

import com.example.scrapetok.domain.UserApifyFilters;
import com.example.scrapetok.repository.UserApifyFilterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;


@Service
public class ApifyServerConnection {
    @Autowired
    private UserApifyFilterRepository userApifyFilterRepository;
    public Map<String,Object> fetchDataFromApify(Map<String,Object> jsonInput, Long filterID) throws IOException, EntityNotFoundException, IllegalStateException {
        // Convertir el diccionario a un JSON usando Jackson
        long inicio = System.currentTimeMillis();
        UserApifyFilters filter = userApifyFilterRepository.findById(filterID).orElseThrow(() -> new EntityNotFoundException("Filter id: " + filterID + " Not found"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(jsonInput);
        String apiURL = "http://localhost:8000";

        // Setup HTTP connection
        String ApiURL = apiURL + "/APIFYCALL";
        URL url = new URL(ApiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(true);

        // Send request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Read response
        int responseCode = conn.getResponseCode();
        InputStream inputStream = (responseCode == HttpURLConnection.HTTP_OK)
                ? conn.getInputStream()
                : conn.getErrorStream();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            Map<String, Object> responseMap = objectMapper.readValue(response.toString(), new TypeReference<>() {});
            long fin = System.currentTimeMillis();
            int tiempoTotal = (int) (fin - inicio); // tiempo en milisegundos
            filter.setExecutionTime(tiempoTotal);
            userApifyFilterRepository.save(filter);
            // Interpret API response
            if (responseMap.containsKey("Sucess")) {
                return Map.of("Sucess", responseMap.get("Success"));
            } else if (responseMap.containsKey("Error")) {
                return Map.of("Error", responseMap.get("Error"));
            }
            throw new IllegalStateException("Internal Server error: Can't establish connection to Apify server");
        }
    }
}
