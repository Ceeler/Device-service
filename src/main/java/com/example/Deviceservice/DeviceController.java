package com.example.Deviceservice;

import com.example.Deviceservice.model.Device;
import com.example.Deviceservice.model.Event;
import com.example.Deviceservice.model.Project;
import com.example.Deviceservice.model.Type;
import com.example.Deviceservice.repositories.DeviceRepository;
import com.example.Deviceservice.repositories.EventRepository;
import com.example.Deviceservice.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public @ResponseBody String addNewProject (@RequestParam String name) {
        Project p = new Project();
        p.setName(name);
        projectRepository.save(p);
        return "Saved";
    }

    @PostMapping(path="/addDevice")
    public @ResponseBody String addNewDevice (@RequestParam int projectId,@RequestParam String serialNumber) {
        Device d = new Device();
        d.setProject(projectRepository.findById(projectId));
        d.setSerial_number(serialNumber);
        deviceRepository.save(d);
        return "Saved";
    }

    @PostMapping(path="/addEvent")
    public @ResponseBody String addNewEvent (@RequestParam Type type, @RequestParam boolean isRead, @RequestParam int deviceId) {
        Event e = new Event();
        e.setType(type);
        e.setIs_read(isRead);
        e.setDevice(deviceRepository.findById(deviceId));
        eventRepository.save(e);
        return "Saved";
    }


    @GetMapping(path="/getProjectById")
    public @ResponseBody Map<String, Object> getProjectById(@RequestParam int projectId) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Project p = projectRepository.findById(projectId);
        for (Device d: p.getDevices()) {
            int deviceId = d.getId();
            map.put(d.getSerial_number(),new LinkedHashMap<>(){{
                put( "id",deviceId);
                put("serialNumber",d.getSerial_number());
                put("projectId", d.getProject().getId());
                put("hasErrors", !eventRepository.isErrorsById(deviceId).isEmpty());
                put("summaryInfo",new LinkedHashMap<>(){{
                    put("eventCount", eventRepository.countEventByDevice_IdAndType(deviceId, Type.event));
                    put("warningCount", eventRepository.countEventByDevice_IdAndType(deviceId, Type.warning));
                    put( "errorCount", eventRepository.countEventByDevice_IdAndType(deviceId, Type.error));
                }});
            }});
        }
        return map;
    }

    @GetMapping(path="/getAllProjects")
    public @ResponseBody Iterable<Object> getAllProjects() {
        Set<Object> set = new LinkedHashSet<>();
        Iterable<Project> p = projectRepository.findAll();
        Date currentDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
        for (Project project: p) {
            int deviceAmount = project.getDevices().size();
            set.add(new LinkedHashMap<>(){{
                put("id", project.getId());
                put("projectName",project.getName());
                put("stats",new LinkedHashMap<>(){{
                    put( "deviceCount", deviceAmount);
                    put("deviceWithErrors",eventRepository.countDevicesWithProblems(currentDate, project.getId()).size());
                    put("stableDevices",deviceAmount - eventRepository.countUnstableDevices(project.getId()).size());
                }});
                put("devices", deviceRepository.findSerialsNumbersById(project.getId()));
            }});
        }
        return set;
    }

}
