package com.example.instantastatusapp.model.requestDTO;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String newPassword;
    private String confirmPassword;
}