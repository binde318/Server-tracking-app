package com.example.instantastatusapp.email;

public interface EmailSender {

//    void send(String to, String email,String body);

//    void sendEmail(String invitationUrl, String email);
    void sendEmail(EmailDto emailDto);
}
