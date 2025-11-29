package com.example.blog.settings;

import com.example.blog.dto.PublicSettingsResponse;
import com.example.blog.dto.SettingsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public PublicSettingsResponse getPublicSettings() {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
        return toDto(settings);
    }

    public PublicSettingsResponse getAdminSettings() {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
        return toDto(settings);
    }

    @Transactional
    public PublicSettingsResponse update(SettingsRequest request) {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
        settings.setBlogTitle(request.getBlogTitle());
        settings.setBlogSubtitle(request.getBlogSubtitle());
        settings.setAuthorName(request.getAuthorName());
        settings.setAuthorBio(request.getAuthorBio());
        settings.setProfileImageUrl(request.getProfileImageUrl());
        settings.setSocialGithub(request.getSocialGithub());
        settings.setSocialLinkedin(request.getSocialLinkedin());
        settings.setSocialX(request.getSocialX());
        settingsRepository.save(settings);
        return toDto(settings);
    }

    private PublicSettingsResponse toDto(Settings settings) {
        return new PublicSettingsResponse(settings.getBlogTitle(), settings.getBlogSubtitle(),
                settings.getAuthorName(), settings.getAuthorBio(), settings.getProfileImageUrl(),
                settings.getSocialGithub(), settings.getSocialLinkedin(), settings.getSocialX());
    }
}
