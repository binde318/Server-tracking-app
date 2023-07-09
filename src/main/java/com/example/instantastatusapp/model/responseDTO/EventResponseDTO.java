package com.example.instantastatusapp.model.responseDTO;

import com.example.instantastatusapp.entity.Incident;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;
    private String status;
    private Boolean state;
    private LocalDateTime time;
    private String componentName;
    private String type;
    private String componentGroup;

}
