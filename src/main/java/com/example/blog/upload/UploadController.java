package com.example.blog.upload;

import com.example.blog.dto.UploadResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/uploads")
public class UploadController {

    private final FileStorageService storageService;

    public UploadController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        StoredFile stored = storageService.store(file);
        // This demo does not persist uploads, so id is not tracked.
        return ResponseEntity.ok(new UploadResponse(0L, stored.getUrl(), stored.getFileName(), stored.getSize()));
    }
}
