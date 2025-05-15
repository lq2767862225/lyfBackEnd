package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.Role;
import site.rainbowx.backend.exception.NotFoundException;
import site.rainbowx.backend.repository.RoleRepository;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * 创建角色
     */
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * 更新角色
     */
    public Role updateRole(Long id, Role newRole) {
        Role existing = getRoleById(id);
        existing.setName(newRole.getName());
        existing.setDescription(newRole.getDescription());
        return roleRepository.save(existing);
    }

    /**
     * 删除角色
     */
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * 根据ID获取角色
     */
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("角色不存在 ID: " + id));
    }

    /**
     * 根据名称获取角色
     */
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("角色不存在: " + name));
    }

    /**
     * 获取所有角色
     */
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * 分页查询角色
     */
    @Transactional(readOnly = true)
    public Page<Role> getRolesByPage(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}