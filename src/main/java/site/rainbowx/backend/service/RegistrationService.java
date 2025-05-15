package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.Registrations;
import site.rainbowx.backend.exception.NotFoundException;
import site.rainbowx.backend.repository.RegistrationsRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationsRepository repository;

    @Autowired
    public RegistrationService(RegistrationsRepository repository) {
        this.repository = repository;
    }

    /**
     * 创建或更新报名记录
     */
    @Transactional
    public Registrations saveRegistration(Registrations registration) {
        return repository.save(registration);
    }

    /**
     * 根据复合主键查询
     */
    @Transactional(readOnly = true)
    public Registrations getRegistrationById(Long userId, Long activityId) {
        return repository.findById(new Registrations.RegistrationId(userId, activityId))
                .orElseThrow(() -> new NotFoundException("找不到报名记录: 用户ID=" + userId + ", 活动ID=" + activityId));
    }

    /**
     * 删除报名记录
     */
    @Transactional
    public void deleteRegistration(Long userId, Long activityId) {
        if (!repository.existsById(new Registrations.RegistrationId(userId, activityId))) {
            throw new NotFoundException("删除失败: 记录不存在");
        }
        repository.deleteById(new Registrations.RegistrationId(userId, activityId));
    }

    /**
     * 获取活动所有报名记录
     */
    @Transactional(readOnly = true)
    public List<Registrations> getRegistrationsByActivity(Long activityId) {
        return repository.findByActivityId(activityId);
    }

    /**
     * 更新签到状态
     */
    @Transactional
    public Registrations updateSignStatus(Long userId, Long activityId,
                                          Registrations.SignStatus newStatus) {
        Registrations reg = getRegistrationById(userId, activityId);
        reg.setSignStatus(newStatus);
        reg.setSignTime(LocalDateTime.now());
        return repository.save(reg);
    }
}