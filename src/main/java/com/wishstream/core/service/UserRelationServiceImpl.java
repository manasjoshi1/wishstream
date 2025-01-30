package com.wishstream.core.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishstream.core.dto.UserRelationDTO;
import com.wishstream.core.model.UserRelation;
import com.wishstream.core.repository.UserRelationRepo;
import com.wishstream.core.utils.JSONValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRelationServiceImpl implements UserRelationService {

    @Autowired
    private UserRelationRepo userRelationRepository;
    private static final String SCHEMA_PATH = "/user-relation-schema.json";


    @Override
    public Map<String, Object> processUserRelations(List<String> userRelationList) {
        Map<String, Object> response = new HashMap<>();
        List<String> invalidRecords = new ArrayList<>();
        List<UserRelation> validRecords = new ArrayList<>();
        try {
            invalidRecords.clear(); // Clear previous invalid records.

            for (String dto : userRelationList) {
                JSONValidator.ValidationResult result = JSONValidator.validateJson(dto, SCHEMA_PATH);
                if (result.isValid()) {
                    UserRelation userRelation = new ObjectMapper().readValue(dto, UserRelation.class);
                    validRecords.add(userRelation);
                } else {
                    invalidRecords.add(result.getErrors().toString());
                }
            }

            // Save valid records to MongoDB
            userRelationRepository.saveAll(validRecords);

            // Prepare response
            response.put("validCount", validRecords.size());
            response.put("invalidCount", invalidRecords.size());
            response.put("invalidRecords", invalidRecords);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}




