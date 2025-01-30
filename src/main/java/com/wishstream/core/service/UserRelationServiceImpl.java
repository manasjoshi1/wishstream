package com.wishstream.core.service;

import com.wishstream.core.dto.ComprehensiveEventDTO;
import com.wishstream.core.dto.EventRequest;
import com.wishstream.core.dto.UserRelationRequest;
import com.wishstream.core.dto.UserRequest;
import com.wishstream.core.model.Event;
import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import com.wishstream.core.repository.EventRepo;
import com.wishstream.core.repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRelationServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserRelationServiceImpl.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EventRepo eventRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    @Transactional
    public Map<String, Object> saveOrUpdate(UserRequest userRequest) {
        logger.debug("Starting saveOrUpdate for user: {}", userRequest);

        List<Map<String, String>> errors = validateUserRequest(userRequest);
        if (!errors.isEmpty()) {
            return createErrorResponseInvalidUser(errors, 0, 0);
        }

        User user = findOrCreateUser(userRequest);
        List<UserRelation> validRelations = new ArrayList<>();
        int validEventsCount = processUserRelations(userRequest, user, validRelations, errors);

        user.setUserRelations(validRelations);
        userRepo.save(user);

        return generateFinalResponse(errors, validRelations.size(), validEventsCount);
    }

    private List<Map<String, String>> validateUserRequest(UserRequest userRequest) {
        List<Map<String, String>> errors = new ArrayList<>();

        if (userRequest == null) {
            errors.add(Map.of("field", "userRequest", "value", "null", "message", "User request cannot be null"));
            return errors;
        }
        validateField("email", userRequest.getEmail(), errors);
        validateField("phone", userRequest.getPhone(), errors);
        return errors;
    }

    private void validateField(String fieldName, String value, List<Map<String, String>> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(Map.of("field", fieldName, "value", "null", "message", fieldName + " is required"));
        }
    }

    private User findOrCreateUser(UserRequest userRequest) {
        return userRepo.findByEmailOrPhone(userRequest.getEmail(), userRequest.getPhone())
                .orElseGet(() -> createUser(userRequest));
    }

    private User createUser(UserRequest userRequest) {
        User user = new User();
        updateUser(user, userRequest);
        return user;
    }

    private int processUserRelations(UserRequest userRequest, User user, List<UserRelation> validRelations, List<Map<String, String>> errors) {
        int validEventsCount = 0;

        if (userRequest.getUserRelations() != null) {
            for (UserRelationRequest relation : userRequest.getUserRelations()) {
                if (relation == null) continue;

                Set<ConstraintViolation<UserRelationRequest>> relationViolations = validator.validate(relation);
                if (!relationViolations.isEmpty()) {
                    errors.addAll(extractValidationErrors(relationViolations));
                    continue;
                }

                UserRelation userRelation = findOrCreateUserRelation(user, relation);
                updateUserRelation(userRelation, relation);

                List<Event> validEvents = processEvents(relation, userRelation, errors,user);
                eventRepository.saveAll(validEvents);

                userRelation.setEvents(validEvents);
                validRelations.add(userRelation);
                validEventsCount += validEvents.size();
            }
        }
        return validEventsCount;
    }

    private UserRelation findOrCreateUserRelation(User user, UserRelationRequest relationRequest) {
        return findUserRelation(user, relationRequest.getEmail(), relationRequest.getPhone())
                .orElseGet(() -> new UserRelation());
    }

    private List<Event> processEvents(UserRelationRequest relation, UserRelation userRelation, List<Map<String, String>> errors,User user) {
        List<Event> validEvents = new ArrayList<>();

        if (relation.getEvents() != null) {
            for (EventRequest event : relation.getEvents()) {
                Set<ConstraintViolation<EventRequest>> eventViolations = validator.validate(event);
                if (!eventViolations.isEmpty()) {
                    errors.addAll(extractValidationErrors(eventViolations));
                    continue;
                }

                Event eventEntity = findOrCreateEvent(event, userRelation.getUserRelationId());
                updateEvent(eventEntity, event, userRelation.getUserRelationId(), relation.getTimezone());
                validEvents.add(eventEntity);
                saveComprehensiveDTO(createComprehensiveDTO(user, userRelation, eventEntity));
            }
        }
        return validEvents;
    }

    private Event findOrCreateEvent(EventRequest eventRequest, String relationId) {
        return eventRepository.findAllByEventAndUserRelationId(eventRequest.getEvent(), relationId)
                .orElseGet(Event::new);
    }

    private void updateUser(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPhoneExtension(userRequest.getPhoneExtension());
    }

    private void updateUserRelation(UserRelation relation, UserRelationRequest relationRequest) {
        if (relation.getUserRelationId() == null) {
            relation.setUserRelationId(UUID.randomUUID().toString());
        }
        relation.setFirstName(relationRequest.getFirstName());
        relation.setLastName(relationRequest.getLastName());
        relation.setRelation(relationRequest.getRelation());
        relation.setRelationType(relationRequest.getRelationType());
        relation.setEmail(relationRequest.getEmail());
        relation.setPhone(relationRequest.getPhone());
        relation.setPhoneExtension(relationRequest.getPhoneExtension());
        relation.setTimezone(relationRequest.getTimezone());
    }

    private void updateEvent(Event event, EventRequest eventRequest, String relationId, String timezone) {
        event.setEvent(eventRequest.getEvent());
        event.setUserRelationId(relationId);
        event.setNotes(eventRequest.getNotes());
        event.setRecurrence(eventRequest.getRecurrence());
        event.setType(eventRequest.getType());
        event.setEventDateInUtc(eventRequest.getDate().atStartOfDay(), timezone);
    }

    private Optional<UserRelation> findUserRelation(User user, String email, String phone) {
        return user.getUserRelations() != null ? user.getUserRelations().stream()
                .filter(r -> (email != null && email.equals(r.getEmail())) ||
                        (phone != null && phone.equals(r.getPhone())))
                .findFirst() : Optional.empty();
    }

    private <T> List<Map<String, String>> extractValidationErrors(Set<ConstraintViolation<T>> violations) {
        return violations.stream().map(violation -> Map.of(
                "field", violation.getPropertyPath().toString(),
                "value", violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : "null",
                "message", violation.getMessage()
        )).collect(Collectors.toList());
    }

    private Map<String, Object> generateFinalResponse(List<Map<String, String>> errors, int relationsSaved, int eventsSaved) {
        return errors.isEmpty()
                ? Map.of("message", "User and relations saved successfully", "relations-saved", relationsSaved, "events-saved", eventsSaved)
                : createErrorResponse(errors, relationsSaved, eventsSaved);
    }

    private Map<String, Object> createErrorResponse(List<Map<String, String>> errors, int relationsSaved, int eventsSaved) {
        logger.error("Validation errors: {}", errors);
        return Map.of("message", "Partial data saved", "errors", errors, "relations-saved", relationsSaved, "events-saved", eventsSaved);
    }

    private Map<String, Object> createErrorResponseInvalidUser(List<Map<String, String>> errors, int relationsSaved, int eventsSaved) {
        logger.error("Validation errors: {}", errors);
        return Map.of("message", "Invalid User", "errors", errors, "relations-saved", relationsSaved, "events-saved", eventsSaved);
    }
    private ComprehensiveEventDTO createComprehensiveDTO(User user, UserRelation userRelation, Event event) {
        ComprehensiveEventDTO dto = new ComprehensiveEventDTO();

        // Set User fields
        dto.setUserId(user.getId());
        dto.setUserEmail(user.getEmail());
        dto.setUserFirstName(user.getFirstName());
        dto.setUserLastName(user.getLastName());
        dto.setUserPhone(user.getPhone());
        dto.setUserPhoneExtension(user.getPhoneExtension());

        // Set UserRelation fields
        dto.setUserRelationId(userRelation.getUserRelationId());
        dto.setRelationFirstName(userRelation.getFirstName());
        dto.setRelationLastName(userRelation.getLastName());
        dto.setRelation(userRelation.getRelation());
        dto.setRelationType(userRelation.getRelationType());
        dto.setRelationEmail(userRelation.getEmail());
        dto.setRelationPhone(userRelation.getPhone());
        dto.setRelationPhoneExtension(userRelation.getPhoneExtension());
        dto.setTimezone(userRelation.getTimezone());

        // Set Event fields
        dto.setEventId(event.getId());
        dto.setEventName(event.getEvent());
        dto.setEventDateUtc(event.getEventDateUtc());
        dto.setNotes(event.getNotes());

        return dto;
    }
    private void saveComprehensiveDTO(ComprehensiveEventDTO dto) {
        String tableName = "events_master_" + dto.getEventDateUtc().format(DateTimeFormatter.ofPattern("MM_dd"));
        if (!tableExists(tableName)) {
            createTable(tableName);
        }
        String insertQuery = "INSERT INTO  " + tableName+
                " (user_id, user_email, user_first_name, user_last_name, user_phone, user_phone_extension, " +
                "user_relation_id, relation_first_name, relation_last_name, relation, relation_type, " +
                "relation_email, relation_phone, relation_phone_extension, timezone, " +
                "event_id, event_name, event_date_utc, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Query query = entityManager.createNativeQuery(insertQuery);
        query.setParameter(1, dto.getUserId());
        query.setParameter(2, dto.getUserEmail());
        query.setParameter(3, dto.getUserFirstName());
        query.setParameter(4, dto.getUserLastName());
        query.setParameter(5, dto.getUserPhone());
        query.setParameter(6, dto.getUserPhoneExtension());
        query.setParameter(7, dto.getUserRelationId());
        query.setParameter(8, dto.getRelationFirstName());
        query.setParameter(9, dto.getRelationLastName());
        query.setParameter(10, dto.getRelation());
        query.setParameter(11, dto.getRelationType());
        query.setParameter(12, dto.getRelationEmail());
        query.setParameter(13, dto.getRelationPhone());
        query.setParameter(14, dto.getRelationPhoneExtension());
        query.setParameter(15, dto.getTimezone());
        query.setParameter(16, dto.getEventId());
        query.setParameter(17, dto.getEventName());
        query.setParameter(18, dto.getEventDateUtc());
        query.setParameter(19, dto.getNotes());

        query.executeUpdate();
    }
    private boolean tableExists(String tableName) {
        try {
            entityManager.createNativeQuery("SELECT 1 FROM " + tableName + " WHERE 1=0").getResultList();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private void createTable(String tableName) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id VARCHAR(255), " +
                "user_email VARCHAR(255), " +
                "user_first_name VARCHAR(255), " +
                "user_last_name VARCHAR(255), " +
                "user_phone VARCHAR(20), " +
                "user_phone_extension VARCHAR(10), " +
                "user_relation_id VARCHAR(255), " +
                "relation_first_name VARCHAR(255), " +
                "relation_last_name VARCHAR(255), " +
                "relation VARCHAR(255), " +
                "relation_type VARCHAR(255), " +
                "relation_email VARCHAR(255), " +
                "relation_phone VARCHAR(20), " +
                "relation_phone_extension VARCHAR(10), " +
                "timezone VARCHAR(50), " +
                "event_id BIGINT, " +
                "event_name VARCHAR(255), " +
                "event_date_utc DATETIME, " +
                "notes TEXT, " +
                "status VARCHAR(50), " +
                "message_on_phone MEDIUMTEXT, " +
                "subject TEXT, " +
                "body MEDIUMTEXT)";

        entityManager.createNativeQuery(createTableQuery).executeUpdate();
    }

    @Transactional
    public int cloneTable(String sourceTableName, String destinationTableName) {
        logger.info("Cloning from " + sourceTableName + " to " + destinationTableName);

        // Step 1: Create the destination table with the same structure as the source table
        String createTableQuery = String.format(
                "CREATE TABLE %s AS SELECT * FROM %s WHERE 1=0",
                destinationTableName, sourceTableName
        );
        entityManager.createNativeQuery(createTableQuery).executeUpdate();

        // Step 2: Copy data from the source table to the destination table
        String insertDataQuery = String.format(
                "INSERT INTO %s SELECT * FROM %s",
                destinationTableName, sourceTableName
        );
        Query query = entityManager.createNativeQuery(insertDataQuery);
        int rowsAffected = query.executeUpdate();

        logger.info("Cloned " + rowsAffected + " rows from " + sourceTableName + " to " + destinationTableName);
        return rowsAffected;
    }

        @PersistenceContext
        private EntityManager entityManager;

        public List<ComprehensiveEventDTO> fetchComprehensiveEvents(String tableName) {
            String query = "SELECT * FROM " + tableName;
            List<Object[]> results = entityManager.createNativeQuery(query).getResultList();

            return results.stream()
                    .map(this::mapToComprehensiveEventDTO)
                    .collect(Collectors.toList());
        }

        private ComprehensiveEventDTO mapToComprehensiveEventDTO(Object[] row) {
            ComprehensiveEventDTO dto = new ComprehensiveEventDTO();

            dto.setUserId((String) row[1]);
            dto.setUserEmail((String) row[2]);
            dto.setUserFirstName((String) row[3]);
            dto.setUserLastName((String) row[4]);
            dto.setUserPhone((String) row[5]);
            dto.setUserPhoneExtension((String) row[6]);
            dto.setUserRelationId((String) row[7]);
            dto.setRelationFirstName((String) row[8]);
            dto.setRelationLastName((String) row[9]);
            dto.setRelation((String) row[10]);
            dto.setRelationType((String) row[11]);
            dto.setRelationEmail((String) row[12]);
            dto.setRelationPhone((String) row[13]);
            dto.setRelationPhoneExtension((String) row[14]);
            dto.setTimezone((String) row[15]);
//            dto.setEventId(Long.parseLong((String) row[i++]));
            dto.setEventName((String) row[17]);
//            dto.setEventDateUtc((LocalDateTime) row[i++]);
            dto.setNotes((String) row[19]);
//            dto.setStatus((String) row[19]);
            return dto;
        }

        public ComprehensiveEventDTO fetchComprehensiveEventById(Long eventId) {
            String query = "SELECT * FROM comprehensive_events WHERE event_id = :eventId";
            Object[] result = (Object[]) entityManager.createNativeQuery(query)
                    .setParameter("eventId", eventId)
                    .getSingleResult();
            return mapToComprehensiveEventDTO(result);
        }

        @Transactional
        public int updateComprehensiveEvent(ComprehensiveEventDTO dto,String tableName) {
            String updateQuery = "UPDATE "+tableName+" SET " +
                    "user_email = :userEmail, user_first_name = :userFirstName, user_last_name = :userLastName, " +
                    "user_phone = :userPhone, user_phone_extension = :userPhoneExtension, " +
                    "relation_first_name = :relationFirstName, relation_last_name = :relationLastName, " +
                    "relation = :relation, relation_type = :relationType, relation_email = :relationEmail, " +
                    "relation_phone = :relationPhone, relation_phone_extension = :relationPhoneExtension, " +
                    "timezone = :timezone, event_name = :eventName,  " +
                    "message_on_phone = :messageOnPhone, subject = :subject, body = :body, " +
                    "notes = :notes, status = :status " +
                    "WHERE user_id = :userId AND user_relation_id = :userRelationId";

            Query query = entityManager.createNativeQuery(updateQuery);
            query.setParameter("userEmail", dto.getUserEmail());
            query.setParameter("userFirstName", dto.getUserFirstName());
            query.setParameter("userLastName", dto.getUserLastName());
            query.setParameter("userPhone", dto.getUserPhone());
            query.setParameter("userPhoneExtension", dto.getUserPhoneExtension());
            query.setParameter("relationFirstName", dto.getRelationFirstName());
            query.setParameter("relationLastName", dto.getRelationLastName());
            query.setParameter("relation", dto.getRelation());
            query.setParameter("relationType", dto.getRelationType());
            query.setParameter("relationEmail", dto.getRelationEmail());
            query.setParameter("relationPhone", dto.getRelationPhone());
            query.setParameter("relationPhoneExtension", dto.getRelationPhoneExtension());
            query.setParameter("timezone", dto.getTimezone());
            query.setParameter("eventName", dto.getEventName());
            query.setParameter("notes", dto.getNotes());
            query.setParameter("status", dto.getStatus());
            query.setParameter("userId", dto.getUserId());
            query.setParameter("userRelationId", dto.getUserRelationId());
//            query.setParameter("eventId", dto.getEventId());
            query.setParameter("messageOnPhone", dto.getMessageOnPhone());
            query.setParameter("subject", dto.getSubject());
            query.setParameter("body", dto.getBody());


            int rowsAffected = query.executeUpdate();
            if (rowsAffected == 0) {
                logger.error("No rows updated for event with ID: {}", dto.getEventId());
            }
            return rowsAffected;
        }
    }



