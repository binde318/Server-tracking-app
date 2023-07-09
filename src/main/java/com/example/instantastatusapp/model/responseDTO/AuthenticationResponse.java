package com.example.instantastatusapp.model.responseDTO;

import com.example.instantastatusapp.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;
    private Integer id;
    private String firstname;
    private String lastname;
    private String userName;
    private String email;
//    private UserRole role;
    private String phoneNumber;

    private Set<UserRole> roles;

}
