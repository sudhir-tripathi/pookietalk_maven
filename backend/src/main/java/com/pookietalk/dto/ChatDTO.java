package com.pookietalk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ChatDTO {
    private Long id;
    private String chatName;
    private Set<Long> participantIds;
}
