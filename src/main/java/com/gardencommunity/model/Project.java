package com.gardencommunity.model;

import java.time.LocalDateTime;

public class Project {

    private int id;
    private int ownerId;
    private String title;
    private String description;
    private int progress; // 0–100
    private LocalDateTime createdAt;

    public Project(int id, int ownerId, String title, String description,
                   int progress, LocalDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getOwnerId() { return ownerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getProgress() { return progress; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setProgress(int progress) { this.progress = progress; }
}
