package com.wishstream.core.controller;


import com.wishstream.core.dto.UserRequest;
import com.wishstream.core.exception.ValidationException;
import com.wishstream.core.service.UserService;
import com.wishstream.core.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @GetMapping(value = "/")
    public String getAllRelations(){
       return "Hello World";
    }

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/save",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveUser( @RequestBody  UserRequest userRequest) throws ValidationException {
            Map<String, Object> result = userService.saveOrUpdate(userRequest);
            return ResponseHandler.success((String) result.get("message"),result);

    }
//    @Autowired
//    private ExcelProcessingService excelProcessingService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadExcelFiles(@RequestParam("files") List<MultipartFile> files) {
//        if (files.isEmpty()) {
//            return ResponseEntity.badRequest().body("No files uploaded");
//        }
//
//        return excelProcessingService.processExcelFiles(files);
//    }
}
