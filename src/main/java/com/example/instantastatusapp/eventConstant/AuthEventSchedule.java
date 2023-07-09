package com.example.instantastatusapp.eventConstant;

import com.example.instantastatusapp.controller.EventController;
import lombok.RequiredArgsConstructor;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEventSchedule {
    private final EventController eventController;
    @Scheduled(fixedRate = 5000L)
    void eventSchedule() {
    }

}
//      (CloseableHttpClient client = HttpClients.createDefault();
//              {
//              HttpPost request = new HttpPost(TRACKING_URL);
//              request.setHeader("Content-Type", "application/json");
//              request.setEntity(new StringEntity(eventJson));
//
//              CloseableHttpResponse response = client.execute(request);
//
//              }
