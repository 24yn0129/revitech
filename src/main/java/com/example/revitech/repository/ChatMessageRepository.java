package com.example.revitech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.revitech.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT c FROM ChatMessage c " +
           "WHERE c.group.id = :groupId " +
           "ORDER BY c.createdAt ASC")
    List<ChatMessage> findMessagesByGroup(@Param("groupId") Long groupId);

}
