package com.example.blog.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${app.upload.base-dir}")
    private String baseDir;

    public StoredFile store(MultipartFile file) throws IOException {
        LocalDate today = LocalDate.now();
        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
        Path targetDir = Paths.get(baseDir, "posts", String.valueOf(today.getYear()), String.format("%02d", today.getMonthValue()));
        Files.createDirectories(targetDir);
        Path target = targetDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        String url = String.format("/assets/posts/%d/%02d/%s", today.getYear(), today.getMonthValue(), filename);
        return new StoredFile(url, filename, file.getSize());
    }

    private String getExtension(String name) {
        if (name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
