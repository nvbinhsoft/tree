package com.example.blog.dto;

public class PublicSettingsResponse {
    private String blogTitle;
    private String blogSubtitle;
    private String authorName;
    private String authorBio;
    private String profileImageUrl;
    private String socialGithub;
    private String socialLinkedin;
    private String socialX;

    public PublicSettingsResponse(String blogTitle, String blogSubtitle, String authorName, String authorBio,
                                  String profileImageUrl, String socialGithub, String socialLinkedin, String socialX) {
        this.blogTitle = blogTitle;
        this.blogSubtitle = blogSubtitle;
        this.authorName = authorName;
        this.authorBio = authorBio;
        this.profileImageUrl = profileImageUrl;
        this.socialGithub = socialGithub;
        this.socialLinkedin = socialLinkedin;
        this.socialX = socialX;
    }

    public String getBlogTitle() { return blogTitle; }
    public String getBlogSubtitle() { return blogSubtitle; }
    public String getAuthorName() { return authorName; }
    public String getAuthorBio() { return authorBio; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getSocialGithub() { return socialGithub; }
    public String getSocialLinkedin() { return socialLinkedin; }
    public String getSocialX() { return socialX; }
}
