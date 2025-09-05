package com.project.aws.AwsS3.controller;

import com.project.aws.AwsS3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class AmazonS3Controller {

    private final AwsS3Service s3Service;

    // 업로드: POST /files?key=경로/파일명
    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam("key") String key) throws Exception {
        String savedKey = s3Service.upload(key, file);
        return ResponseEntity.ok(savedKey);
    }

    // 다운로드: GET /files/{key}  (key에 슬래시가 있을 수 있으니 정규식으로 받습니다)
    @GetMapping("/{key:.+}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String key) {
        InputStreamResource resource = s3Service.download(key);
        String contentType = s3Service.getContentType(key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key.substring(key.lastIndexOf('/')+1) + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // 목록: GET /files?prefix=폴더/경로/
    @GetMapping
    public ResponseEntity<List<String>> list(@RequestParam(value = "prefix", required = false) String prefix) {
        return ResponseEntity.ok(s3Service.list(prefix));
    }

    // 삭제: DELETE /files/{key}
    @DeleteMapping("/{key:.+}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        s3Service.delete(key);
        return ResponseEntity.noContent().build();
    }

    // (옵션) 프리사인 GET URL: GET /files/presign/{key}?minutes=60
    @GetMapping("/presign/{key:.+}")
    public ResponseEntity<String> presign(@PathVariable String key,
                                          @RequestParam(defaultValue = "60") long minutes) {
        URL url = s3Service.presignedGetUrl(key, Duration.ofMinutes(minutes));
        return ResponseEntity.ok(url.toString());
    }
}
