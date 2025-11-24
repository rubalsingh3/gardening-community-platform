package com.gardencommunity.model;

import java.time.LocalDateTime;

public class Discussion {

    private int id;
    private int authorId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public Discussion(int id, int authorId, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
