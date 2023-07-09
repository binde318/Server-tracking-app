package com.example.instantastatusapp.service;

import com.example.instantastatusapp.entity.Event;
import com.example.instantastatusapp.model.requestDTO.EventRequestDTO;
import com.example.instantastatusapp.model.requestDTO.IncidentRequestDTO;
import com.example.instantastatusapp.model.requestDTO.ManualIncidentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.EventResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface EventService {
    EventResponseDTO troubleShootComponent(EventRequestDTO eventRequestDTO) throws IOException;

    List<EventResponseDTO> listEvent();

    EventResponseDTO viewEvent(Long id);

    void automatedEvent() throws IOException;

    List<EventResponseDTO> getEvents(Integer nopOfDays);

    List<EventResponseDTO> logIncident(ManualIncidentRequestDTO manualIncident);

    Event createIncident(IncidentRequestDTO incidentRequestDTO);

    Long totalIncident();

    Event edithIncident(IncidentRequestDTO incidentRequestDTO);

    Long totalIncident1(String type);

    List<EventResponseDTO> listEvents(String component, String componentGroup, String type, String status, Boolean visibility);
}
