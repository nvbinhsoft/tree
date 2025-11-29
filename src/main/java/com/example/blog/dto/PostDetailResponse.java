package com.example.blog.dto;

import com.example.blog.post.PostStatus;
import java.time.OffsetDateTime;
import java.util.List;

public class PostDetailResponse extends PostSummaryResponse {
    private String body;
    private PostStatus status;

    public PostDetailResponse(Long id, String slug, String title, String excerpt, OffsetDateTime publishedAt,
                              String coverImageUrl, Integer readTimeMinutes, List<TagResponse> tags,
                              String body, PostStatus status) {
        super(id, slug, title, excerpt, publishedAt, coverImageUrl, readTimeMinutes, tags);
        this.body = body;
        this.status = status;
    }

    public String getBody() { return body; }
    public PostStatus getStatus() { return status; }
}
