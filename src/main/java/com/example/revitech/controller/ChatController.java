package com.example.revitech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.revitech.entity.ChatGroup;
import com.example.revitech.entity.ChatMessage;
import com.example.revitech.entity.Users;
import com.example.revitech.repository.ChatGroupRepository;
import com.example.revitech.repository.ChatMessageRepository;
import com.example.revitech.repository.UsersRepository;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam Long groupId,
                                         @RequestParam Long senderId,
                                         @RequestParam String content) {

        ChatGroup chatGroup = chatGroupRepository.findById(groupId).orElse(null);
        Users senderUser = usersRepository.findById(senderId).orElse(null);

        if (chatGroup == null || senderUser == null) {
            return ResponseEntity.badRequest().body("Group or sender not found");
        }

        ChatMessage message = new ChatMessage(chatGroup, senderUser, content);
        chatMessageRepository.save(message);

        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<ChatMessage>> getInbox(@RequestParam Long groupId) {
        ChatGroup chatGroup = chatGroupRepository.findById(groupId).orElse(null);
        if (chatGroup == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatMessage> messages = chatMessageRepository.findByGroupOrderByIdAsc(chatGroup);
        return ResponseEntity.ok(messages);
    }
}
