package com.filemesh.user.controller;

import com.filemesh.user.appContent.APPSTATUSCODE;
import com.filemesh.user.dto.AddUserDto;
import com.filemesh.user.dto.ApiResponse;
import com.filemesh.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private  UserService userService;

    @PostMapping("v1/validate_user")
    public  ResponseEntity<ApiResponse> validateEmail(@Valid @RequestParam String email) {
     return ResponseEntity.ok(userService.validateEmail(email));
    }

    @PostMapping("v1/validate_otp")
    public  ResponseEntity<ApiResponse> validateOtp(@Valid @RequestParam String email,@RequestParam String otp) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.validateOTP(email,otp));
    }

    @PostMapping("v1/add_user")
    public  ResponseEntity<ApiResponse> addUser(@Valid @RequestBody AddUserDto user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(user));
    }
}
