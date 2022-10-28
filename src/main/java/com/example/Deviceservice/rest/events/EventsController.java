package com.example.Deviceservice.rest.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EventsController{
    @Autowired EventsResponseBuilder eventsResponseBuilder;

    @PostMapping(path="/createEvent")
    public @ResponseBody ResponseEntity<Object> addNewEvent (@RequestParam String type, @RequestParam String isRead, @RequestParam int deviceId) {
        return eventsResponseBuilder.processCreateEventRequest(type, isRead, deviceId);
    }

}
