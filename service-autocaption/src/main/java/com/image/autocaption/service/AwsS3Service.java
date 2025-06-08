package com.image.autocaption.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AwsS3Service {

    private static final String BUCKET_NAME = "autocaption-images";

    private final AmazonS3 amazonS3;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // Upload the file to S3
        amazonS3.putObject(BUCKET_NAME, fileName, inputStream, metadata);

        // Return the file URL from S3
        return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }
}
