package com.wishstream.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String phoneExtension;

    @NotNull(message = "User relations cannot be null")
    private List<@Valid UserRelationRequest> userRelations;

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

    public @NotNull(message = "User relations cannot be null") List<@Valid UserRelationRequest> getUserRelations() {
        return userRelations;
    }

    public void setUserRelations(@NotNull(message = "User relations cannot be null") List<@Valid UserRelationRequest> userRelations) {
        this.userRelations = userRelations;
    }

    // Getters and Setters
}
