package com.example.blog.dto;

public class UploadResponse {
    private Long id;
    private String url;
    private String fileName;
    private long size;

    public UploadResponse(Long id, String url, String fileName, long size) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.size = size;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public String getFileName() { return fileName; }
    public long getSize() { return size; }
}
