package com.filemesh.user.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpDto {
    private String token;

    private UserDto user;
}
