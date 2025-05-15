package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.Activity;

import java.util.List;
import site.rainbowx.backend.exception.NotFoundException;

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * 创建活动
     */
    @Transactional
    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    /**
     * 更新活动
     */
    @Transactional
    public Activity updateActivity(Activity activity) {
        // 确保活动存在
        if (!activityRepository.existsById(activity.getId())) {
            throw new NotFoundException("活动不存在 ID: " + activity.getId());
        }
        return activityRepository.save(activity);
    }

    /**
     * 删除活动
     */
    @Transactional
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    /**
     * 根据ID获取活动详情
     */
    @Transactional(readOnly = true)
    public Page<Activity> searchActivitiesByName(String name, Pageable pageable) {
        return activityRepository.findByTitleContainingIgnoreCase(name);
    }

    public List<Activity> findByType(Activity.ActivityType activityType) {
        return null;
    }

    public interface ActivityRepository extends JpaRepository<Activity, Long> {
        // 添加分页查询方法
        Page<Activity> findByTitleContainingIgnoreCase(String title);
    }
    /**
     * 获取所有活动列表
     */
    @Transactional(readOnly = true)
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /**
     * 分页查询活动
     */
    @Transactional(readOnly = true)
    public Page<Activity> getActivitiesByPage(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }

    /**
     * 根据名称模糊查询活动
     */
    @Transactional(readOnly = true)
    public List<Activity> searchActivitiesByName(String name) {
        return (List<Activity>) activityRepository.findByTitleContainingIgnoreCase(name);
    }

    public Activity getActivityById(Long id) {
        return null;
    }
}