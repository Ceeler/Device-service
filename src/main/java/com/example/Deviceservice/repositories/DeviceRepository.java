package com.example.Deviceservice.repositories;

import com.example.Deviceservice.model.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface DeviceRepository extends CrudRepository<Device, Integer> {
    Device findById(int id);

    @Query(value = "SELECT d.serial_number FROM devices d WHERE d.project.id = ?1")
    List<String> findSerialsNumbersById(int id);
}
