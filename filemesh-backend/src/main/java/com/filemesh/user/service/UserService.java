package com.filemesh.user.service;

import com.filemesh.user.dto.AddUserDto;
import com.filemesh.user.dto.ApiResponse;

public interface UserService {

    ApiResponse validateEmail(String email);
    ApiResponse validateOTP(String email , String otp );
    ApiResponse addUser(AddUserDto userDto);
}
