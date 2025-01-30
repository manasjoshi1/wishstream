package com.wishstream.core.service;

import com.wishstream.core.dto.ComprehensiveEventDTO;
import com.wishstream.core.model.Event;
import com.wishstream.core.model.UserRelation;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class GPTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GPTService.class);

    @Value("${gpt.api.url}") // OpenAI API URL
    private String apiUrl;

    @Value("${gpt.api.key}") // OpenAI API Key
    private String apiKey;

    @Value("${gpt.api.model}") // OpenAI API Key
    private String model;
    @Value("${gpt.api.temperature}") // OpenAI API Key
    private Float temperature;
    @Value("${gpt.api.max_tokens}") // OpenAI API Key
    private Integer tokens;
    public GPTService() {

    }
    /**
     * Generates a message using GPT based on the provided prompt.
     *
     * @param prompt The input prompt for GPT to generate the message.
     * @return The generated message as a string.
     */
    public ComprehensiveEventDTO generateMessage(JSONObject prompt, ComprehensiveEventDTO comprehensiveEventDTO) {
        try {
            LOGGER.info("Generating message with GPT...");
            // Create the request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model); // Choose the GPT model
            requestBody.put("messages", prompt.getJSONArray("messages").toList());
            requestBody.put("max_tokens", tokens); // Increased token limit for more detailed responses
            requestBody.put("temperature", temperature); // Slightly increased creativity level

            LOGGER.info("GPT request: {}", requestBody);

            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Create HTTP entity
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            // Parse and return the response
            if (response.getStatusCode() == HttpStatus.OK) {
                LOGGER.info("GPT response: {}", response.getBody());

                JsonNode responseBody = new ObjectMapper().readTree(response.getBody());
                JsonNode choicesNode = responseBody.get("choices");

                if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                    String content = choicesNode.get(0).get("message").get("content").asText().trim().replace("```json\n","").replace("```","");

                    try {
                        JSONObject responseJson = new JSONObject(content);
                        comprehensiveEventDTO.setSubject(responseJson.optString("subject"));
                        comprehensiveEventDTO.setBody(responseJson.optString("body"));
                        comprehensiveEventDTO.setMessageOnPhone(responseJson.optString("phoneMessage"));
                        comprehensiveEventDTO.setStatus("Generated");
                    } catch (JSONException e) {
                        LOGGER.error("Error parsing GPT response JSON: {}", e.getMessage());
                    }
                } else {
                    LOGGER.error("No choices found in GPT response");
                }
            } else {
                LOGGER.error("Failed to generate message. Response status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error generating message with GPT: {}", e.getMessage(), e);
        }

        return comprehensiveEventDTO;
    }

    public ComprehensiveEventDTO generateEmailDetails(ComprehensiveEventDTO comprehensiveEventDTO) {
        try {
            // Construct the GPT prompt
            StringBuilder prompt = new StringBuilder();
            prompt.append("Generate a JSON object with the following details.The total length of JSON should not exceed 200 words: ")
                    .append("subject, to, body, phoneNumber, extension, and phoneMessage (if phone number is provided). ")
                    .append("We are writing a warm and personalized email to ")
                    .append(comprehensiveEventDTO.getRelation()).append(" named ").append(comprehensiveEventDTO.getRelationFirstName()).append(" ")
                    .append(comprehensiveEventDTO.getRelationLastName())
                    .append(", wishing them for their ")
                    .append(comprehensiveEventDTO.getEventName()).append(". Please make the message heartfelt, personal, and appropriate for the occasion .Include a warm greeting, a sincere wish, and a personal touch based on the relationship. If it's a recurring event, mention how special it is to celebrate it again. ").append(comprehensiveEventDTO.getEventDateUtc())
                    .append(". ")
                    .append("Notes: ").append(comprehensiveEventDTO.getNotes()).append(". ")
                    .append("sender: ").append(comprehensiveEventDTO.getUserFirstName()).append(" ").append(comprehensiveEventDTO.getUserLastName()).append(". ");

            // Include phone number details if available
            if (comprehensiveEventDTO.getRelationPhone() != null) {
                prompt.append("Also, generate a brief, friendly phone message to be sent on phone as phoneMessage  ");
            }

            // Log the prompt for debugging purposes
            LOGGER.info("Sending GPT prompt: {}", prompt);

            // Generate the message using the GPT service
            JSONObject gptPrompt = new JSONObject();
            gptPrompt.put("messages", new org.json.JSONArray()
                    .put(new JSONObject()
                            .put("role", "system")
                            .put("content", "You are a helpful assistant skilled in writing warm, personalized messages for various occasions."))
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt.toString()))
            );

            generateMessage(gptPrompt, comprehensiveEventDTO);
            return comprehensiveEventDTO;
        } catch (Exception e) {
            LOGGER.error("Error generating email details for {}: {}", comprehensiveEventDTO.getRelationFirstName(), e.getMessage());
            return null;
        }
    }

}
