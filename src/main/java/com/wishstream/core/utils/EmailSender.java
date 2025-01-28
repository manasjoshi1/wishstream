package com.wishstream.core.utils;

import com.wishstream.core.dto.ComprehensiveEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class EmailSender {
    @Value("${mailtrap.api.url}")
    private String mailtrapApiUrl;

    @Value("${mailtrap.api.token}")
    private String mailtrapApiToken;


    public  void sendEmail(ComprehensiveEventDTO eventDTO)  {
        try {


            StringBuilder jsonBody = new StringBuilder();
            jsonBody.append("{");
            jsonBody.append("\"to\":[{\"email\":\"").append("raghavsphadke@gmail.com").append("\",\"name\":\"")
                    .append(eventDTO.getRelationFirstName()).append(" ").append(eventDTO.getRelationLastName()).append("\"}],");
            jsonBody.append("\"from\":{\"email\":\"").append(eventDTO.getUserEmail()).append("\",\"name\":\"").append(eventDTO.getUserFirstName()).append("\"},");
            jsonBody.append("\"reply_to\":{\"email\":\"").append(eventDTO.getUserEmail()).append("\",\"name\":\"").append(eventDTO.getUserFirstName()).append("\"},");

            jsonBody.append("\"subject\":\"").append(eventDTO.getSubject()).append("\",");

            jsonBody.append("\"text\":\"").append(eventDTO.getBody().replace("\"", "\\\"").replace("\n", "\\n")).append("\"");
            jsonBody.append("}");
            System.out.println(jsonBody.toString());
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(mailtrapApiUrl))
                    .header("Accept", "application/json")
                    .header("Authorization","Bearer " +mailtrapApiToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status code: " + response.statusCode());
            log.info("Response body: " + response.body());
        } catch (Exception e) {
            log.error(" Error  : {}",e);
        }
    }
}