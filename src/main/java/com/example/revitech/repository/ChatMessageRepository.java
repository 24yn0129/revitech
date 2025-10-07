package com.example.revitech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.revitech.entity.ChatGroup;
import com.example.revitech.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT c FROM ChatMessage c " +
           "WHERE (c.sender.id = :user1Id AND c.group.id = :user2GroupId) " +
           "   OR (c.sender.id = :user2Id AND c.group.id = :user1GroupId) " +
           "ORDER BY c.id ASC")
    List<ChatMessage> findChatBetweenUsers(@Param("user1Id") Long user1Id,
                                           @Param("user2Id") Long user2Id);

    // groupごとのメッセージを取得する
    List<ChatMessage> findByGroupOrderByIdAsc(ChatGroup group);

}

