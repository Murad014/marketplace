package com.azercell.marketplace.filemanager.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Wires the S3 client for non-local profiles. Absent in the {@code local} profile, so dev needs no AWS creds.
 */
@Configuration
@Profile("!local")
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3Config {

    @Bean
    S3Client s3Client(S3StorageProperties props) {
        return S3Client.builder()
                .region(Region.of(props.region()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
