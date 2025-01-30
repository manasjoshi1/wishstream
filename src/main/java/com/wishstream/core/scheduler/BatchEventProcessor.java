package com.wishstream.core.scheduler;

import com.wishstream.core.model.MessageDocument;
import com.wishstream.core.model.UserRelation;
import com.wishstream.core.model.Event;
import com.wishstream.core.repository.UserRelationRepo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
@Slf4j
public class BatchEventProcessor {

    @Autowired
    private UserRelationRepo userRelationRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void processUpcomingEvents() {

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);

        // Query user relations that have events between [start, end]
        List<UserRelation> userRelations = userRelationRepository.findEventsBetweenDates(start, end);

        for (UserRelation userRelation : userRelations) {
            for (Event event : userRelation.getEvents()) {
                LocalDate eventDate = LocalDate.parse(event.getDate());

                // Only process events within the specified date range
                if (!eventDate.isBefore(start) && !eventDate.isAfter(end)) {
                    String gptResponse = generateEmailDetails(userRelation, event);
                    if (gptResponse != null) {
                        buildMessageDocument(eventDate, userRelation, gptResponse);
                    }
                }
            }
        }

    }

    private String generateEmailDetails(UserRelation userRelation, Event event) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Generate a JSON object with the following details: ")
                    .append("subject, to, and body, phoneNumber, extension and phoneMessage if phone number sent. ");
            prompt.append("We are writing an email to ")
                    .append(userRelation.getRelation()).append(" named ").append(userRelation.getFirstName()).append(" ")
                    .append(userRelation.getLastName())
                    .append(", wishing them for their ")
                    .append(event.getEvent()).append(" on ").append(event.getDate())
                    .append(". Notes: ").append(event.getNotes())
                    .append(". Recurrence: ").append(event.getRecurrence());
                    if(userRelation.getPhone() != null && userRelation.getPhoneExtension() != null) {
                        prompt.append("Phone number: ").append(userRelation.getPhone())
                                .append("Extension: ").append(userRelation.getPhoneExtension());
                    }
            log.info("Sending GPT prompt: {}", prompt);
            return null;

//            return gptService.generateMessage(prompt.toString());
        } catch (Exception e) {
            log.error("Error generating email details for {}: {}", userRelation.getFirstName(), e.getMessage());
            return null;
        }
    }

    private void buildMessageDocument(LocalDate eventDate, UserRelation userRelation, String gptResponse) {
        try {

            JSONObject jsonObject = new JSONObject(gptResponse);


            MessageDocument messageDocument = new MessageDocument();
            messageDocument.setDate(eventDate.toString());

            // Parse GPT response for email details
            MessageDocument.EmailMessage emailMessage = new MessageDocument.EmailMessage();
            emailMessage.setTo(jsonObject.getString("to"));
            emailMessage.setSubject(jsonObject.getString("subject"));
            emailMessage.setBody(jsonObject.getString("body"));

            MessageDocument.PhoneMessage phoneMessage = new MessageDocument.PhoneMessage();
            phoneMessage.setPhoneNumber(jsonObject.getString("phoneNumber"));
            phoneMessage.setExtension(jsonObject.getString("extension"));
            phoneMessage.setMessage(jsonObject.getString("phoneMessage"));

            // Add the email message
            List<MessageDocument.EmailMessage> emailMessages = new ArrayList<>();
            List<MessageDocument.PhoneMessage> phoneMessages = new ArrayList<>();
            phoneMessages.add(phoneMessage);
            messageDocument.setPhone(phoneMessages);
            emailMessages.add(emailMessage);
            messageDocument.setEmail(emailMessages);

            log.info("Generated message for user: {}",userRelation.getFirstName());
//            messageRepository.save(messageDoc);
        } catch (Exception e) {
            log.error("Error building message document for {}: {}", userRelation.getFirstName(), e.getMessage());
//            return null;
        }
    }


}
