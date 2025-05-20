package site.rainbowx.backend.entity;

import lombok.*;

import java.util.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "is_deleted")    // 是否已经被软删除
    private Boolean isDeleted;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;//用户名

    @Column(name = "nickname", length = 255)
    private String nickname;

    @Column(name = "fullName", length = 255)
    private String fullName;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "activity_participation",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id")
    )
    private List<User> participats;
    @Column(name = "status", length = 20)
    private String status;
    @Column(name = "username", length = 20)
    private String username;
    @Column(name = "userid", length = 20)
    private Long userid;
    @Column(name = "role", length = 20)
    private String role;
    @Column(name = "avatar", length = 20)
    private String avatar;
    @Column(name = "phone", length = 20)
    private Long phone;
    @Column(name = "department", length = 20)
    private String department;
    @Column(name = "studentId", length = 20)
    private String studentId;
    @Column(name = "creditScore", length = 20)
    private Long creditScore;
    @Column(name = "firstLogin", length = 20)
    private String firstLogin;
    @Column(name = "gender", length = 20)
    private String gender;
    @Column(name = "class", length = 20)
    private String class_name;
    @Column(name = "grade", length = 20)
    private String grade;
    @Column(name = "serviceHours", length = 20)
    private String serviceHours;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", columnDefinition = "varbinary(32)") // 考虑到现在IP占用最大的是ipv6, 占16个字节, 32字节已经足够
    private byte[] lastLoginIp;

    @PrePersist  // 在插入数据前设置创建时间
    protected void onCreate() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate  // 在更新数据前设置更新时间
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // overrides
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return ! isDeleted;
    }

    public void setDeleted(boolean b) {
    }
}
