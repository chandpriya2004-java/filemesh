package com.filemesh.user.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {

    private String firstName;
    private String lastName;
    private String email;

}
