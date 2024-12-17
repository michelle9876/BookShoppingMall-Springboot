package com.github.ecommerce.web.controller.auth;


import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.buckets.bucket1.name}")
    private String bucket1;

    @PostMapping("api/save")
    public String saveFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        // S3에 파일 업로드
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket1)
                .key(originalFilename)
                .contentType(multipartFile.getContentType()) // MIME 타입 설정
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket1).key(originalFilename).build()).toString();
    }
}