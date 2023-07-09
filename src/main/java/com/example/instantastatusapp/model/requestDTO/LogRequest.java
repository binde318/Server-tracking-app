package com.example.instantastatusapp.model.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {
    private String getUrl;
    private int portNumber;
    private String username;
    private String password;
    private String filepath;
}
