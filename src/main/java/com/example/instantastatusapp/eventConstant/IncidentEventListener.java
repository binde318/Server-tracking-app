package com.example.instantastatusapp.eventConstant;

import com.example.instantastatusapp.entity.Incident;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventListener {
    @Async
    @EventListener
    void createIncident(Incident incident){


    }
}
