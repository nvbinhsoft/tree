package com.example.blog.settings;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blog_title")
    private String blogTitle;

    @Column(name = "blog_subtitle")
    private String blogSubtitle;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_bio", columnDefinition = "text")
    private String authorBio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "social_github")
    private String socialGithub;

    @Column(name = "social_linkedin")
    private String socialLinkedin;

    @Column(name = "social_x")
    private String socialX;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBlogTitle() { return blogTitle; }
    public void setBlogTitle(String blogTitle) { this.blogTitle = blogTitle; }
    public String getBlogSubtitle() { return blogSubtitle; }
    public void setBlogSubtitle(String blogSubtitle) { this.blogSubtitle = blogSubtitle; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorBio() { return authorBio; }
    public void setAuthorBio(String authorBio) { this.authorBio = authorBio; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public String getSocialGithub() { return socialGithub; }
    public void setSocialGithub(String socialGithub) { this.socialGithub = socialGithub; }
    public String getSocialLinkedin() { return socialLinkedin; }
    public void setSocialLinkedin(String socialLinkedin) { this.socialLinkedin = socialLinkedin; }
    public String getSocialX() { return socialX; }
    public void setSocialX(String socialX) { this.socialX = socialX; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
