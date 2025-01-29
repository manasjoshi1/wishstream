//package com.wishstream.core.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Date;
//import java.util.UUID;
//
//@Entity
//@Table(name = "user_upload_log")
//@Getter
//@Setter
//@Data
////@AllArgsConstructor
//@NoArgsConstructor
//public class UserUploadLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String fileName;
//    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
//    private Integer totalRecords;
//    private Integer validRecords;
//    private Integer invalidRecords;
//    private String UUID;
//    private String errorFilePath; // Local storage path
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date uploadedAt;
//
//    public UserUploadLog(String uploadId, String originalFileName, String processing, Date date) {
//        this.fileName = originalFileName;
//        this.status = processing;
//        this.uploadedAt = date;
//        this.UUID = uploadId;
//    }
//
//    // Getters and Setters
//}
