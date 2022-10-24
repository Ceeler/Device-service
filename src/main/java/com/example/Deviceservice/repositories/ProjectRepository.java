package com.example.Deviceservice.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.Deviceservice.model.Project;

import java.util.ArrayList;

public interface ProjectRepository extends CrudRepository<Project, Integer>{

    Project findById(int id);

    boolean existsByName(String name);
}
