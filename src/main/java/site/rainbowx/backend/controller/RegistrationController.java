package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.entity.Registrations;
import site.rainbowx.backend.service.RegistrationService;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // 创建或更新报名记录
    @PostMapping
    public ResponseEntity<Registrations> saveRegistration(@RequestBody Registrations registration) {
        Registrations saved = registrationService.saveRegistration(registration);
        return ResponseEntity.ok(saved);
    }

    // 获取报名详情
    @GetMapping("/{userId}/{activityId}")
    public ResponseEntity<Registrations> getRegistrationById(@PathVariable Long userId,
                                                             @PathVariable Long activityId) {
        Registrations registration = registrationService.getRegistrationById(userId, activityId);
        return ResponseEntity.ok(registration);
    }

    // 删除报名记录
    @DeleteMapping("/{userId}/{activityId}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long userId,
                                                   @PathVariable Long activityId) {
        registrationService.deleteRegistration(userId, activityId);
        return ResponseEntity.noContent().build();
    }

    // 获取某活动的所有报名记录
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Registrations>> getRegistrationsByActivity(@PathVariable Long activityId) {
        List<Registrations> registrations = registrationService.getRegistrationsByActivity(activityId);
        return ResponseEntity.ok(registrations);
    }

    // 更新签到状态
    @PutMapping("/{userId}/{activityId}/sign-status")
    public ResponseEntity<Registrations> updateSignStatus(@PathVariable Long userId,
                                                          @PathVariable Long activityId,
                                                          @RequestParam Registrations.SignStatus newStatus) {
        Registrations updated = registrationService.updateSignStatus(userId, activityId, newStatus);
        return ResponseEntity.ok(updated);
    }
}
