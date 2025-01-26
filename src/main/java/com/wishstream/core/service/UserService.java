package com.wishstream.core.service;

import com.wishstream.core.dto.UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public  interface UserService {

    Map<String, Object> saveOrUpdate(UserRequest userRequest);




}
