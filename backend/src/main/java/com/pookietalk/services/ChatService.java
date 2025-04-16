package com.pookietalk.services;

import com.pookietalk.dto.ChatDTO;
import com.pookietalk.exceptions.ChatNotFoundException;
import com.pookietalk.exceptions.UserNotFoundException;
import com.pookietalk.models.Chat;
import com.pookietalk.models.User;
import com.pookietalk.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;

    public ChatDTO createChat(ChatDTO chatDTO) {
        Chat chat = new Chat();
        chat.setChatName(chatDTO.getChatName());

        Set<User> participants = chatDTO.getParticipantIds().stream()
                .map(userService::getUserById)
                .map(userDTO -> new User(userDTO.getId()))
                .collect(Collectors.toSet());

        chat.setParticipants(participants);
        chat = chatRepository.save(chat);

        return convertToDTO(chat);
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found with id: " + id));
    }

    public List<ChatDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getUserChats(String username) {
        try {
            Long userId = userService.getUserByUsername(username).getId();
            User user = new User(userId);
            return chatRepository.findByParticipantsContaining(user).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }

    private ChatDTO convertToDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setParticipantIds(
                chat.getParticipants().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}
