package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.entity.Role;
import site.rainbowx.backend.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 创建角色
     */
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        Role updated = roleService.updateRole(id, role);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取所有角色
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * 根据名称获取角色
     */
    @GetMapping("/by-name")
    public ResponseEntity<Role> getRoleByName(@RequestParam String name) {
        Role role = roleService.getRoleByName(name);
        return ResponseEntity.ok(role);
    }

    /**
     * 分页查询角色
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Role>> getRolesByPage(Pageable pageable) {
        Page<Role> roles = roleService.getRolesByPage(pageable);
        return ResponseEntity.ok(roles);
    }
}
