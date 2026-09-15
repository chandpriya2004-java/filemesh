package com.filemesh.storage.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadDto {

    private String userId;
    private MultipartFile file;

}
