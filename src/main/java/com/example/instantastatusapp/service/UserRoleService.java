package com.example.instantastatusapp.service;

import com.example.instantastatusapp.entity.UserRole;
import com.example.instantastatusapp.model.requestDTO.UserRoleRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserRoleService {
    List<UserRole> getAllRoles();
    UserRole addRole(UserRoleRequestDTO roleName);

    void deleteRole(Long id);
    Optional<UserRole> findUserRoleByRoleName(String roleName);

}

