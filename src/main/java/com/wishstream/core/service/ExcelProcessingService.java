//package com.wishstream.core.service;
//
//import com.wishstream.core.dto.*;
//import com.wishstream.core.model.*;
//import com.wishstream.core.repository.*;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//import java.util.UUID;
//
//@Service
//public class ExcelProcessingService {
//
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private EventRepo eventRepo;
//    @Autowired
//    private UserUploadLogRepo userUploadLogRepo;
//
//    private static final int BATCH_SIZE = 10000;
//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//    public ResponseEntity<?> processExcelFiles(List<MultipartFile> files) {
//        for (MultipartFile file : files) {
//            String uploadId = UUID.randomUUID().toString();
//            String originalFileName = file.getOriginalFilename();
//
//            UserUploadLog uploadLog = new UserUploadLog(uploadId, originalFileName, "PROCESSING", new Date());
//            userUploadLogRepo.save(uploadLog);
//
//            try (InputStream inputStream = file.getInputStream();
//                 Workbook workbook = new XSSFWorkbook(inputStream)) {
//
//                List<Map<String, Object>> invalidRecords = processWorkbook(workbook);
//
//                if (!invalidRecords.isEmpty()) {
//                    String errorFilePath = saveErrorFile(workbook, originalFileName);
//                    uploadLog.setErrorFilePath(errorFilePath);
//                    uploadLog.setStatus("FAILED");
//                } else {
//                    uploadLog.setStatus("COMPLETED");
//                }
//
//                userUploadLogRepo.save(uploadLog);
//            } catch (Exception e) {
//                uploadLog.setStatus("FAILED");
//                userUploadLogRepo.save(uploadLog);
//                return ResponseEntity.badRequest().body("Error processing file: " + originalFileName);
//            }
//        }
//        return ResponseEntity.ok("All records processed successfully.");
//    }
//
//    private List<Map<String, Object>> processWorkbook(Workbook workbook) {
//        List<UserRequest> userRequests = new ArrayList<>();
//        List<Map<String, Object>> invalidRecords = new ArrayList<>();
//        processSheet(workbook.getSheet("Users"), userRequests, invalidRecords, UserRequest.class);
//        processSheet(workbook.getSheet("User Relations"), relationRequests, invalidRecords, UserRelationRequest.class);
//        processSheet(workbook.getSheet("Events"), eventRequests, invalidRecords, EventRequest.class);
//        return invalidRecords;
//    }
//
//    private void processSheet(Sheet sheet, List<Map<String, Object>> invalidRecords) {
//        if (sheet == null) return;
//        for (Row row : sheet) {
//            if (rowCount++ == 0) continue;
//            try {
//                T entity = mapRowToEntity(row, type);
//                validList.add(entity);
//            } catch (Exception e) {
//                invalidRecords.add(mapInvalidRow(row, sheet));
//            }
//        }
//        for (Row row : sheet) {
//            try {
//                validateRow(row);
//            } catch (Exception e) {
//                addErrorComment(sheet, row.getRowNum(), 0, e.getMessage());
//                invalidRecords.add(Collections.singletonMap("row", row.getRowNum()));
//            }
//        }
//    }
//
//    private void validateRow(Row row) {
//        Cell cell = row.getCell(0);
//        if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
//            throw new IllegalArgumentException("Missing required field");
//        }
//    }
//
//    private void addErrorComment(Sheet sheet, int rowIdx, int colIdx, String errorMsg) {
//        Row row = sheet.getRow(rowIdx);
//        Cell cell = row.getCell(colIdx, Row.MissingCellPolicy);
//        Drawing<?> drawing = sheet.createDrawingPatriarch();
//        CreationHelper factory = sheet.getWorkbook().getCreationHelper();
//        ClientAnchor anchor = factory.createClientAnchor();
//        Comment comment = drawing.createCellComment(anchor);
//        comment.setString(factory.createRichTextString(errorMsg));
//        cell.setCellComment(comment);
//    }
//
//    private String saveErrorFile(Workbook workbook, String originalFileName) {
//        String errorFileName = "errors_" + originalFileName;
//        String filePath = "/local/path/errors/" + errorFileName;
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            workbook.write(fos);
//            return filePath;
//        } catch (IOException e) {
//            return null;
//        }
//    }
//}
