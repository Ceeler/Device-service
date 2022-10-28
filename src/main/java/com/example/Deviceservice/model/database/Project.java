package com.example.Deviceservice.model.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "projects")

public class Project {

    public Project(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name = "project_seq", allocationSize = 1)
    private int id;

    private String name;
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "project")
    private List<Device> devices = new ArrayList<>();

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
