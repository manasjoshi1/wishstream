package com.wishstream.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "user_relations") // MongoDB collection name
public class UserRelation {

    @Id
    private String uid; // Unique ID for the user

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("relation")
    private String relation;

    @Field("relation_type")
    private String relationType;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("phone_extension")
    private String phoneExtension;

    @Field("user_relation_id")
    private String userRelationId;

    @Field("events")
    private List<Event> events;

    private String timezone;

    public UserRelation(String relation, String firstName, String lastName, String s, String s1) {
    }
    public UserRelation(String firstName, String lastName, String relation, String relationType, String email, String phone, String phoneExtension, String timezone, List<Event> events) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.relation = relation;
        this.relationType = relationType;
        this.email = email;
        this.phone = phone;
        this.phoneExtension = phoneExtension;
        this.timezone = timezone;
        this.events = events;
    }
    public UserRelation() {
    }

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



    public String getUserRelationId() {
        return userRelationId;
    }

    public void setUserRelationId(String userRelationId) {
        this.userRelationId = userRelationId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }



    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
