package com.example.instantastatusapp.controller;

import com.example.instantastatusapp.entity.Event;
import com.example.instantastatusapp.model.requestDTO.EventRequestDTO;
import com.example.instantastatusapp.model.requestDTO.IncidentRequestDTO;
import com.example.instantastatusapp.model.requestDTO.ManualIncidentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.EventResponseDTO;
import com.example.instantastatusapp.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
//@SecurityRequirement(
//        name = "Bearer Authentication"
//)
//@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/v1/Event")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    @PostMapping("/createEvent")
    public ResponseEntity<?> troubleShootComponent(@RequestBody EventRequestDTO eventRequestDTO) throws IOException {
        EventResponseDTO responseDTO = eventService.troubleShootComponent(eventRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/listEvents")
    public ResponseEntity<List<EventResponseDTO>> listEvents() {
        List<EventResponseDTO> response = eventService.listEvent();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewEvent/{id}")
    public ResponseEntity<?> viewEvent(@PathVariable("id") Long id) {
        EventResponseDTO response = eventService.viewEvent(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/componentEvents/{nopOfDays}")
    public ResponseEntity<List<EventResponseDTO>> getEvents(@PathVariable("nopOfDays") Integer nopOfDays) {
        List<EventResponseDTO> listOfEvent = eventService.getEvents(nopOfDays);
        return ResponseEntity.ok(listOfEvent);
    }

    @GetMapping("/log-incident")
    public ResponseEntity<List<EventResponseDTO>> logIncident(@RequestBody ManualIncidentRequestDTO manualIncident) {
        List<EventResponseDTO> incident = eventService.logIncident(manualIncident);
        return ResponseEntity.ok(incident);

    }

    @PostMapping("/create-incident")
    public ResponseEntity<Event> createIncident(@RequestBody IncidentRequestDTO incidentRequestDTO) {
        Event incident = eventService.createIncident(incidentRequestDTO);
        return ResponseEntity.ok(incident);
    }

    @PutMapping("/nice")
    public ResponseEntity<?> edithIncident(IncidentRequestDTO incidentRequestDTO) {
        Event incident = eventService.edithIncident(incidentRequestDTO);
        return ResponseEntity.ok(incident);
    }

    @GetMapping("/{types}")
    public ResponseEntity<Long> totalIncident(@PathVariable("type") String type) {
        Long totalIncident = eventService.totalIncident();
        return ResponseEntity.ok(totalIncident);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Long> totalIncident1(@PathVariable("type") String type) {
        Long totalIncident = eventService.totalIncident1(type);
        return ResponseEntity.ok(totalIncident);
    }

    @GetMapping("/event_list")
    public ResponseEntity<?> listEvent(
            @RequestParam(value = "component",required = false) String component,
            @RequestParam(value = "componentGroup",required = false) String componentGroup,
            @RequestParam(value = "type",required = false) String type,
            @RequestParam(value = "status",required = false) String status,
            @RequestParam(value = "visibility",required = false) Boolean visibility){
        List<EventResponseDTO> event= eventService.listEvents(component,componentGroup,type,status,visibility);
        return ResponseEntity.ok(event);
    }

}
