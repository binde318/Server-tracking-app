package com.example.instantastatusapp.service;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.Incident;
import org.springframework.stereotype.Service;

@Service
public interface IncidentService {
    Incident creatIncident(Component component);
}
