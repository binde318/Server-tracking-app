package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.UserRole;
import com.example.instantastatusapp.model.requestDTO.UserRoleRequestDTO;
import com.example.instantastatusapp.repository.UserRoleRepository;
import com.example.instantastatusapp.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImplementation implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    private final UserRoleRequestDTO userRoleRequestDTO;

    private List<UserRole> roles = new ArrayList<>();

    /**
     * @return
     */
    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole addRole(UserRoleRequestDTO userRoleRequestDTO) {
        UserRole role = new UserRole(userRoleRequestDTO.getRoleName());
        return userRoleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        userRoleRepository.deleteById(id);
    }

    /**
     * @param roleName
     * @return
     */
    @Override
    public Optional<UserRole> findUserRoleByRoleName(String roleName) {
        return userRoleRepository.findByRoleName(roleName);
    }

}
