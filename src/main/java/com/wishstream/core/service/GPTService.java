package com.wishstream.core.service;

import com.wishstream.core.model.Event;
import com.wishstream.core.model.UserRelation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String temperature;
    @Value("${gpt.api.max_tokens}") // OpenAI API Key
    private String tokens;

    /**
     * Generates a message using GPT based on the provided prompt.
     *
     * @param prompt The input prompt for GPT to generate the message.
     * @return The generated message as a string.
     */
    public static String generateMessage(JSONObject prompt) {
        try {
            LOGGER.info("Generating message with GPT...");
            // Create the request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4"); // Choose the GPT model
            requestBody.put("messages", prompt.getJSONArray("messages").toList());
            requestBody.put("max_tokens", 100); // Adjust token limit based on your needs
            requestBody.put("temperature", 0.5); // Adjust creativity level
            LOGGER.info("GPT request: {}", requestBody);
            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth("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");

            // Create HTTP entity
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, requestEntity, String.class);

            // Parse and return the response
            if (response.getStatusCode() == HttpStatus.OK) {
                LOGGER.info("GPT response: {}", response.getBody());

                JsonNode responseBody = new ObjectMapper().readTree(response.getBody());
                return responseBody.get("choices").get(0).get("text").asText().trim();
            } else {
                LOGGER.error("Failed to generate message. Response status: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error generating message with GPT: {}", e.getMessage(), e);
            return null;
        }
    }
    public static String generateEmailDetails(UserRelation userRelation, Event event) {
        try {
            // Construct the GPT prompt
            StringBuilder prompt = new StringBuilder();
            prompt.append("Generate a JSON object with the following details: ")
                    .append("subject, to, body, phoneNumber, extension, and phoneMessage (if phone number is provided). ")
                    .append("We are writing an email to ")
                    .append("frient").append(" named ").append("urvi").append(" ")
                    .append("joshi")
                    .append(", wishing them for their ")
                    .append("birthday").append(" on ").append("today")
                    .append(". ")
                    .append("Notes: ").append("loves flower").append(". ")
                    .append("Recurrence: ").append("yearly").append(". ").append("sender: Manas");

            // Include phone number details if available
            if (userRelation.getPhone() != null) {
                prompt.append("Phone number: ").append(userRelation.getPhone()).append(". ");
                if (userRelation.getPhoneExtension() != null) {
                    prompt.append("Extension: ").append(userRelation.getPhoneExtension()).append(". ");
                }
            }

            // Log the prompt for debugging purposes
            LOGGER.info("Sending GPT prompt: {}", prompt);

            // Generate the message using the GPT service
            JSONObject gptPrompt = new JSONObject();
            gptPrompt.put("messages", new org.json.JSONArray()
                    .put(new JSONObject()
                            .put("role", "system")
                            .put("content", "You are a helpful assistant. Generate the required email details in JSON format."))
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt.toString()))
            );

            return generateMessage(gptPrompt);
        } catch (Exception e) {
            LOGGER.error("Error generating email details for {}: {}", userRelation.getFirstName(), e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // Create a scanner for user input

        // Collect UserRelation details
        System.out.println("Enter the recipient's relation (e.g., friend, colleague, sibling):");
        String relation = "Friend";

        System.out.println("Enter the recipient's first name:");
            String firstName = "Janey";

        System.out.println("Enter the recipient's last name:");
        String lastName = "Jane";

        System.out.println("Enter the recipient's phone number (or leave blank if not available):");
        String phoneExtension = "1";

        System.out.println("Enter the phone extension (or leave blank if not available):");
        String phone = "98904459123";

        // Collect Event details
        System.out.println("Enter the event (e.g., birthday, anniversary):");
        String event = "Birthday";

        System.out.println("Enter the event date (e.g., 2025-01-25):");
        String eventDate = "2025-01-24";

        System.out.println("Enter any notes for the event:");
        String notes = "She like flowers and dislikes chocolate";

        System.out.println("Enter the recurrence (e.g., Once, Annual, Monthly):");
        String recurrence = "Yearly";

        // Initialize UserRelation and Event objects
        UserRelation userRelation = new UserRelation(
                relation,
                firstName,
                lastName,
                phone.isEmpty() ? null : phone,
                phoneExtension.isEmpty() ? null : phoneExtension
        );

        Event eventDetails = new Event(
                event,
                eventDate,
                notes,
                recurrence
        );

        // Create an instance of GPTService and call the generateEmailDetails method
        GPTService gptService = new GPTService();
        String response = generateEmailDetails(userRelation, eventDetails);

        // Print the generated response
        System.out.println("Generated Email Details JSON:");
        System.out.println(response);

    }
}
