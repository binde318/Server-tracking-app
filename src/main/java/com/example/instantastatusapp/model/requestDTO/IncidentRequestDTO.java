package com.example.instantastatusapp.model.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentRequestDTO {
    private String componentName;
    private String componentGroup;
    private String url;
    private Integer portNumber;
    private LocalDateTime createdTime;
    private String message;
    private String type;
    private Boolean visibility;
    private String status;

}
