package com.example.revitech.entity;

import java.time.LocalDateTime;

public class ChatRoom {
    private long id;
    private String name;
    private String type;
    private long creatorUserId;
    private LocalDateTime createdAt;

    public ChatRoom() {}

    public ChatRoom(long id, String name, String type, long creatorUserId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.creatorUserId = creatorUserId;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public long getCreatorUserId() {
        return creatorUserId;
    }
    public void setCreatorUserId(long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
