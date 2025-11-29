package com.example.blog.upload;

public class StoredFile {
    private final String url;
    private final String fileName;
    private final long size;

    public StoredFile(String url, String fileName, long size) {
        this.url = url;
        this.fileName = fileName;
        this.size = size;
    }

    public String getUrl() { return url; }
    public String getFileName() { return fileName; }
    public long getSize() { return size; }
}
