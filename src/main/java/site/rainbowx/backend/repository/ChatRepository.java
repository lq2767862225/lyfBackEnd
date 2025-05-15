package site.rainbowx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.rainbowx.backend.entity.Chat;  // 确保导入Chat实体
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {  // 正确泛型参数
    // 自定义查询方法
    List<Chat> findByType(String type);
}