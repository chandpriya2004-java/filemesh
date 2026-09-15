package com.filemesh.storage.controller;

import com.filemesh.storage.service.StorageService;
import com.filemesh.user.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("storage")
public class StorageController {

    @Autowired
    private StorageService s3StorageService;

    @PostMapping("v1/upload_file")
    public ResponseEntity<ApiResponse> uploadFile(@Valid  @RequestParam("userId") String userId,
                                                  @Valid @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3StorageService.uploadFile(file,userId));
    }
}
