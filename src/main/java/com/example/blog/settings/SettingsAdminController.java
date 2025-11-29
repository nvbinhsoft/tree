package com.example.blog.settings;

import com.example.blog.dto.PublicSettingsResponse;
import com.example.blog.dto.SettingsRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settings")
public class SettingsAdminController {

    private final SettingsService settingsService;

    public SettingsAdminController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PublicSettingsResponse get() {
        return settingsService.getAdminSettings();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public PublicSettingsResponse update(@Valid @RequestBody SettingsRequest request) {
        return settingsService.update(request);
    }
}
