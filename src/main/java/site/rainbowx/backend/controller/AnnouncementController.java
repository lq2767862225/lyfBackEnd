package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.entity.Announcement;
import site.rainbowx.backend.service.AnnouncementService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // 创建公告
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now()); // 自动设置创建时间
        Announcement created = announcementService.createAnnouncement(announcement);
        return ResponseEntity.ok(created);
    }

    // 更新公告
    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable Long id,
                                                           @RequestBody Announcement announcement) {
        announcement.setId(id);
        Announcement updated = announcementService.updateAnnouncement(announcement);
        return ResponseEntity.ok(updated);
    }

    // 删除公告
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    // 根据ID获取公告
    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        return ResponseEntity.ok(announcement);
    }

    // 获取所有公告
    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    // 分页查询公告
    @GetMapping("/page")
    public ResponseEntity<Page<Announcement>> getAnnouncementsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(announcementService.getAnnouncementsByPage(pageable));
    }

    // 根据标题搜索公告
    @GetMapping("/search")
    public ResponseEntity<List<Announcement>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(announcementService.searchAnnouncementsByTitle(title));
    }

    // 获取重要公告
    @GetMapping("/important")
    public ResponseEntity<List<Announcement>> getImportantAnnouncements() {
        return ResponseEntity.ok(announcementService.getImportantAnnouncements());
    }
}
