package com.example.instantastatusapp.model.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualIncidentRequestDTO {
    private String url;
    private Integer portNumber;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

}
