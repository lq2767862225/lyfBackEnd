package site.rainbowx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.rainbowx.backend.entity.Announcement;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 根据标题模糊查询
    List<Announcement> findByTitleContaining(String title);

    // 查询重要公告
    List<Announcement> findByImportantTrue();
}