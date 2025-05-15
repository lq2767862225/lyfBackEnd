package site.rainbowx.backend.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.dto.*;
import site.rainbowx.backend.entity.User;
import site.rainbowx.backend.exception.DuplicateEmailException;
import site.rainbowx.backend.exception.UserNotFoundException;
import site.rainbowx.backend.service.UserService;


import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {
        try {
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());
            newUser.setPhone(request.getPhone());
            newUser.setStudentId(request.getStudentId());
            newUser.setGender(request.getGender());
            newUser.setClass_name(request.getClassName());
            newUser.setDepartment(request.getDepartment());

            User createdUser = userService.registerUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("注册成功", convertToResponse(createdUser)));
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, e.getMessage()));
        }
    }

    /**
     * 获取当前用户信息
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                ApiResponse.success("获取成功", convertToResponse(currentUser))
        );
    }
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(
                    ApiResponse.success("查询成功", convertToResponse(user))
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        }
    }
    /**
     * 更新用户信息
     * PUT /api/users/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {

        if (!userId.equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无权修改其他用户信息"));
        }

        try {
            User updateData = new User();
            updateData.setNickname(request.getNickname());
            updateData.setPhone(request.getPhone());
            updateData.setAvatar(request.getAvatar());
            updateData.setDepartment(request.getDepartment());
            updateData.setGender(request.getGender());
            updateData.setClass_name(request.getClassName());

            User updatedUser = userService.updateUser(userId, updateData);
            return ResponseEntity.ok(
                    ApiResponse.success("更新成功", convertToResponse(updatedUser))
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        }
    }

    /**
     * 重置密码
     * PUT /api/users/{userId}/password
     */
    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordResetRequest request,
            @AuthenticationPrincipal User currentUser) {

        if (!userId.equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无权修改其他用户密码"));
        }

        try {
            userService.resetPassword(userId, request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("密码修改成功"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        }
    }

    /**
     * 删除用户（逻辑删除）
     * DELETE /api/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser) {

        if (!userId.equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无权删除其他用户"));
        }

        try {
            userService.softDeleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("账户已注销"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        }
    }

    /**
     * DTO转换方法
     */
    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .department(user.getDepartment())
                .studentId(user.getStudentId())
                .gender(user.getGender())
                .className(user.getClass_name())
                .serviceHours(user.getServiceHours())
                .creditScore(user.getCreditScore())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 统一API响应格式
     */
    static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;

        private ApiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return new ApiResponse<>(200, message, data);
        }

        public static ApiResponse<Void> success(String message) {
            return new ApiResponse<>(200, message, null);
        }

        public static <T> ApiResponse<T> error(int code, String message) {
            return new ApiResponse<>(code, message, null);
        }

        // Getters
        public int getCode() { return code; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }

    /**
     * DTO类定义
     */
    static class UserRegisterRequest {
        private String email;
        private String password;
        private Long phone;
        private String studentId;
        private String gender;
        @JsonProperty("className")
        private String className;
        private String department;

        public String getEmail() {
            return null;
        }

        public String getPassword() {
            return null;
        }

        public Long getPhone() {
            return null;
        }

        public String getStudentId() {
            return null;
        }

        public String getGender() {
            return null;
        }

        public String getClassName() {
            return null;
        }

        public String getDepartment() {
            return null;
        }

        // Getters and Setters
    }

    static class UserUpdateRequest {
        private String nickname;
        private Long phone;
        private String avatar;
        private String department;
        private String gender;
        @JsonProperty("className")
        private String className;

        public String getNickname() {
            return null;
        }

        public Long getPhone() {
            return null;
        }

        public String getAvatar() {
            return null;
        }

        public String getDepartment() {
            return null;
        }

        public String getGender() {
            return null;
        }

        public String getClassName() {
            return null;
        }

        // Getters and Setters
    }

    @lombok.Builder
    static class UserResponse {
        private Long id;
        private String email;
        private String nickname;
        private Long phone;
        private String avatar;
        private String department;
        private String studentId;
        private String gender;
        @JsonProperty("className")
        private String className;
        private String serviceHours;
        private Long creditScore;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;

        // Getters
    }

    static class PasswordResetRequest {
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String newPassword;

        public String getNewPassword() {
            return null;
        }

        // Getter and Setter
    }
}