package com.remopark.service.impl;

import com.remopark.model.Role;
import com.remopark.model.RoleType;
import com.remopark.repository.RoleRepository;
import com.remopark.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleByName(String roleName) {
        try {
            RoleType roleType = RoleType.valueOf(roleName.toUpperCase());
            return roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
} 