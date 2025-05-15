package site.rainbowx.backend.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "volunteer")
public class Volunteer {
    @Id
    @Column(name = "id") // 假设数据库列名是 id，类型为 BIGINT 或 VARCHAR
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 或 String id（根据数据库类型调整）
}
