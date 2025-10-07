package com.example.revitech.entity;

public class ChatMember {
    private long id;
    private long roomId;
    private long userId;

    public ChatMember() {}

    public ChatMember(long id, long roomId, long userId) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getRoomId() {
        return roomId;
    }
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
