package com.example.Deviceservice.rest.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectsController {

    @Autowired ProjectsResponseBuilder projectsResponseBuilder;

    @PostMapping(path="/createProject")
    public @ResponseBody ResponseEntity<Object> addNewProject (@RequestParam String name) {
        return projectsResponseBuilder.processCreateProjectRequest(name);
    }

    @GetMapping(path="/getProjectInfoById")
    public @ResponseBody ResponseEntity<Object> getProjectById(@RequestParam int projectId) {
        return projectsResponseBuilder.processGetProjectInfoById(projectId);
    }

    @GetMapping(path="/getAllProjectsInfo")
    public @ResponseBody ResponseEntity<Object> getAllProjects() {
        return projectsResponseBuilder.processGetAllProjectsInfo();
    }

}
