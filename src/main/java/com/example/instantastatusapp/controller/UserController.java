package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.entity.PasswordResetToken;
import com.example.instantastatusapp.entity.User;
import com.example.instantastatusapp.entity.UserInvitationToken;
import com.example.instantastatusapp.exception.CustomException;
import com.example.instantastatusapp.model.requestDTO.*;
import com.example.instantastatusapp.model.responseDTO.AppResponse;
import com.example.instantastatusapp.model.responseDTO.UserResponseDTO;
import com.example.instantastatusapp.repository.PasswordResetTokenRepository;
import com.example.instantastatusapp.repository.UserInvitationTokenRepository;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.service.AuthenticationService;
import com.example.instantastatusapp.service.UserService;
import com.example.instantastatusapp.service.serviceImplementation.PasswordServiceImpl;
import com.example.instantastatusapp.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("api/v1/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "User Profile Endpoints"
)
public class UserController {

    private final UserService userService;
    private final PasswordServiceImpl passwordService;
    private final TokenService tokenService;
    private final AuthenticationService authenticationService;
    private final UserInvitationTokenRepository invitationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/forget-password")
    public ResponseEntity<AppResponse<?>> forgetPassword(@RequestBody ForgetPasswordDto passwordDTO,
                                                         HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        Optional<User> user = userService.findUserByEmail(passwordDTO.getEmail());
        log.info("{}",user);
        String url = "";
        if(user.isPresent()) {
            String token = UUID.randomUUID().toString();
            passwordService.createPasswordResetTokenForUser(user.get(), token);
            url = passwordService.passwordResetTokenMail(user.get(), passwordService.applicationUrl(request), token);
        } else {
            log.info("user missing");
            throw new CustomException("User not found");
        }
        log.info("Sending a reset password link to {}", passwordDTO.getEmail());
        log.info(url);
        passwordService.sendEmail(passwordDTO.getEmail(), url);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/forget_password").toUriString());
        return ResponseEntity.created(uri).body(AppResponse.buildSuccess(url));
    }

    @Operation(
            summary = "Reset Password link",
            description = "Enter your mail here to reset to your password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password Reset Successful, login with new password"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<AppResponse<?>> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordDTO passwordDto) {
        String result = passwordService.validatePasswordResetToken(token);

        if(!result.equalsIgnoreCase("valid")) {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/reset_password").toUriString());
            return ResponseEntity.created(uri).body(AppResponse.buildSuccess("Invalid Token"));
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()) {
            passwordService.changePassword(user.get(), passwordDto);
           PasswordResetToken existingToken= passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new CustomException("Token does not exist"));
           passwordResetTokenRepository.delete(existingToken);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/reset_password").toUriString());

            //http://127.0.0.1:5173/resetpassword

            return ResponseEntity.created(uri).body(AppResponse.buildSuccess("Password reset successful"));
        } else {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/reset_password").toUriString());
            return ResponseEntity.created(uri).body(AppResponse.buildSuccess("Invalid Token"));
        }
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/invite-user")
    public ResponseEntity<AppResponse<String>> invite(@RequestBody UserInvitationDTO userInvitationDT, HttpServletRequest request){
        String response= userService.inviteUser(userInvitationDT.getEmail(),request);
        return ResponseEntity.ok(AppResponse.buildSuccess("User has Been invited"));
    }

    @PostMapping("/verify-invitationToken")
    public ResponseEntity<AppResponse> verifyToken(@RequestParam("token") String token,@RequestBody UserRequestDTO requestDTO){
        UserInvitationToken sentToken= tokenService.verifyInvitationToken(token);

        UserResponseDTO userResponseDTO= authenticationService.createAccount(requestDTO);
        invitationTokenRepository.delete(sentToken);
        return ResponseEntity.ok(AppResponse.buildSuccess(userResponseDTO));
    }


    @PostMapping("/change-user-password")
    public ResponseEntity<AppResponse<String>> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){

      String str=  userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(AppResponse.buildSuccess("login with your new password"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/change-user-role")
    public ResponseEntity<AppResponse<String>> changeUserRole(@RequestBody UserRoleRequestDTO userRoleRequestDTO) {
        userService.changeUserRole(userRoleRequestDTO);
        return ResponseEntity.ok(AppResponse.buildSuccess("User role updated successfully"));
    }
}
