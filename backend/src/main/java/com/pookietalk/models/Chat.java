package com.pookietalk.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_name", nullable = false)
    private String chatName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "chat_participants",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    // Constructor for setting only ID
    public Chat(Long id) {
        this.id = id;
    }
}
