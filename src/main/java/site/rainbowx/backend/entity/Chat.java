package site.rainbowx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @Column(name = "id", length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 10)
    private String type; // "group" 或 "private"

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ElementCollection
    @CollectionTable(name = "chat_participants", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "user_id", length = 20)
    private List<String> participants;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}