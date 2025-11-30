package com.example.blog.upload;

import com.example.blog.dto.UploadResponse;
import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/uploads")
@Tag(name = "Uploads")
@SecurityRequirement(name = "bearerAuth")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    private final FileStorageService storageService;

    public UploadController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Upload an image", description = "Accepts multipart/form-data with field 'file'")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/admin/uploads filename='{}' size={}", file.getOriginalFilename(), file.getSize());
        StoredFile stored = storageService.store(file);
        // This demo does not persist uploads, so id is not tracked.
        return ResponseEntity.ok(new UploadResponse(0L, stored.getUrl(), stored.getFileName(), stored.getSize()));
    }
}
