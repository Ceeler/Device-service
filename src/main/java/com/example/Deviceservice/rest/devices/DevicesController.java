package com.example.Deviceservice.rest.devices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DevicesController {

    @Autowired
    private DevicesResponseBuilder devicesResponseBuilder;

    @PostMapping(path="/createDevice")
    public @ResponseBody ResponseEntity<Object> addNewDevice (@RequestParam int projectId, @RequestParam String serialNumber) {
        return devicesResponseBuilder.processCreateDeviceRequest(projectId, serialNumber);
    }

}
