package com.example.blog.dto;

public class TagResponse {
    private Long id;
    private String name;
    private String slug;

    public TagResponse(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
}
