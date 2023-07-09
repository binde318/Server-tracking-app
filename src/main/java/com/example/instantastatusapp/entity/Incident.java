package com.example.instantastatusapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean status;
    private String message;
    private String url;
    private String type;
    private String component;
    private String componentGroup;
    private  LocalDateTime resolvedTime;
    private Boolean visibility;
    private LocalDateTime createdTime;
}
