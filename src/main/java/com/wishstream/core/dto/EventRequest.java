package com.wishstream.core.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Event name is required")
    private String event;

    private String notes;

    @Pattern(regexp = "none|daily|weekly|monthly|yearly", message = "Recurrence must be one of: none, daily, weekly, monthly, yearly")
    private String recurrence;

    private String type; // new or update

    // Getters and Setters

    public @NotBlank(message = "Event name is required") String getEvent() {
        return event;
    }

    public void setEvent(@NotBlank(message = "Event name is required") String event) {
        this.event = event;
    }

    public @NotNull(message = "Date is required") LocalDate getDate() {
        return date;
    }

    public void setDate(@NotNull(message = "Date is required") LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public @Pattern(regexp = "none|daily|weekly|monthly|yearly", message = "Recurrence must be one of: none, daily, weekly, monthly, yearly") String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(@Pattern(regexp = "none|daily|weekly|monthly|yearly", message = "Recurrence must be one of: none, daily, weekly, monthly, yearly") String recurrence) {
        this.recurrence = recurrence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
