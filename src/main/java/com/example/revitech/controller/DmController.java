package com.example.revitech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.revitech.entity.ChatGroup;
import com.example.revitech.entity.ChatMessage;
import com.example.revitech.entity.Users;
import com.example.revitech.repository.ChatGroupRepository;
import com.example.revitech.repository.ChatMessageRepository;
import com.example.revitech.repository.UsersRepository;

@Controller
public class DmController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private UsersRepository usersRepository;

    // DMページ表示
    @GetMapping("/dm")
    public String showDmPage(@RequestParam(value = "receiverId", required = false) Long receiverId,
                             Model model) {

        Long currentUserId = 1L; // 仮ログインユーザー
        Users currentUser = usersRepository.findById(currentUserId).orElse(null);

        Users partnerUser = null;
        ChatGroup group = null;

        if (receiverId != null) {
            partnerUser = usersRepository.findById(receiverId).orElse(null);

            if (currentUser != null && partnerUser != null) {
                String groupName = "DM:" + currentUser.getId() + "_" + partnerUser.getId();
                group = chatGroupRepository.findByName(groupName)
                        .orElseGet(() -> {
                            ChatGroup g = new ChatGroup();
                            g.setName(groupName);
                            return chatGroupRepository.save(g);
                        });
            }
        }

        List<ChatMessage> messages;
        if (currentUser != null && partnerUser != null) {
            messages = chatMessageRepository.findChatBetweenUsers(currentUserId, partnerUser.getId());
        } else {
            messages = List.of();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("partnerUser", partnerUser);
        model.addAttribute("group", group);

        return "dm";
    }

    // メッセージ送信
    @PostMapping("/dm/send")
    public String sendMessage(@RequestParam("receiverId") Long receiverId,
                              @RequestParam("content") String content) {

        Long currentUserId = 1L; // 仮ログインユーザー
        Users sender = usersRepository.findById(currentUserId).orElse(null);
        Users receiver = usersRepository.findById(receiverId).orElse(null);

        if (sender == null || receiver == null) {
            return "redirect:/dm";
        }

        // 既存 DM グループを取得 or 作成
        String groupName = "DM:" + sender.getId() + "_" + receiver.getId();
        ChatGroup group = chatGroupRepository.findByName(groupName)
                .orElseGet(() -> {
                    ChatGroup g = new ChatGroup();
                    g.setName(groupName);
                    return chatGroupRepository.save(g);
                });

        ChatMessage message = new ChatMessage();
        message.setGroup(group);
        message.setSender(sender);
        message.setBody(content);

        chatMessageRepository.save(message);

        return "redirect:/dm?receiverId=" + receiverId;
    }
}
