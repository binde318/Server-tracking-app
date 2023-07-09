package com.example.instantastatusapp.model.responseDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String firstname;
    private String lastname;
    private String profileName;
    private String email;
    private String phoneNumber;
}
