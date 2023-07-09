package com.example.instantastatusapp.model.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
}
