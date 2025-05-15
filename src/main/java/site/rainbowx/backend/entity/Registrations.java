package site.rainbowx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "registrations")
@IdClass(RegistrationId.class)
public class Registrations {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @Column(name = "activity_id", length = 20)
    private String activityId;

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
    @CollectionTable(name = "proof_files",
            joinColumns = {
                    @JoinColumn(name = "user_id"),
                    @JoinColumn(name = "activity_id")
            })
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
}

// 复合主键类
@Getter
@Setter
class RegistrationId implements Serializable {
    private String userId;
    private String activityId;
}