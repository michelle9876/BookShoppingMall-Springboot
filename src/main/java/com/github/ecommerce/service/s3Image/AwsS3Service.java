package com.github.ecommerce.service.s3Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AwsS3Service {
    private final S3Client amazonS3;

    @Value("${cloud.aws.s3.buckets.bucket1.name}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    private String bucketName = bucket;

    public String upload(MultipartFile image) {
        //입력받은 이미지 파일이 빈 파일인지 검증
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw S3Exception.builder()
                    .statusCode(404)
                    .message("이미지가 비어있거나 파일 이름이 없습니다.")
                    .build();
        }
        //uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환한다.
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        // validateImageFileExtention()을 호출하여 확장자 명이 올바른지 확인한다.
        this.validateImageFileExtension(image.getOriginalFilename());
        try {
            //uploadImageToS3()를 호출하여 이미지를 S3에 업로드하고,
            //S3에 저장된 이미지의 public url을 받아서 서비스 로직에 반환한다.
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw S3Exception.builder()
                    .statusCode(400) // Bad Request
                    .message("이미지 업로드 중 IO 예외가 발생했습니다: " + e.getMessage()) // 예외 메시지 설정
                    .cause(e) // 원인 예외 설정
                    .build();
        }
    }

    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw S3Exception.builder()
                    .statusCode(400) // Bad Request
                    .message("확장자를 찾을 수 없습니다. 파일명: " +filename)
                    .build();
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw S3Exception.builder()
                    .statusCode(400) // Bad Request
                    .message(allowedExtensionList.toString()+"의 확장자만 사용 가능합니다. 확장자: " +extension)
                    .build();
        }
    }

    //이미지를 S3에 업로드하고, S3에 저장된 이미지의 public url을  반환한다.
    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); // 원본 파일 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; // 변경된 파일 명


        // MultipartFile의 InputStream을 사용
        try (InputStream inputStream = image.getInputStream()) {
            // PutObjectRequest 생성
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket) // bucket 변수를 사용
                    .key(s3FileName)
                    .contentType("image/" + extension) // 콘텐츠 타입 설정
                    .acl(ObjectCannedACL.PUBLIC_READ) // ACL 설정 (선택적)
                    .build();

            // S3에 객체 업로드
            PutObjectResponse response = amazonS3
                    .putObject(putRequest, RequestBody.fromInputStream(inputStream, image.getSize()));

            // 업로드된 객체의 URL 반환
            String objectUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, s3FileName);
            return objectUrl;
        } catch (IOException e) {
            throw S3Exception.builder()
                    .statusCode(500)
                    .message("파일 업로드에 실패했습니다.")
                    .build();
        }
    }


    //업로드 된 이미지를 삭제한다.
    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);

        try {
            // S3에서 객체 삭제
            amazonS3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());

            log.info("S3에서 이미지 삭제 성공: {}", key);
        } catch (S3Exception e) {
            log.error("S3 이미지 삭제 실패: {}, 오류 메시지: {}", key, e.getMessage());
            throw S3Exception.builder()
                    .statusCode(500)
                    .message("이미지 삭제에 실패했습니다: " + key +", 에러메세지 : "+ e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("알 수 없는 오류 발생: {}, 오류 메시지: {}", key, e.getMessage());
            throw S3Exception.builder()
                    .statusCode(500)
                    .message("이미지 삭제 중 알 수 없는 오류가 발생했습니다. key: " + key +", 에러메세지 : "+ e.getMessage())
                    .build();
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException e) {
            log.error("잘못된 URL 형식: {}", imageAddress);
            throw S3Exception.builder()
                    .statusCode(500)
                    .message("잘못된 URL 형식입니다: " + imageAddress)
                    .build();
        } catch (UnsupportedEncodingException e) {
            log.error("URL 디코딩 실패: {}", imageAddress);
            throw S3Exception.builder()
                    .statusCode(500)
                    .message("URL 디코딩에 실패했습니다: " + imageAddress)
                    .build();
        }
    }
}





