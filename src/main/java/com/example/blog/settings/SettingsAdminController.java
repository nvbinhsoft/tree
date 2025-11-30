package com.example.blog.settings;

import com.example.blog.dto.PublicSettingsResponse;
import com.example.blog.dto.SettingsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settings")
@Tag(name = "Admin Settings")
@SecurityRequirement(name = "bearerAuth")
public class SettingsAdminController {

    private static final Logger log = LoggerFactory.getLogger(SettingsAdminController.class);
    private final SettingsService settingsService;

    public SettingsAdminController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get admin settings")
    public PublicSettingsResponse get() {
        log.info("GET /api/admin/settings");
        return settingsService.getAdminSettings();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "Update admin settings")
    public PublicSettingsResponse update(@Valid @RequestBody SettingsRequest request) {
        log.info("PUT /api/admin/settings title='{}'", request.getBlogTitle());
        return settingsService.update(request);
    }
}
