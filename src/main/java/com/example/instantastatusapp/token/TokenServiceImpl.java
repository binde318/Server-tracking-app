package com.example.instantastatusapp.token;

import com.example.instantastatusapp.entity.ForgotPasswordToken;
import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.entity.UserInvitationToken;
import com.example.instantastatusapp.exception.CustomException;
import com.example.instantastatusapp.exception.ResourceCreationException;
import com.example.instantastatusapp.repository.ForgotPasswordRepo;
import com.example.instantastatusapp.repository.UserInvitationTokenRepository;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenServiceImpl implements TokenService{
    private final UserInvitationTokenRepository invitationTokenRepository;
    private final ForgotPasswordRepo forgotPasswordRepo;
    private final UserRepository userRepository;
    private final AppConstant appConstant;

    @Override
    public String generateInvitationToken(String email) {
        String token= UUID.randomUUID().toString();
        UserInvitationToken newToken= UserInvitationToken.builder()
                .expirationDate(appConstant.calculateExpirationDate(15))
                .token(token)
                .email(email)
                .build();
        invitationTokenRepository.save(newToken);
        return token;
    }

    @Override
    public UserInvitationToken verifyInvitationToken(String token) {
        UserInvitationToken existingToken= invitationTokenRepository.findByToken(token);
        if (existingToken==null){
            throw new CustomException("Invalid Token");
        }
        return existingToken;
    }


    @Override
    public String generatePasswordToken(String email) {
        User newUser=userRepository.findByEmail(email).orElseThrow();
        String token= UUID.randomUUID().toString();
        ForgotPasswordToken newToken= ForgotPasswordToken.builder()
                .expirationDate(appConstant.calculateExpirationDate(15*60000))
                .token(token)
                .user(newUser)
                .build();
        forgotPasswordRepo.save(newToken);
        return token;

    }

    @Override
    public Boolean verifyForgotPasswordToken(String token) {
        ForgotPasswordToken verifying= forgotPasswordRepo.findForgotPasswordTokenByToken(token);
        if(verifying==null){
            throw new CustomException("Wrong token");
        }

        Calendar cal = Calendar.getInstance();

        if ((verifying.getExpirationDate().getTime()
                -cal.getTime().getTime())<=0){
            log.info("TOKEN EXPIRED and deleted");
            forgotPasswordRepo.delete(verifying);
            throw new ResourceCreationException("Token Expired");

        }
        forgotPasswordRepo.delete(verifying);
        return true;
    }
}
