package com.example.instantastatusapp.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    @NotBlank
    private String subject;

    @NotBlank
    @Email
    private String recipient;

    @NotBlank
    private String body;

    @NotBlank
    private String sender="gr8erkay@gmailcom";


    private String attachment;
    private String cc;
    private String bcc;


}