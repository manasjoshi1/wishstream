package com.wishstream.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserRelationRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Relation is required")
    private String relation;

    @NotBlank(message = "Relation type is required")
    private String relationType;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String phoneExtension;

    @NotBlank(message = "Timezone is required")
    private String timezone;

    @NotNull(message = "Events cannot be null")
    private List<@Valid EventRequest> events;

    // Getters and Setters

    public @NotBlank(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Relation is required") String getRelation() {
        return relation;
    }

    public void setRelation(@NotBlank(message = "Relation is required") String relation) {
        this.relation = relation;
    }

    public @NotBlank(message = "Relation type is required") String getRelationType() {
        return relationType;
    }

    public void setRelationType(@NotBlank(message = "Relation type is required") String relationType) {
        this.relationType = relationType;
    }

    public @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email format") String email) {
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

    public @NotBlank(message = "Timezone is required") String getTimezone() {
        return timezone;
    }

    public void setTimezone(@NotBlank(message = "Timezone is required") String timezone) {
        this.timezone = timezone;
    }

    public @NotNull(message = "Events cannot be null") List<@Valid EventRequest> getEvents() {
        return events;
    }

    public void setEvents(@NotNull(message = "Events cannot be null") List<@Valid EventRequest> events) {
        this.events = events;
    }
}
