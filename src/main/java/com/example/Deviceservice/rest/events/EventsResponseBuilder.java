package com.example.Deviceservice.rest.events;

import com.example.Deviceservice.model.database.Device;
import com.example.Deviceservice.model.database.Event;
import com.example.Deviceservice.model.database.Type;
import com.example.Deviceservice.repositories.DeviceRepository;
import com.example.Deviceservice.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class EventsResponseBuilder {
    @Autowired private EventRepository eventRepository;
    @Autowired private DeviceRepository deviceRepository;
    public ResponseEntity<Object> processCreateEventRequest(String type, String isRead, int deviceId){
        Type enumType;
        boolean booleanIsRead = false;
        isRead = isRead.toLowerCase(Locale.ROOT);

        try {
            enumType = Type.valueOf(type);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>("Invalid event type", HttpStatus.BAD_REQUEST);
        }
        if(isRead.equals("true") || isRead.equals("false")){
            if(isRead.equals("true")){
                booleanIsRead = true;
            }
        }else{
            return new ResponseEntity<>("Invalid boolean type", HttpStatus.BAD_REQUEST);
        }

        Device d = deviceRepository.findById(deviceId);
        if(d == null){
            return new ResponseEntity<>("Device with provided id not exist", HttpStatus.BAD_REQUEST);
        }
        Event e = new Event();
        e.setType(enumType);
        e.setIs_read(booleanIsRead);
        e.setDevice(d);
        eventRepository.save(e);
        return new ResponseEntity<>("Saved", HttpStatus.OK) ;

    }
}
