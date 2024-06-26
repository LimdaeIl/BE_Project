package com.fastcampus.aptner.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
@ToString
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public ResponseEntity<?> uploadFileV1(String category, List<MultipartFile> multiFileList) {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : multiFileList){
            if (file.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String fileName = CommonUtils.buildFileName(category, file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            urlList.add(amazonS3.getUrl(bucketName, fileName).toString());
        }
        return new ResponseEntity<>(urlList, HttpStatus.OK);
    }

    public static class CommonUtils {
        private static final String FILE_EXTENSION_SEPARATOR = ".";
        private static final String CATEGORY_PREFIX = "/";
        private static final String TIME_SEPARATOR = "_";

        public static String buildFileName(String category, String originalFileName) {
            int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
            String fileExtension = originalFileName.substring(fileExtensionIndex);
            String fileName = originalFileName.substring(0, fileExtensionIndex);
            String now = String.valueOf(System.currentTimeMillis());

            return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
        }
    }

}
