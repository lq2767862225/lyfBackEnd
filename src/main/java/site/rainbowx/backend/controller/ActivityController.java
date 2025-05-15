package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.entity.Activity;
import site.rainbowx.backend.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // 创建活动
    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        Activity created = activityService.createActivity(activity);
        return ResponseEntity.ok(created);
    }

    // 更新活动
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        Activity updated = activityService.updateActivity(activity);
        return ResponseEntity.ok(updated);
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    // 根据ID查询活动
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activity);
    }

    // 查询所有活动
    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    // 分页查询活动
    @GetMapping("/page")
    public ResponseEntity<Page<Activity>> getActivitiesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(activityService.getActivitiesByPage(pageable));
    }

    // 模糊搜索活动名称
    @GetMapping("/search")
    public ResponseEntity<List<Activity>> searchActivitiesByName(@RequestParam String keyword) {
        return ResponseEntity.ok(activityService.searchActivitiesByName(keyword));
    }
}
