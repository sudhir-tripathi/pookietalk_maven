package com.pookietalk.repositories;

import com.pookietalk.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(Long chatId);
}
