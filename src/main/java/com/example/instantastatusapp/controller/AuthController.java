package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.model.requestDTO.AuthenticationRequest;
import com.example.instantastatusapp.model.requestDTO.UserRequestDTO;
import com.example.instantastatusapp.model.responseDTO.AppResponse;
import com.example.instantastatusapp.model.responseDTO.AuthenticationResponse;
import com.example.instantastatusapp.repository.UserRepository;
import com.example.instantastatusapp.service.AuthService;
import com.example.instantastatusapp.token.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "User Authentication Endpoints"
)
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final UserRepository userRepository;


    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/register")
    public ResponseEntity<?> createAccount(

            @RequestBody UserRequestDTO registerRequest) {
      log.info("creating");
        return ResponseEntity.ok(AppResponse.buildSuccessTxn(authService.createAccount(registerRequest)));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
     log.info("authentication");
        return ResponseEntity.ok(authService.authenticate(authenticationRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/logout")
        public ResponseEntity<AppResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return ResponseEntity.ok(AppResponse.buildSuccess("redirect:/login"));

}

}

