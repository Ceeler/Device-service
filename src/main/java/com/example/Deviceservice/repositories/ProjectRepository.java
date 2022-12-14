package com.example.Deviceservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.Deviceservice.model.database.Project;

public interface ProjectRepository extends CrudRepository<Project, Integer>{

    Project findById(int id);

    boolean existsByName(String name);
}
