package com.wishstream.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishstream.core.dto.UserRelationDTO;
import com.wishstream.core.utils.JSONValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public  interface UserService {

    Map<String, Object> saveUsers(List<String> userRelationList);



}
