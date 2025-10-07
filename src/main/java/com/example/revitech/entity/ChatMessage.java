package com.example.revitech.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // グループメッセージ用（グループIDがある場合はDMでは使わない）
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatGroup group;

    // 送信者
    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private Users sender;

    // 1対1 DM用の受信者
    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private Users receiver;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ChatMessage() {}

    // グループメッセージ用コンストラクタ
    public ChatMessage(ChatGroup group, Users sender, String body) {
        this.group = group;
        this.sender = sender;
        this.body = body;
    }

    // 1対1 DM用コンストラクタ
    public ChatMessage(Users sender, Users receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // getter / setter
    public Long getId() { return id; }

    public ChatGroup getGroup() { return group; }
    public void setGroup(ChatGroup group) { this.group = group; }

    public Users getSender() { return sender; }
    public void setSender(Users sender) { this.sender = sender; }

    public Users getReceiver() { return receiver; }
    public void setReceiver(Users receiver) { this.receiver = receiver; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
