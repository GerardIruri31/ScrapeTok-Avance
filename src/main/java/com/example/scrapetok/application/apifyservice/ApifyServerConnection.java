package com.example.scrapetok.application.apifyservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;


@Service
public class ApifyServerConnection {
    public Map<String,Object> fetchDataFromApify(Map<String,Object> jsonInput) {
        try {
            // Convertir el diccionario a un JSON usando Jackson
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

                Map<String, Object> responseMap = objectMapper.readValue(
                        response.toString(), new TypeReference<Map<String, Object>>() {});

                // Interpret API response
                if (responseMap.containsKey("onError")) {
                    return Map.of("error", responseMap.get("onError"));
                } else if (responseMap.containsKey("onSuccess")) {
                    return Map.of("data", responseMap.get("onSuccess"));
                } else {
                    return Map.of("error", "Unexpected API response format");
                }
            }
        } catch (Exception e) {
            return Map.of("error", "Error connecting to API: " + e.getMessage());
        }
    }
}
