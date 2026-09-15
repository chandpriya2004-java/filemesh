package com.filemesh.storage.serviceImp;

import com.filemesh.storage.UserStoreRepo;
import com.filemesh.storage.entity.UserStore;
import com.filemesh.storage.service.StorageService;
import com.filemesh.user.appContent.APPMSG;
import com.filemesh.user.appContent.APPSTATUSCODE;
import com.filemesh.user.dto.ApiResponse;
import com.filemesh.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class StorageServiceImp implements StorageService {

    @Autowired
    private UserRepo userRepo;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Autowired
    private UserStoreRepo userStoreRepo;

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    @Value("${cloudflare.r2.bucket-name}")
    private String r2BucketName;

    @Autowired
    @Qualifier("awsS3Client")
    private S3Client s3Client;

    @Autowired
    @Qualifier("r2Client")
    private S3Client r2Client;

    @Autowired
    @Qualifier("storageExecutor")
    private Executor storageExecutor;

    @Override
    public ApiResponse uploadFile(MultipartFile file, String userId) throws IOException {

        if(!userRepo.existsById(userId)){
            return ApiResponse.builder().status(APPSTATUSCODE.FAIL.label).message(APPMSG.USER_NOT_FOUND).build();
        }
        if (file.isEmpty()) {
            return ApiResponse.builder().status(APPSTATUSCODE.FAIL.label).message(APPMSG.EMPTY_FILE).build();
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ApiResponse.builder().status(APPSTATUSCODE.FAIL.label).message(APPMSG.MAX_FILE_SIZE_MSG).build();
        }

        String storageKey = generateStorageKey(userId,file.getOriginalFilename());

        byte[] fileBytes = file.getBytes();
        String contentType = file.getContentType();
        Long fileSize = file.getSize();

        CompletableFuture<Void> s3 = CompletableFuture.runAsync(() -> uploadToS3(fileBytes, storageKey,contentType,fileSize), storageExecutor);
        CompletableFuture<Void> r2 = CompletableFuture.runAsync(() -> uploadToR2(fileBytes, storageKey, contentType,fileSize), storageExecutor);
        CompletableFuture.allOf(s3, r2).join();

        UserStore store = userStoreRepo.save(UserStore.builder().user_id(userId)
                .original_name(file.getOriginalFilename()).content_type(file.getContentType())
                .size(file.getSize()).storage_key(storageKey).build());

        return ApiResponse.builder().status(APPSTATUSCODE.SUCCESS.label).message(APPMSG.FILE_UPLOADED).data(store).build();
    }

    private void uploadToS3(byte[] fileBytes, String storageKey, String contentType , Long fileSize) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(s3BucketName).key(storageKey).contentType(contentType)
                    .contentLength(fileSize).build();
            s3Client.putObject(request, RequestBody.fromBytes(fileBytes));

        } catch (Exception e) {
            log.error("Failed to upload file to S3. Key: {}", storageKey, e);
            throw e;
        }
    }

    private void uploadToR2(byte[] fileBytes, String storageKey, String contentType, Long fileSize) {

        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(r2BucketName).key(storageKey).contentType(contentType)
                            .contentLength(fileSize).build();
           r2Client.putObject(request, RequestBody.fromBytes(fileBytes));

        } catch (Exception e) {
            log.error("Failed to upload file to S3. Key: {}", storageKey, e);
            throw e;
        }
    }


    private String generateStorageKey(String userId, String fileName) {
        return "user-" + userId + "/" + UUID.randomUUID() + "-" + fileName;
    }


}