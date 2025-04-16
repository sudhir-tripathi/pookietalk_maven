package com.pookietalk.controllers;

import com.pookietalk.dto.MessageDTO;
import com.pookietalk.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.getMessagesByChat(chatId));
    }

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.sendMessage(messageDTO));
    }
}
