package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.Incident;
import com.example.instantastatusapp.repository.IncidentRepo;
import com.example.instantastatusapp.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepo incidentRepo;
    @Override
    public Incident creatIncident(Component component) {
        Incident incident= Incident.builder()
//                .time(LocalDateTime.now())
                .status(false)
                .message("service is down")
                .build();
        return incidentRepo.save(incident);
    }
}
