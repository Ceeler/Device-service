package com.example.Deviceservice.repositories;

import com.example.Deviceservice.model.Device;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public interface DeviceRepository extends CrudRepository<Device, Integer> {
    Device findById(int id);


    @Query(value = "SELECT d.serial_number FROM devices d WHERE d.project.id = ?1")
    Set<String> findSerialsNumbersById(int id);

    //@EntityGraph(attributePaths = {"project"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT d FROM devices d LEFT JOIN d.events e  WHERE d.project.id = ?1 GROUP BY d.id")
    List<Object> findByIdProved(int id);


}
