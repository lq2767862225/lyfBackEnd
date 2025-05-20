package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.Announcement;
import site.rainbowx.backend.exception.NotFoundException;
import site.rainbowx.backend.repository.AnnouncementRepository;

import java.util.List;

@Service
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    /**
     * 创建公告
     */
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    /**
     * 更新公告
     */
    public Announcement updateAnnouncement(Announcement announcement) {
        if (!announcementRepository.existsById(announcement.getId())) {
            throw new NotFoundException("公告不存在 ID: " + announcement.getId());
        }
        return announcementRepository.save(announcement);
    }

    /**
     * 删除公告
     */
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    /**
     * 根据ID获取公告详情
     */
    @Transactional(readOnly = true)
    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("公告不存在 ID: " + id));
    }

    /**
     * 获取所有公告列表
     */
    @Transactional(readOnly = true)
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    /**
     * 分页查询公告
     */
    @Transactional(readOnly = true)
    public Page<Announcement> getAnnouncementsByPage(Pageable pageable) {
        return announcementRepository.findAll(pageable);
    }

    /**
     * 根据标题搜索公告
     */
    @Transactional(readOnly = true)
    public List<Announcement> searchAnnouncementsByTitle(String title) {
        return announcementRepository.findByTitleContaining(title);
    }

    /**
     * 获取重要公告
     */
    @Transactional(readOnly = true)
    public List<Announcement> getImportantAnnouncements() {
        return announcementRepository.findByImportantTrue();
    }
}