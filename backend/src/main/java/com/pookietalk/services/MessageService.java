package com.pookietalk.services;

import com.pookietalk.dto.MessageDTO;
import com.pookietalk.dto.UserDTO;
import com.pookietalk.models.Chat;
import com.pookietalk.models.Message;
import com.pookietalk.models.User;
import com.pookietalk.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    public List<MessageDTO> getMessagesByChat(Long chatId) {
        // Validate chat exists
        chatService.getChatById(chatId);

        List<Message> messages = messageRepository.findByChatId(chatId);
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MessageDTO sendMessage(MessageDTO messageDTO) {
        // Validate user and chat
        UserDTO userDTO = userService.getUserById(messageDTO.getSenderId());
        chatService.getChatById(messageDTO.getChatId());

        Message message = convertToEntity(messageDTO);
        message.setTimestamp(LocalDateTime.now());
        message.setSender(new User(userDTO.getId()));

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setChatId(message.getChat().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }

    private Message convertToEntity(MessageDTO messageDTO) {
        Message message = new Message();
        message.setChat(new Chat(messageDTO.getChatId()));
        message.setContent(messageDTO.getContent());
        return message;
    }
}
