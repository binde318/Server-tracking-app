package com.example.instantastatusapp.token;

import com.example.instantastatusapp.entity.UserInvitationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    String generateInvitationToken(String email);

    UserInvitationToken verifyInvitationToken(String token);

    String generatePasswordToken(String email);

    Boolean verifyForgotPasswordToken(String token);
}
