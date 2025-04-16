package com.pookietalk.controllers;

import com.pookietalk.dto.ChatDTO;
import com.pookietalk.models.Chat;
import com.pookietalk.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable String username) {
        return ResponseEntity.ok(chatService.getUserChats(username));
    }

    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chatDTO) {
        return ResponseEntity.ok(chatService.createChat(chatDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @GetMapping
    public ResponseEntity<List<ChatDTO>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }
}
