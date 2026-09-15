package com.filemesh.storage.service;

import com.filemesh.user.dto.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    ApiResponse uploadFile(MultipartFile file, String userId) throws IOException;
}
