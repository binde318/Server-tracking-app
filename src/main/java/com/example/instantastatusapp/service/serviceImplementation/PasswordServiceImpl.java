package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.PasswordResetToken;
import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.exception.CustomException;
import com.example.instantastatusapp.model.requestDTO.ResetPasswordDTO;
import com.example.instantastatusapp.repository.PasswordResetTokenRepository;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.service.PasswordService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String passwordResetTokenMail(User user, String applicationUrl, String token) {
//        String url = applicationUrl + "/savePassword?token=" + token;


        String url = applicationUrl +
                "?token="
                + token;
//        String url = "http://localhost:3000/reset-password/" + token;

//        http://127.0.0.1:5173/resetpassword

        log.info("Click the link to reset your password: {}", url);

        return url;
    }

    @Override
    public String applicationUrl(HttpServletRequest request) {
        String frontendServerName = "127.0.0.1";
        int frontendServerPort = 5173;
        String frontendContextPath = "/resetpassword";
        //return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String frontendUrl = "http://" + frontendServerName + ":" + frontendServerPort + frontendContextPath;

        String backendUrl = request.getRequestURL().toString();
        String backendContextPath = request.getContextPath();

        String frontendRelativePath = backendUrl.replace(backendContextPath, "");

        return frontendUrl;
    }

    @Override
    public void sendEmail(String recipientEmail, String link)
            throws UnsupportedEncodingException, MessagingException {
        String senderEmail = "gr8erkay@gmail.com";

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(senderEmail, "Instanta Status App");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);

        };
        try {
            CompletableFuture.runAsync(() ->
                    mailSender.send(mimeMessagePreparator)).exceptionally(exp -> {
                throw new CustomException("Exception occurred sending mail [message]: " + exp.getLocalizedMessage());
            });
            log.info("email has been sent!!");
        }catch (MailException exception) {
            log.error("Exception occurred when sending mail {}",exception.getMessage());
            throw new CustomException("Exception occurred when sending mail to " + recipientEmail, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = Optional.of(passwordResetTokenRepository.findByToken(token).orElseThrow());

        if(passwordResetToken.isEmpty()) {
            return "invalid token";
        }

        User user = passwordResetToken.get().getUser();
        Calendar calender = Calendar.getInstance();

        if(passwordResetToken.get().getDate().getTime() - calender.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken.get());
            return "Expired";
        }
        return "valid";
    }

    /**
     * @param user
     * @param passwordDto
     */

    @Override
    public void changePassword(User user, ResetPasswordDTO passwordDto) {
        if (passwordDto.getNewPassword().equals((passwordDto.getConfirmPassword()))) {
            user.setPassword((passwordEncoder.encode(passwordDto.getNewPassword())));
            userRepository.save(user);
        }
       else { throw new CustomException("Password does not match");}
    }
}