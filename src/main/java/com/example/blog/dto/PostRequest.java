package com.example.blog.dto;

import com.example.blog.post.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PostRequest {
    private String slug;

    @NotBlank
    private String title;

    private String excerpt;

    @NotBlank
    private String body;

    @NotNull
    private PostStatus status;

    private List<Long> tagIds;

    private String coverImageUrl;

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }
    public List<Long> getTagIds() { return tagIds; }
    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
}
