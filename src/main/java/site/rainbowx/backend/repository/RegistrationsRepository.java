package site.rainbowx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.rainbowx.backend.entity.Registrations;
import java.util.List;

public interface RegistrationsRepository
        extends JpaRepository<Registrations, Registrations.RegistrationId> {

    // 自定义查询方法示例
    List<Registrations> findByActivityId(Long activityId);
}