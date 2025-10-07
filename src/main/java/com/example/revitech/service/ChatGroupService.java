package com.example.revitech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.revitech.entity.ChatGroup;
import com.example.revitech.repository.ChatGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;

    public List<ChatGroup> findAll() {
        return chatGroupRepository.findAll();
    }

    public Optional<ChatGroup> findById(Long id) {
        return chatGroupRepository.findById(id);
    }

    public ChatGroup save(ChatGroup chatGroup) {
        return chatGroupRepository.save(chatGroup);
    }

    public void deleteById(Long id) {
        chatGroupRepository.deleteById(id);
    }
}
