package com.example.instantastatusapp.utils;

import com.example.instantastatusapp.model.requestDTO.ForgetPasswordDto;
import com.example.instantastatusapp.service.AuthService;
import com.example.instantastatusapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Data
@Component
@RequiredArgsConstructor
public class AppConstant {
    private String Sender="nannimbinde@gmail.com";
//    private Integer tokenExpirationTime;


    public Date calculateExpirationDate(Integer tokenExpirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, tokenExpirationTime);
        return new Date(calendar.getTime().getTime());
    }

}
