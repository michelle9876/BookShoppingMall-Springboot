package com.github.ecommerce.web.controller.auth;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class AmazonS3Controller {
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.buckets.bucket1.name}")
    private String bucket1;

    @PostMapping("/upload")
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

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadImage(String originalFilename) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket1)
                .key(originalFilename)
                .build();

        byte[] content = s3Client.getObject(getObjectRequest).readAllBytes();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(content));

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/delete")
    public void deleteImage(String originalFilename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket1)
                .key(originalFilename)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}
