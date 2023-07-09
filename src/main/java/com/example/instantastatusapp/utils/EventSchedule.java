package com.example.instantastatusapp.utils;

import com.example.instantastatusapp.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSchedule {
    private final EventService eventService;
    @Scheduled(fixedRate = 600000L)
//@Scheduled(fixedRate = 10000L)
    public void automatedEvent() throws IOException {
        eventService.automatedEvent();
        log.info("Event scheduled");

    }
}
