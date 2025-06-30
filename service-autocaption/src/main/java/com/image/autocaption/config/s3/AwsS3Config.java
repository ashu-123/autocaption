package com.image.autocaption.config.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AWS & S3 credentials.
 */
@Configuration
public class AwsS3Config {

    @Bean
    public AmazonS3 amazonS3() {

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1) // Replace with the region you selected
                .build();
    }
}
