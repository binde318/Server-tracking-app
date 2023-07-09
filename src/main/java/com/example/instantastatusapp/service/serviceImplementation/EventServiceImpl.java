package com.example.instantastatusapp.service.serviceImplementation;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.Event;
import com.example.instantastatusapp.exception.ResourceNotFoundException;
import com.example.instantastatusapp.model.requestDTO.EventRequestDTO;
import com.example.instantastatusapp.model.requestDTO.IncidentRequestDTO;
import com.example.instantastatusapp.model.requestDTO.ManualIncidentRequestDTO;
import com.example.instantastatusapp.model.responseDTO.EventResponseDTO;
import com.example.instantastatusapp.repository.ComponentRepository;
import com.example.instantastatusapp.repository.EventRepo;
import com.example.instantastatusapp.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final ComponentRepository componentRepository;
    private final EventRepo eventRepo;

    @Override
    public EventResponseDTO troubleShootComponent(EventRequestDTO eventRequestDTO) {
        String url = eventRequestDTO.getUrl();
        Integer port= eventRequestDTO.getPort();

        Component component = eventRequestDTO.getUrl()!=null?componentRepository.findComponentByUrl(url)
                :componentRepository.findComponentByPortNumber(port);
        Event event;
        Component newComponent = new Component();
        if(component!=null){
            event= component.getType().equals("Service")?serviceType(component):clientType(component);
        }else {
            newComponent.setName(eventRequestDTO.getName() + "(New Component)");
            newComponent.setUrl(eventRequestDTO.getUrl());
            newComponent.setPortNumber(eventRequestDTO.getPort());
             event= eventRequestDTO.getType().equals("Service")?serviceType(newComponent):clientType(newComponent);
        }

        return mapEventResponse(event);
    }

    @Override
    public List<EventResponseDTO> listEvent() {
        List<Event> response = eventRepo.findAll();
        return  response.stream()
                .map(resp ->
                        EventResponseDTO.builder()
                                .componentName(resp.getComponent())
                                .status(resp.getStatus())
                                .state(resp.getState())
                                .time(resp.getCreatedTime())
                                .componentGroup(resp.getComponentGroup())
                                .build())
                .toList();
    }

    @Override
    public EventResponseDTO viewEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("event does not exist"));
        return EventResponseDTO.builder()
                .componentGroup(event.getComponentGroup())
                .componentName(event.getComponent())
                .time(event.getCreatedTime())
                .state(event.getState())
                .status(event.getStatus())
                .build();
    }

    @Override
    public void automatedEvent() {
        log.info("automated event call  {}", LocalDateTime.now());
        List<Component> components = componentRepository.findAll();
        log.info("{} list components", components);
        for (Component c : components) {
            Event event= c.getType().equals("Service")?serviceType(c):clientType(c);
            saveEvent(event);
        }

    }

    private Optional<Event>saveEvent(Event event) {
        Optional<Event> e= eventRepo.findOne(Example.of(event));
        if(e.isEmpty()) eventRepo.save(event);
        return e;
    }

    private Event serviceType(Component c){
        log.info("Service");
        Boolean isConnected;
        String url = c.getUrl();
        Event event;
        Integer portNumber = c.getPortNumber();
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(url, portNumber), 5000); // timeout in milliseconds
            isConnected = socket.isConnected();
            socket.close();
            event = getEvent(isConnected, c);
            log.info("{}", isConnected);
        } catch (IOException e) {
            log.info("Invalid urls");
            event = Event.builder()
                    .type("Unknown Host")
                    .status("Invalid Url")
                    .component(c.getName())
                    .url(c.getUrl())
                    .componentGroup(c.getGroup().getName())
                    .state(false)
                    .portNumber(c.getPortNumber())
                    .createdTime(LocalDateTime.now())
                    .build();
        }
        return event;
    }

    private Event clientType(Component c){
        log.info("client");
        Event event;
        String url = c.getUrl();
        RestTemplate restTemplate=new RestTemplate();
        try {
            restTemplate.getForEntity(url,String.class);
            event=getEvent(true,c);
        }
        catch (HttpClientErrorException e){
            event = Event.builder()
                    .type("CLIENT")
                    .status("Not Reachable")
                    .component(c.getName())
                    .url(c.getUrl())
                    .componentGroup(c.getGroup().getName())
                    .state(false)
                    .portNumber(c.getPortNumber())
                    .createdTime(LocalDateTime.now())
                    .build();
        }
        return  event;
    }

    @Override
    public List<EventResponseDTO> getEvents(Integer nopOfDays) {

        Long noOfComponents= componentRepository.count();
        Integer dataPerPage = ((nopOfDays*24*60)/10);

        PageRequest pageable= PageRequest.of(0,dataPerPage);
        Page<Event> eventPage= eventRepo.findAllByOrderByCreatedTimeDesc(pageable);
        log.info("{}",eventPage);
        return eventPage
                .stream()
                .map(resp->mapEventResponse(resp))
                .toList();
    }

    @Override
    public List<EventResponseDTO> logIncident(ManualIncidentRequestDTO manualIncident) {
        LocalDateTime time=LocalDateTime.of(manualIncident.getYear(),manualIncident.getMonth(),manualIncident.getDay()
                ,manualIncident.getHour(), manualIncident.getMinute());

        LocalDateTime startTime= time.minusMinutes(15);
        LocalDateTime endTime=time.plusMinutes(15);
        List<Event> event = eventRepo.findAllByUrlAndPortNumberAndCreatedTimeBetween(manualIncident.getUrl()
                ,manualIncident.getPortNumber(),startTime,endTime);;

        if(event==null) throw new ResourceNotFoundException("NO Incident at this time");
        List<EventResponseDTO> response= event.stream()

                .filter(filteredEvent ->filteredEvent.getType().equals("Event") )
                .map(mapedEvent->mapEventResponse(mapedEvent))
                .toList();

        return response;
    }

    @Override
    public Event createIncident(IncidentRequestDTO incidentRequestDTO) {
        Event event=Event.builder()
                .component(incidentRequestDTO.getComponentName())
                .componentGroup(incidentRequestDTO.getComponentGroup())
                .url(incidentRequestDTO.getUrl())
                .portNumber(incidentRequestDTO.getPortNumber())
                .state(false)
                .status(incidentRequestDTO.getStatus())
                .type(incidentRequestDTO.getType())
                .visibility(incidentRequestDTO.getVisibility())
                .createdTime(LocalDateTime.now())
                .message(incidentRequestDTO.getMessage())
//                .resolvedTime(null)
                .build();
        return eventRepo.save(event);
    }

    @Override
    public Long totalIncident() {
        return eventRepo.countEventByTypeEqualsIgnoreCase("Incident");
    }

    @Override
    public Event edithIncident(IncidentRequestDTO incidentRequestDTO) {
        List<Event> eventList= eventRepo.findAllByUrlAndTypeEquals(incidentRequestDTO.getUrl(),incidentRequestDTO.getType());
        List<Event> event= eventRepo.saveAll( eventList.stream()
                 .map(c->Event.builder()
                         .portNumber(incidentRequestDTO.getPortNumber())
                         .build())
                 .toList());
//        return event.stream().map(resp->mapEventResponse(resp));
        return null;
    }

    @Override
    public Long totalIncident1(String type) {
        return eventRepo.countAllByType(type);
    }

    @Override
    public List<EventResponseDTO> listEvents(String component, String componentGroup, String type, String status, Boolean visibility) {
        List<Event> listEvent= eventRepo.findAll();
        return listEvent.stream()
                .filter(resp->resp.getComponent().equals(component))
                .filter(resp->resp.getComponentGroup().equalsIgnoreCase(componentGroup))

                .map(resp->mapEventResponse(resp))
                .toList();

    }

    private Event getEvent(Boolean isConnected, Component component) {
        Event event = Event.builder()
                .state(isConnected ? true : false)
                .status(isConnected ?"Operational" : "Not Operational")
                .type(isConnected?"Event":"Incident")
                .url(component.getUrl())
                .component(component.getName())
                .portNumber(component.getPortNumber())
                .componentGroup(component.getGroup().getName())
                .createdTime(LocalDateTime.now())
                .build();
        return eventRepo.save(event);

    }


    private EventResponseDTO mapEventResponse(Event event){
        log.info("DATE CHECKING");
        return  EventResponseDTO.builder()
                .id(event.getId())
                .state(event.getState())
                .type(event.getType())
                .componentName(event.getComponent())
                .status(event.getStatus())
                .time(event.getCreatedTime())
                .componentGroup(event.getComponentGroup())
                .build();
    }


}