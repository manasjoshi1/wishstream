package com.wishstream.core.service;

import com.wishstream.core.model.Event;
import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import com.wishstream.core.repository.EventRepo;
import com.wishstream.core.repository.UserRelationRepo;
import com.wishstream.core.repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserRelationRepo relationRepo;
    @Autowired
    private UserRepo userRepo;
    @PersistenceContext
    private EntityManager entityManager;

//    public Optional<List<Event>> getEventsByDate(LocalDate date) {
//        return eventRepo.findAllBydate(date);
//    }

    @Transactional
    public void storeDynamicEvents(String tableName, List<Event> events) {
        for (Event event : events) {
            Optional<UserRelation> recipientOpt = relationRepo.findUserRelationByUserRelationId(event.getUserRelationId());
            User sender = userRepo.findByUserRelationId(event.getUserRelationId());

            if (recipientOpt.isEmpty() || sender == null) {
                log.info("Recipient or sender not found for event: {} {}", event.getUserRelationId(), event.getEvent());
                continue;
            }

            UserRelation recipient = recipientOpt.get();

            String insertQuery = "INSERT INTO " + tableName + " (event_name, event_date_utc, first_name, last_name, user_relation_id, email, phone, recipient_email, recipient_phone_extension, subject, body, recipient_phone_with_extension, message_on_phone, status, sender) " +
                    "VALUES (:eventName, :eventDateUtc, :firstName, :lastName, :userRelationId, :email, :phone, :recipientEmail, :recipientPhoneExtension, :subject, :body, :recipientPhoneWithExtension, :messageOnPhone, :status, :sender)";

            Query query = entityManager.createNativeQuery(insertQuery);
            query.setParameter("eventName", event.getEvent());
            query.setParameter("eventDateUtc", event.getEventDateUtc());
            query.setParameter("firstName", sender.getFirstName());
            query.setParameter("lastName", sender.getLastName());
            query.setParameter("userRelationId", event.getUserRelationId());
            query.setParameter("email", sender.getEmail());
            query.setParameter("phone", sender.getPhone());
            query.setParameter("recipientEmail", recipient.getEmail());
            query.setParameter("recipientPhoneExtension", recipient.getPhoneExtension());
            query.setParameter("subject", event.getNotes());
            query.setParameter("body", null);
            query.setParameter("recipientPhoneWithExtension", (recipient.getPhone() + " " + recipient.getPhoneExtension()).trim());
            query.setParameter("messageOnPhone", null);
            query.setParameter("status", "PENDING");
            query.setParameter("sender", sender.getFirstName() + " " + sender.getLastName());

            query.executeUpdate();
        }
    }
}
