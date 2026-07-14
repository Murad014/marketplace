package com.azercell.marketplace.filemanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * S3 storage settings. Credentials are NOT here — they come from the standard AWS chain
 * (env AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY, or an instance/role profile).
 *
 * @param bucket        target bucket name
 * @param region        AWS region (e.g. eu-central-1)
 * @param keyPrefix     object key prefix (folder), normalized to end with '/'
 * @param publicBaseUrl optional CDN/CloudFront base URL; when set, returned image URLs use it instead of the raw S3 host
 */
@ConfigurationProperties("app.storage.s3")
public record S3StorageProperties(String bucket, String region, String keyPrefix, String publicBaseUrl) {

    public S3StorageProperties {
        if (keyPrefix == null) {
            keyPrefix = "";
        } else if (!keyPrefix.isEmpty() && !keyPrefix.endsWith("/")) {
            keyPrefix = keyPrefix + "/";
        }
    }
}
