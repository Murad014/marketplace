package com.azercell.marketplace.filemanager.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Storage abstraction for product images. The write path ({@link #store}) is used in every profile;
 * the concrete adapter is chosen by Spring profile — local disk for dev, S3 for qa/prod.
 */
public interface FileStorage {

    /**
     * Stores one image under {@code objectName} and returns the URL the frontend uses in {@code <img src>}.
     * Local disk returns a relative {@code images/<name>} URL served by this app; S3 returns the absolute
     * public S3/CDN URL (this app is then out of the download path).
     */
    String store(MultipartFile file, String objectName);

    /**
     * Loads a stored image for serving by this app. Only the local-disk adapter implements this — in S3 mode
     * images are served directly from S3/CDN via the returned URL, so this returns empty (→ 404).
     */
    default Optional<Resource> loadAsResource(String objectName) {
        return Optional.empty();
    }
}
