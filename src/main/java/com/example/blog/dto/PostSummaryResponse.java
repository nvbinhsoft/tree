package com.example.blog.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class PostSummaryResponse {
    private Long id;
    private String slug;
    private String title;
    private String excerpt;
    private OffsetDateTime publishedAt;
    private String coverImageUrl;
    private Integer readTimeMinutes;
    private List<TagResponse> tags;

    public PostSummaryResponse(Long id, String slug, String title, String excerpt, OffsetDateTime publishedAt,
                               String coverImageUrl, Integer readTimeMinutes, List<TagResponse> tags) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.excerpt = excerpt;
        this.publishedAt = publishedAt;
        this.coverImageUrl = coverImageUrl;
        this.readTimeMinutes = readTimeMinutes;
        this.tags = tags;
    }

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getExcerpt() { return excerpt; }
    public OffsetDateTime getPublishedAt() { return publishedAt; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public Integer getReadTimeMinutes() { return readTimeMinutes; }
    public List<TagResponse> getTags() { return tags; }
}
