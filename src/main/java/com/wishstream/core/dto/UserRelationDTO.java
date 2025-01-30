package com.wishstream.core.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class UserRelationDTO {

    private String uid;
    private String firstName;
    private String lastName;
    private String relation;
    private String relationType;
    private String email;
    private String phone;
    private String phoneExtension;
    private String wsTxnId;
    private String userRelationId;
    private List<EventDTO> events;
    private LocationDTO location;
    private String timezone;

    // Getters and Setters

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getWsTxnId() {
        return wsTxnId;
    }

    public void setWsTxnId(String wsTxnId) {
        this.wsTxnId = wsTxnId;
    }

    public String getUserRelationId() {
        return userRelationId;
    }

    public void setUserRelationId(String userRelationId) {
        this.userRelationId = userRelationId;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Document(collection = "messages")
    public static class MessageDocument {

        @Id
        private String id; // Unique identifier for the message document
        private String date; // The date for which the messages are intended (ISO-8601 format, e.g., "2025-01-27")

        private List<PhoneMessage> phone; // List of phone messages
        private List<EmailMessage> email; // List of email messages

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<PhoneMessage> getPhone() {
            return phone;
        }

        public void setPhone(List<PhoneMessage> phone) {
            this.phone = phone;
        }

        public List<EmailMessage> getEmail() {
            return email;
        }

        public void setEmail(List<EmailMessage> email) {
            this.email = email;
        }

        // Inner Classes for PhoneMessage and EmailMessage

        public static class PhoneMessage {
            private String message;       // The message content for the phone notification
            private String phoneNumber;   // Phone number to which the message should be sent
            private String extension;     // Country code or phone extension

            // Getters and Setters
            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }
        }

        public static class EmailMessage {
            private String subject;   // Subject of the email
            private String to;        // Recipient's email address
            private String body;      // Body content of the email

            // Getters and Setters
            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }
        }
    }
}
