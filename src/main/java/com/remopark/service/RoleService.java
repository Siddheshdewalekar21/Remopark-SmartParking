package com.remopark.service;

import com.remopark.model.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String name);
    Role createRole(Role role);
    void deleteRole(Long id);
} 