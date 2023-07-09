package com.example.instantastatusapp.model.requestDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String firstname;
    private String lastname;
    private String profileName;

    private String email;
    private String phoneNumber;
    private String password;

    public String getRoleName() {
        return profileName;
    }
}
