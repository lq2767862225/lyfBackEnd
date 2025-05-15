package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.User;
import site.rainbowx.backend.exception.DuplicateEmailException;
import site.rainbowx.backend.exception.UserNotFoundException;
import site.rainbowx.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户注册（自动加密密码）
     */
    public User registerUser(User user) {
        // 验证邮箱唯一性
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException("邮箱已被注册: " + user.getEmail());
        }

        // 设置初始值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setDeleted(false);

        return userRepository.save(user);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long userId, User updateData) {
        User existing = getUserById(userId);

        // 逐个字段更新
        if (updateData.getNickname() != null) existing.setNickname(updateData.getNickname());
        if (updateData.getPhone() != null) existing.setPhone(updateData.getPhone());
        if (updateData.getAvatar() != null) existing.setAvatar(updateData.getAvatar());
        if (updateData.getDepartment() != null) existing.setDepartment(updateData.getDepartment());
        if (updateData.getStudentId() != null) existing.setStudentId(updateData.getStudentId());
        if (updateData.getGender() != null) existing.setGender(updateData.getGender());
        if (updateData.getClass_name() != null) existing.setClass_name(updateData.getClass_name());

        return userRepository.save(existing);
    }

    /**
     * 根据ID获取用户
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在 ID: " + userId));
    }

    /**
     * Spring Security 用户加载方法
     */
    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + email));
    }

    /**
     * 重置密码
     */
    public void resetPassword(Long userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 更新登录信息
     */
    public void updateLoginInfo(Long userId, LocalDateTime loginTime) {
        User user = getUserById(userId);
        user.setLastLoginAt(loginTime);
        userRepository.save(user);
    }

    /**
     * 逻辑删除用户
     */
    public void softDeleteUser(Long userId) {
        User user = getUserById(userId);
        user.setDeleted(true);
        userRepository.save(user);
    }
}