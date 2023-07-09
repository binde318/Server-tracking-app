package com.example.instantastatusapp.service;

import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.model.requestDTO.ChangePasswordDTO;
import com.example.instantastatusapp.model.requestDTO.UserRoleRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    Optional<User> findUserByEmail(String email);

    Optional<User> getUserByPasswordResetToken(String token);


    String inviteUser(String email, HttpServletRequest request);

    String changePassword(ChangePasswordDTO changePasswordDTO);

    String changeUserRole(UserRoleRequestDTO userRoleRequestDTO);
}

