package com.example.instantastatusapp.service;

import com.example.instantastatusapp.model.requestDTO.AuthenticationRequest;
import com.example.instantastatusapp.model.requestDTO.ForgetPasswordDto;
import com.example.instantastatusapp.model.requestDTO.ResetPasswordDTO;
import com.example.instantastatusapp.model.requestDTO.UserRequestDTO;
import com.example.instantastatusapp.model.responseDTO.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {
    String forgotPassword(ForgetPasswordDto forgetPasswordDto, HttpServletRequest request);

    String resetPassword(ResetPasswordDTO resetPasswordDTO);

    Object createAccount(UserRequestDTO registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
