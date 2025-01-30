import java.time.LocalDateTime;

public class ComprehensiveEventDTO {
    // User fields
    private String userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPhone;
    private String userPhoneExtension;

    // UserRelation fields
    private String userRelationId;
    private String relationFirstName;
    private String relationLastName;
    private String relation;
    private String relationType;
    private String relationEmail;
    private String relationPhone;
    private String relationPhoneExtension;
    private String timezone;

    // Event fields
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDateUtc;
    private String notes;
    private String status;

    // Constructor, getters, and setters
    // ...
}
