package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.email.EmailDto;
import com.example.instantastatusapp.email.EmailSender;
import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.entity.UserRole;
import com.example.instantastatusapp.exception.CustomException;
import com.example.instantastatusapp.exception.ResourceNotFoundException;
import com.example.instantastatusapp.model.requestDTO.ChangePasswordDTO;
import com.example.instantastatusapp.model.requestDTO.UserRoleRequestDTO;
import com.example.instantastatusapp.repository.PasswordResetTokenRepository;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.repository.UserRoleRepository;
import com.example.instantastatusapp.service.UserRoleService;
import com.example.instantastatusapp.service.UserService;
import com.example.instantastatusapp.token.TokenService;
import com.example.instantastatusapp.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final static String USER_NOT_FOUND_MSG = "User with email: %s not found.";
    private final UserRepository userRepository;
    private final TokenService tokenService;
   private final EmailSender emailSender;
   private final PasswordEncoder passwordEncoder;
   private final AppConstant appConstant;
   private final UserRoleRepository userRoleRepository;
   private final UserRoleService userRoleService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }



    @Override
    public Optional<User> findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {

        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).get().getUser());

    }

    @Override
    public String inviteUser(String email, HttpServletRequest request) {
        String invitationToken= tokenService.generateInvitationToken(email);
        String invitationUrl= generateInvitationUrl(applicationUrl(request),invitationToken);
        log.info(invitationUrl);
        EmailDto invitationMail= new EmailDto();
        invitationMail.setSender(appConstant.getSender());
        invitationMail.setSubject("");
        invitationMail.setRecipient(email);
        invitationMail.setBody(invitationUrl);

        emailSender.sendEmail(invitationMail);
        log.info(invitationUrl);

        return "";
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO) {

            String userEmail= getUserEmail();
            User user= userRepository.findByEmail(userEmail).orElseThrow();
            log.info("find user");
            log.info("{}",user);
        if(!changePasswordDTO.getConfirmPassword().equals(changePasswordDTO.getNewPassword())){
            throw new CustomException("confirm password");
        }

            if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(),user.getPassword())){
            throw new CustomException("Wrong password");
        }

            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
        log.info("{}",user);

        return "Password Updated successfully";
    }

    /**
     * @param userRoleRequestDTO
     * @return
     */
    @Override
    public String changeUserRole(UserRoleRequestDTO userRoleRequestDTO) {
        if (userRoleRequestDTO == null) {
            throw new IllegalArgumentException("New role cannot be blank");
        }
        User user = userRepository.findByEmail(userRoleRequestDTO.getEmail()).orElseThrow(
                ()-> new ResourceNotFoundException("User not found with email " + userRoleRequestDTO.getEmail()));
        userRoleExists(user, userRoleRepository, userRoleRequestDTO.getRoleName(), userRoleService, log, userRoleRequestDTO);

        return "User role updated successfully";

    }

    public static void userRoleExists(User user, UserRoleRepository userRoleRepository, String roleName, UserRoleService userRoleService, Logger log, UserRoleRequestDTO userRoleRequestDTO) {
        Optional<UserRole> existingRole = userRoleRepository.findByRoleName(roleName);
        if (existingRole.isPresent()) {
            user.setUserRole(Collections.singleton(existingRole.get()));
        } else {
            UserRoleRequestDTO userRoleDTO = new UserRoleRequestDTO(roleName);
            UserRole userRole = userRoleService.addRole(userRoleDTO);
            log.info("Created new UserRole: {}", userRole.toString());
            user.setUserRole(Collections.singleton(userRole));
        }
    }

    private String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){

        }
        return authentication.getName();
    }


    private String generateInvitationUrl(String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/api/v1/user/verify-invitationToken?token="
                        + token;

        log.info("Click the link to Reset your Password: {}",url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
