package com.example.instantastatusapp.service;


import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.model.requestDTO.ResetPasswordDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface PasswordService {
    void createPasswordResetTokenForUser(User user, String token);
    String passwordResetTokenMail(User user, String applicationUrl, String token);
    String applicationUrl(HttpServletRequest request);
    void sendEmail(String recipientEmail, String link)
            throws UnsupportedEncodingException, MessagingException;
    String validatePasswordResetToken(String token);
    void changePassword(User user, ResetPasswordDTO passwordDto);
}