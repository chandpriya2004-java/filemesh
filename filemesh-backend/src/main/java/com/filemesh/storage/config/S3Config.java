package com.filemesh.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


import java.net.URI;

@Configuration
public class S3Config {

    @Value("${aws.s3.access-key}")
    private String awsAccessKey;

    @Value("${aws.s3.secret-key}")
    private String awsSecretKey;

    @Value("${aws.s3.region}")
    private String awsRegion;

    @Value("${cloudflare.r2.access-key}")
    private String r2AccessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String r2SecretKey;

    @Value("${cloudflare.r2.endpoint}")
    private String r2Endpoint;

    @Bean("awsS3Client")
    public S3Client s3Client() {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(awsAccessKey,awsSecretKey);

        return S3Client.builder().region(Region.of(awsRegion)).credentialsProvider(
                        StaticCredentialsProvider.create(credentials)).build();
    }

    @Bean("r2Client")
    public S3Client r2Client() {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(r2AccessKey, r2SecretKey);

        return S3Client.builder().endpointOverride(URI.create(r2Endpoint)).region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
    }
}