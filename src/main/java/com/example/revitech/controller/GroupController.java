package com.example.revitech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.revitech.entity.ChatGroup;
import com.example.revitech.entity.GroupMessage;
import com.example.revitech.repository.ChatGroupRepository;
import com.example.revitech.repository.GroupMessageRepository;

@Controller
public class GroupController {

    private final ChatGroupRepository chatGroupRepository;
    private final GroupMessageRepository groupMessageRepository;

    @Autowired
    public GroupController(ChatGroupRepository chatGroupRepository, GroupMessageRepository groupMessageRepository) {
        this.chatGroupRepository = chatGroupRepository;
        this.groupMessageRepository = groupMessageRepository;
    }

    @GetMapping("/group")
    public String groupList(Model model) {
        List<ChatGroup> groups = chatGroupRepository.findAll();
        model.addAttribute("groups", groups);
        return "group";
    }

    @GetMapping("/group/{id}")
    public String groupChat(@PathVariable Long id, Model model) {
        Optional<ChatGroup> g = chatGroupRepository.findById(id);
        if (g.isEmpty()) {
            return "redirect:/group";
        }
        List<GroupMessage> messages = groupMessageRepository.findByGroupId(id);
        model.addAttribute("group", g.get());
        model.addAttribute("messages", messages);
        return "group"; // 既存 group.html を利用。必要なら group-chat.html に分ける
    }
}
