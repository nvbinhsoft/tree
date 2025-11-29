package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class SettingsRequest {
    @NotBlank
    private String blogTitle;

    private String blogSubtitle;
    private String authorName;
    private String authorBio;
    private String profileImageUrl;
    private String socialGithub;
    private String socialLinkedin;
    private String socialX;

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
}
