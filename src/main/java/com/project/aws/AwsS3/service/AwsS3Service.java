package com.project.aws.AwsS3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    private final String bucket = "your-bucket-name"; // yml에서 주입받고 싶으면 @Value 써도 됩니다.

    public String upload(String key, MultipartFile file) throws IOException {
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return key;
    }

    public InputStreamResource download(String key) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(get);
        return new InputStreamResource(s3Object);
    }

    public String getContentType(String key) {
        HeadObjectResponse head = s3Client.headObject(
                HeadObjectRequest.builder().bucket(bucket).key(key).build());
        return head.contentType() != null ? head.contentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    public List<String> list(String prefix) {
        ListObjectsV2Request req = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix == null ? "" : prefix)
                .build();

        ListObjectsV2Response res = s3Client.listObjectsV2(req);
        return res.contents().stream().map(S3Object::key).collect(Collectors.toList());
    }

    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    // (옵션) 모두에게 간단히 다운로드 링크를 공유하고 싶을 때 유용
    public URL presignedGetUrl(String key, Duration expiresIn) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
                .signatureDuration(expiresIn)
                .getObjectRequest(get)
                .build();

        PresignedGetObjectRequest pre = s3Presigner.presignGetObject(presign);
        return pre.url();
    }
}
