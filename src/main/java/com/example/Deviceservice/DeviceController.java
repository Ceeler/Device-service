package com.example.Deviceservice;

import com.example.Deviceservice.model.Device;
import com.example.Deviceservice.model.Event;
import com.example.Deviceservice.model.Project;
import com.example.Deviceservice.model.Type;
import com.example.Deviceservice.repositories.DeviceRepository;
import com.example.Deviceservice.repositories.EventRepository;
import com.example.Deviceservice.repositories.ProjectRepository;
import com.example.Deviceservice.response.UIAllProjectDevices;
import com.example.Deviceservice.response.UIGetAllProjectsInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Controller

public class DeviceController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private EventRepository eventRepository;

    @PostMapping(path="/addProject")
    public @ResponseBody ResponseEntity<String> addNewProject (@RequestParam String name) {
        if(name.equals("")){
            return new ResponseEntity<>("Name can't be empty", HttpStatus.BAD_REQUEST);
        }
        if(projectRepository.existsByName(name)){
            return new ResponseEntity<>("Name already added", HttpStatus.BAD_REQUEST);
        }
        Project p = new Project();
        p.setName(name);
        projectRepository.save(p);
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

    @PostMapping(path="/addDevice")
    public @ResponseBody ResponseEntity<String> addNewDevice (@RequestParam int projectId,@RequestParam String serialNumber) {
        Project p = projectRepository.findById(projectId);
        if(p == null){
            return new ResponseEntity<>("Project id Not Found", HttpStatus.NOT_FOUND);
        }
        if(serialNumber.equals("")){
            return new ResponseEntity<>("Serial number can't be Empty", HttpStatus.BAD_REQUEST);
        }
        Device d = new Device();
        d.setProject(p);
        d.setSerial_number(serialNumber);
        deviceRepository.save(d);
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

    @PostMapping(path="/addEvent")
    public @ResponseBody ResponseEntity<String> addNewEvent (@RequestParam Type type, @RequestParam boolean isRead, @RequestParam int deviceId) {

        Event e = new Event();
        e.setType(type);
        e.setIs_read(isRead);
        e.setDevice(deviceRepository.findById(deviceId));
        eventRepository.save(e);
        return new ResponseEntity<>("Saved",HttpStatus.OK) ;
    }


    @GetMapping(path="/getAllProjectDevices")
    public @ResponseBody ResponseEntity<Object> getProjectById(@RequestParam int projectId) {

        Project p = projectRepository.findById(projectId);
        Map<String, UIAllProjectDevices> response = new LinkedHashMap<>();

        if(p==null){
            return new ResponseEntity<>("Project not fount", HttpStatus.NOT_FOUND);
        }

        for (Device d: p.getDevices()) {
            int id = d.getId();
            String serialNumber = d.getSerial_number();
            boolean hasErrors = false;
            int eventCount = 0;
            int warningCount = 0;
            int errorCount = 0;

            for (Event e: d.getEvents()) {
                Type eventType = e.getType();
                if(eventType == Type.event){
                    eventCount++;
                }else if(eventType == Type.warning){
                    warningCount++;
                } else if(eventType == Type.error) {
                    errorCount++;
                }
            }
            if(errorCount > 0){
                hasErrors = true;
            }

            UIAllProjectDevices uiAllProjectDevices = new UIAllProjectDevices(id,serialNumber, projectId, hasErrors);
            uiAllProjectDevices.setSummaryInfo(eventCount, warningCount, errorCount);

            response.put(serialNumber, uiAllProjectDevices);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/getAllProjectsInfo")
    public @ResponseBody ResponseEntity<Object> getAllProjects() {

        Set<UIGetAllProjectsInfo> response = new LinkedHashSet<>();
        Iterable<Project> p = projectRepository.findAll();
        Date currentDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));

        for (Project project: p) {
            int id = project.getId();
            String projectName = project.getName();
            int deviceAmount = project.getDevices().size();
            int deviceWithErrors = 0;
            int stableDevices = 0;
            List<String> devices = new LinkedList<>();

            for(Device d: project.getDevices()){
                boolean isStable = true;
                for (Event e: d.getEvents()){
                    if((e.getType() == Type.error || e.getType() == Type.warning) && e.getDate().after(currentDate)){
                        deviceWithErrors ++;
                        break;
                    }else if((e.getType() == Type.error || e.getType() == Type.warning) && isStable){
                        isStable = false;
                    }
                }
                if(isStable){
                    stableDevices++;
                }
                devices.add(d.getSerial_number());
            }


            UIGetAllProjectsInfo oneProject = new UIGetAllProjectsInfo(id, projectName, devices);
            oneProject.setStats(deviceAmount, deviceWithErrors, stableDevices);

            response.add(oneProject);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
