package com.filemesh.user.serviceImp;

import com.filemesh.auth.service.JwtService;
import com.filemesh.user.appContent.APPMSG;
import com.filemesh.user.appContent.APPSTATUSCODE;
import com.filemesh.user.dto.*;
import com.filemesh.user.entity.OtpValidation;
import com.filemesh.user.entity.User;
import com.filemesh.user.repo.OtpValidationRepo;
import com.filemesh.user.repo.UserRepo;
import com.filemesh.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImp implements UserService {

    @Value("${otp.valid-till-min}")
    private int otpValidationTime;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpValidationRepo validationRepo;

    @Autowired
    private JwtService jwtService;

    @Override
    public ApiResponse validateEmail(String email) {

    final String otp = generateOtp();
    System.out.println(otp);

    OtpValidation otpValidation = validationRepo.findByEmail(email);
    if (otpValidation == null) {
        validationRepo.save(OtpValidation.builder().createdAt(LocalDateTime.now())
                .expiryAt(LocalDateTime.now().plusMinutes(otpValidationTime)).email(email).otp(otp).build());
    } else {
        otpValidation.setOtp(otp);
        otpValidation.setCreatedAt(LocalDateTime.now());
        otpValidation.setExpiryAt(LocalDateTime.now().plusMinutes(otpValidationTime));
        otpValidation.setOtpSentCount(otpValidation.getOtpSentCount() + 1);
        validationRepo.save(otpValidation);
    }

    return ApiResponse.builder().status(APPSTATUSCODE.SUCCESS.label).message(APPMSG.OTP_SEND).build();
}


    private String generateOtp(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }



    @Override
    public ApiResponse validateOTP(String email , String otp ) {

        OtpValidation emailValidation = validationRepo
                .findTopByEmailOrderByCreatedAtDesc(email);

        if (emailValidation == null) {
            return ApiResponse.builder().message(APPMSG.OTP_NOT_GENERATED).status(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }

        if (!emailValidation.getOtp().equals(otp)) {
            return ApiResponse.builder().message(APPMSG.INVALID_OTP).status(APPSTATUSCODE.FAIL.label).build();
        }

        if (LocalDateTime.now().isAfter(emailValidation.getExpiryAt())) {
            return ApiResponse.builder().message(APPMSG.EXPIRED_OTP).status(APPSTATUSCODE.FAIL.label).build();
        }

        User user = userRepo.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user = userRepo.save(user);
        }

        String token = jwtService.generateToken(user);

        ValidateOtpDto response = ValidateOtpDto.builder().token(token).
                user(UserDto.builder().id(user.getId()).email(user.getEmail()).
                        firstName(user.getFirstName()).lastName(user.getLastName()).
                        registrationComplete(user.isRegistrationComplete()).build()).build();

        return ApiResponse.builder().status(APPSTATUSCODE.SUCCESS.label).message(APPMSG.VALID_OTP).data(response).build();
    }

    @Override
    public ApiResponse addUser(AddUserDto userDto) {

        User user = userRepo.findByEmail(userDto.getEmail());

        if(user == null){
            return ApiResponse.builder().status(APPSTATUSCODE.FAIL.label).message(APPMSG.USER_NOT_FOUND).build();
        }

        if(user.isRegistrationComplete()){
            return ApiResponse.builder().status(APPSTATUSCODE.FAIL.label).message(APPMSG.USER_ALREADY_REGISTERED).build();
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRegistrationComplete(true);
        userRepo.save(user);

        return ApiResponse.builder().status(APPSTATUSCODE.SUCCESS.label).message(APPMSG.USER_REGISTERED_SUCCESS).data(user).build();
    }
}


