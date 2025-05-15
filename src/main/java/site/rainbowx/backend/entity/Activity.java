package site.rainbowx.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @Column(name = "activity_id", length = 20)
    private String id; // 示例：act1

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Volunteer manager;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ActivityStatus status = ActivityStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ActivityType type;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Embedded
    private Material materials;
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registrations> registrations = new ArrayList<>();

    // 获取活动的所有参与者
    public List<User> getParticipants() {
        return registrations.stream()
                .map(Registrations::getUser)
                .collect(Collectors.toList());
    }
    @ManyToMany
    @JoinTable(
            name = "activity_participation",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id")
    )
    private List<Volunteer> participants = new ArrayList<>();

    @Column(name = "min_participants")
    private Integer minParticipants;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Column(name = "service_hours")
    private Integer serviceHours;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_hot")
    private Boolean isHot = false;

    // 内嵌物资类
    @Embeddable
    @Data
    public static class Material {
        @Column(name = "material_upload_time")
        private LocalDateTime uploadedAt;
    }

    public enum ActivityStatus {
        PENDING, ONGOING, COMPLETED, CANCELED
    }

    public enum ActivityType {
        ENVIRONMENTAL_PROTECTION,
        EDUCATION_SUPPORT,
        COMMUNITY_SERVICE
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
