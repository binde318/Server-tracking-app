package com.example.instantastatusapp.service;


import com.example.instantastatusapp.email.EmailDto;
import com.example.instantastatusapp.email.EmailSender;
import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.entity.UserRole;
import com.example.instantastatusapp.exception.ResourceCreationException;
import com.example.instantastatusapp.model.requestDTO.*;
import com.example.instantastatusapp.model.responseDTO.AuthenticationResponse;
import com.example.instantastatusapp.model.responseDTO.UserResponseDTO;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.repository.UserRoleRepository;
import com.example.instantastatusapp.service.serviceImplementation.UserServiceImplementation;
import com.example.instantastatusapp.token.TokenService;
import com.example.instantastatusapp.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService implements AuthService{
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    private final UserRoleService userRoleService;

    private final UserRoleRequestDTO userRoleRequestDTO;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailValidator emailValidator;
    private final TokenService tokenService;
    private final AppConstant appConstant;
    private final EmailSender emailSender;


    @Override
    public UserResponseDTO createAccount(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        boolean isValidEmail = emailValidator.test(userRequestDTO.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email is not valid");
        }
        if (!isPasswordValid(userRequestDTO.getPassword())) {
            throw new IllegalStateException("Password is not valid");
        }
        Optional<User> existingUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new ResourceCreationException("User with email already exists");
        }
        Optional<User> existingUser2 = userRepository.findByProfileName(userRequestDTO.getProfileName());
        if (existingUser2.isPresent()) {
            throw new ResourceCreationException("User with profileName already exists");
        }

        // Set the role on the user object
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setProfileName(userRequestDTO.getProfileName());
        user.setFirstname(userRequestDTO.getFirstname());
        user.setLastname(userRequestDTO.getLastname());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());

        if (userRepository.findAll().isEmpty()) {
            UserRoleRequestDTO userAdminRoleDTO = new UserRoleRequestDTO("ADMIN");
            UserRole userAdminRole = userRoleService.addRole(userAdminRoleDTO);
            user.setUserRole(Collections.singleton(userAdminRole));
        } else {
            if (userRequestDTO.getRoleName().isEmpty()) {
                UserRoleRequestDTO userAdminRoleDTO = new UserRoleRequestDTO("USER");
                UserRole userAdminRole = userRoleService.addRole(userAdminRoleDTO);
                user.setUserRole(Collections.singleton(userAdminRole));
            } else {
                UserServiceImplementation.userRoleExists(user, userRoleRepository, userRequestDTO.getRoleName(), userRoleService, log, userRoleRequestDTO);
            }
        }

        User newUser = userRepository.save(user);

        return UserResponseDTO.builder()
                .firstname(newUser.getFirstname())
                .lastname(newUser.getLastname())
                .profileName(newUser.getProfileName())
                .email(newUser.getEmail())
                .phoneNumber(newUser.getPhoneNumber())
                .profileName(newUser.getUserRole().iterator().next().getRoleName())
                .build();
    }

    public static boolean isPasswordValid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));

        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        log.info("User token: {}", jwtToken);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .userName(user.getProfileName())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .roles(user.getUserRole())
                .token(jwtToken)
                .build();
    }

    @Override
    public String forgotPassword(ForgetPasswordDto forgetPasswordDto, HttpServletRequest request) {

        User user= userRepository.findByEmail(forgetPasswordDto.getEmail()).get();
        if(user==null){
            return "No User with email " + forgetPasswordDto.getEmail() + " found";
        }
        String email= forgetPasswordDto.getEmail();
        String token= tokenService.generatePasswordToken(email);
        String forgotPasswordUrl=generateForgotPasswordTokenUrl(applicationUrl(request),token);
        EmailDto passwordEmail= EmailDto.builder()
                .sender(appConstant.getSender())
                .subject("Here's the link to reset your password")
                .recipient(email)
                .body(forgotPasswordUrl)
                .build();
        emailSender.sendEmail(passwordEmail);
         return "Check you mail to reset ur password";


    }

    @Override
    public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user= userRepository.findByEmail(resetPasswordDTO.getEmail()).orElseThrow();
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return "password reset successful";
    }

    private String generateForgotPasswordTokenUrl(String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/api/v1/user/reset-password?token="
                        + token;

        log.info("Click the link to Reset your Password: {}",url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+
                request.getServerName()+
                ":"+
                request.getServerPort()+
                request.getContextPath();
    }

}
