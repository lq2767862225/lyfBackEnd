package site.rainbowx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.rainbowx.backend.entity.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> { // ✅ 正确泛型参数
    // 根据角色名称查询（自动实现）
    Optional<Role> findByName(String name);
}