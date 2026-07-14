package com.azercell.marketplace.filemanager.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Local-disk storage for the {@code local} dev profile — writes to {@code app.image.upload-dir} and serves
 * the bytes back through this app's {@code GET /images/**}. Keeps dev working without any AWS credentials.
 */
@Component
@Profile("local")
public class LocalDiskFileStorage implements FileStorage {

    @Value("${app.image.upload-dir:uploads/images}")
    private String uploadDir;

    private Path storageDirectory;

    @PostConstruct
    void initStorage() {
        this.storageDirectory = Paths.get(uploadDir);
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory path", e);
        }
    }

    @Override
    public String store(MultipartFile file, String objectName) {
        try {
            Files.copy(file.getInputStream(), storageDirectory.resolve(objectName), StandardCopyOption.REPLACE_EXISTING);
            return "images/" + objectName;
        } catch (IOException e) {
            throw new RuntimeException("IO Error: Failed to store image file.", e);
        }
    }

    @Override
    public Optional<Resource> loadAsResource(String objectName) {
        Path base = storageDirectory.toAbsolutePath().normalize();
        Path target = base.resolve(objectName).normalize();
        if (!target.startsWith(base)) {
            return Optional.empty();   // path traversal attempt (e.g. ../../etc)
        }
        try {
            Resource resource = new UrlResource(target.toUri());
            return (resource.exists() && resource.isReadable()) ? Optional.of(resource) : Optional.empty();
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
