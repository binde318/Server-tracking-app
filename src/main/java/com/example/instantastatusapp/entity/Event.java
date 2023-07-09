package com.example.instantastatusapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private Boolean state;
    private String type;
    private String component;
    private String componentGroup;
    private String url;
    private Boolean visibility;
    private Integer portNumber;
    private String request;
    private String response;
    private LocalDateTime createdTime;
    private String message;
}
