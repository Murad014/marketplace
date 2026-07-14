package com.azercell.marketplace.filemanager.storage;

import com.azercell.marketplace.filemanager.config.S3StorageProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

/**
 * S3 storage for qa/prod. Uploads objects and returns their public URL (CDN base if configured, else the
 * S3 virtual-hosted URL). Public read is granted by a bucket policy / CloudFront on the key prefix — no
 * per-object ACL is set (modern buckets have ACLs disabled). This app is out of the download path.
 */
@Component
@Profile("!local")
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;
    private final S3StorageProperties props;

    public S3FileStorage(S3Client s3Client, S3StorageProperties props) {
        this.s3Client = s3Client;
        this.props = props;
    }

    @Override
    public String store(MultipartFile file, String objectName) {
        String key = props.keyPrefix() + objectName;
        try {
            var request = PutObjectRequest.builder()
                    .bucket(props.bucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to S3.", e);
        }
        return publicUrl(key);
    }

    private String publicUrl(String key) {
        String base = props.publicBaseUrl();
        if (base != null && !base.isBlank()) {
            return trimTrailingSlash(base) + "/" + key;
        }
        return "https://" + props.bucket() + ".s3." + props.region() + ".amazonaws.com/" + key;
    }

    private static String trimTrailingSlash(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}
