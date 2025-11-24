package com.gardencommunity.model;

import java.time.LocalDateTime;

public class Tip {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private int id;
    private int authorId;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdAt;

    public Tip(int id, int authorId, String title, String description,
               Status status, LocalDateTime createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(Status status) { this.status = status; }
}
