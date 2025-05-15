package site.rainbowx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "registrations")
@IdClass(Registrations.RegistrationId.class)
public class Registrations {

    @Getter
    @Setter
    public static class RegistrationId implements Serializable {
        private Long userId;  // ✅ 正确类型
        private Long activityId;

        public RegistrationId() {}  // JPA要求无参构造器

        public RegistrationId(Long userId, Long activityId) {
            this.userId = userId;
            this.activityId = activityId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegistrationId that = (RegistrationId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(activityId, that.activityId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, activityId);
        }
    }

    // 主键字段必须与复合主键类字段匹配
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @Column(name = "activity_id")
    private Long activityId;  // ✅ 正确类型

    @Column(name = "apply_time", nullable = false)
    private LocalDateTime applyTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_status", length = 10)
    private JoinStatus joinStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_status", length = 10)
    private SignStatus signStatus;

    @Column(name = "sign_time")
    private LocalDateTime signTime;

    @Column(name = "service_time")
    private Integer serviceTime;

    @ElementCollection
    @CollectionTable(
            name = "proof_files",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")
            }
    )
    @Column(name = "file_name")
    private List<String> proofFiles;

    @Column(name = "credit_change")
    private Double creditChange;

    public enum JoinStatus {
        出席, 缺席, 待处理
    }

    public enum SignStatus {
        是, 否, 待确认
    }

    @PrePersist
    protected void prePersist() {
        if (applyTime == null) {
            applyTime = LocalDateTime.now();
        }
    }
}