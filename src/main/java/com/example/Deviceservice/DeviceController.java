package com.example.Deviceservice;

import com.example.Deviceservice.model.Device;
import com.example.Deviceservice.model.Event;
import com.example.Deviceservice.model.Project;
import com.example.Deviceservice.model.Type;
import com.example.Deviceservice.repositories.DeviceRepository;
import com.example.Deviceservice.repositories.EventRepository;
import com.example.Deviceservice.repositories.ProjectRepository;
import com.example.Deviceservice.response.ProjectInfoView;
import com.example.Deviceservice.response.ProjectSummaryView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity<String> addNewProject (@RequestParam String name) {
        Project p = new Project();
        p.setName(name);
        projectRepository.save(p);
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

    @PostMapping(path="/addDevice")
    public @ResponseBody ResponseEntity<String> addNewDevice (@RequestParam int projectId,@RequestParam String serialNumber) {
        Device d = new Device();
        d.setProject(projectRepository.findById(projectId));
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
        return new ResponseEntity<String>("Saved",HttpStatus.OK) ;
    }


    @GetMapping(path="/getProjectById")
    public @ResponseBody ResponseEntity<Object> getProjectById(@RequestParam int projectId) {
        Map<String, ProjectInfoView> response = new LinkedHashMap<>();
        Project p = projectRepository.findById(projectId);
        for (Device d: p.getDevices()) {
            int id = d.getId();
            String serialNumber = d.getSerial_number();
            boolean hasErrors =  !eventRepository.isErrorsById(id).isEmpty();
            int eventCount = eventRepository.countEventByDevice_IdAndType(id, Type.event);
            int warningCount = eventRepository.countEventByDevice_IdAndType(id, Type.warning);
            int errorCount = eventRepository.countEventByDevice_IdAndType(id, Type.error);

            ProjectInfoView projectInfoView = new ProjectInfoView(id,serialNumber, projectId, hasErrors);
            projectInfoView.setSummaryInfo(eventCount, warningCount, errorCount);

            response.put(serialNumber, projectInfoView);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping(path="/getAllProjects")
    public @ResponseBody ResponseEntity<Object> getAllProjects() {
        Set<ProjectSummaryView> response = new LinkedHashSet<>();
        Iterable<Project> p = projectRepository.findAll();
        Date currentDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
        for (Project project: p) {
            int id = project.getId();
            String projectName = project.getName();
            int deviceAmount = project.getDevices().size();
            int deviceWithErrors = eventRepository.countDevicesWithProblems(currentDate, project.getId()).size();
            int stableDevices = deviceAmount - eventRepository.countUnstableDevices(project.getId()).size();
            Set devices = deviceRepository.findSerialsNumbersById(project.getId());

            ProjectSummaryView oneProject = new ProjectSummaryView(id, projectName, devices);
            oneProject.setStats(deviceAmount, deviceWithErrors, stableDevices);

            response.add(oneProject);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
