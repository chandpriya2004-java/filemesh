package com.filemesh.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean registrationComplete;
}
