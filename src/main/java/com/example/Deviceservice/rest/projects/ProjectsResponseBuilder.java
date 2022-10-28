package com.example.Deviceservice.rest.projects;

import com.example.Deviceservice.model.database.Device;
import com.example.Deviceservice.model.database.Event;
import com.example.Deviceservice.model.database.Project;
import com.example.Deviceservice.model.database.Type;
import com.example.Deviceservice.model.response.UIAllProjectDevices;
import com.example.Deviceservice.model.response.UIGetAllProjectsInfo;
import com.example.Deviceservice.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class ProjectsResponseBuilder {

    @Autowired private ProjectRepository projectRepository;

    public ResponseEntity<Object> processCreateProjectRequest(String name){

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

    public ResponseEntity<Object> processGetProjectInfoById(int projectId){

        Project p = projectRepository.findById(projectId);
        Map<String, UIAllProjectDevices> response = new LinkedHashMap<>();

        if(p==null){
            return new ResponseEntity<>("Project with this id not found", HttpStatus.NOT_FOUND);
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

    public ResponseEntity<Object> processGetAllProjectsInfo(){

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
