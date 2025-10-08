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

    // DM画面表示
    @GetMapping("/dm")
    public String showDmPage(Model model) {
        // とりあえず固定ユーザー
        Long currentUserId = 3L; 
        Users currentUser = usersRepository.findById(currentUserId).orElse(null);

        // 固定の相手ユーザー
        Long partnerUserId = 2L;
        Users partnerUser = usersRepository.findById(partnerUserId).orElse(null);

        // DM用グループ作成または取得
        ChatGroup group = null;
        if (currentUser != null && partnerUser != null) {
            String groupName = "DM:" + currentUser.getId() + "_" + partnerUser.getId();
            group = chatGroupRepository.findByName(groupName)
                    .orElseGet(() -> {
                        ChatGroup g = new ChatGroup();
                        g.setName(groupName);
                        g.setType("DM");
                        g.setCreatarUserId(currentUser.getId());
                        return chatGroupRepository.save(g);
                    });
        }

        // メッセージ取得
        List<ChatMessage> messages = (group != null)
                ? chatMessageRepository.findMessagesByGroup(group.getId())
                : List.of();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("partnerUser", partnerUser);
        model.addAttribute("group", group);
        model.addAttribute("messages", messages);

        return "dm";
    }

    // DM送信
    @PostMapping("/dm/send")
    public String sendMessage(@RequestParam("groupId") Long groupId,
                              @RequestParam("senderId") Long senderId,
                              @RequestParam("content") String content) {

        ChatGroup group = chatGroupRepository.findById(groupId).orElse(null);
        Users sender = usersRepository.findById(senderId).orElse(null);

        if (group == null || sender == null) {
            return "redirect:/dm";
        }

        ChatMessage message = new ChatMessage(group, sender, content);
        chatMessageRepository.save(message);

        return "redirect:/dm"; // 常にこの固定DM画面に戻る
    }
}
