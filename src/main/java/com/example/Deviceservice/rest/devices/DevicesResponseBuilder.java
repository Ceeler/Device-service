package com.example.Deviceservice.rest.devices;

import com.example.Deviceservice.model.database.Device;
import com.example.Deviceservice.model.database.Project;
import com.example.Deviceservice.repositories.DeviceRepository;
import com.example.Deviceservice.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DevicesResponseBuilder {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public ResponseEntity<Object> processCreateDeviceRequest(int projectId, String serialNumber){

        Project p = projectRepository.findById(projectId);
        if(p == null){
            return new ResponseEntity<>("Project with provided id not exist", HttpStatus.BAD_REQUEST);
        }
        if(serialNumber.equals("")){
            return new ResponseEntity<>("Empty Serial number", HttpStatus.BAD_REQUEST);
        }

        Device d = new Device();
        d.setProject(p);
        d.setSerial_number(serialNumber);
        deviceRepository.save(d);
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }
}
