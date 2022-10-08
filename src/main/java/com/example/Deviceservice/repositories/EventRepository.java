package com.example.Deviceservice.repositories;

import com.example.Deviceservice.model.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Deviceservice.model.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer>{

    @Query(value = "SELECT e FROM events e WHERE e.device.id = ?1 AND e.type = 'error'")
    List<Event> isErrorsById(Integer id);

    int countEventByDevice_IdAndType(int id, Type type);

    @Query(value = "SELECT e.device.id FROM events e WHERE e.type IN('error','warning') AND e.device.project.id = ?1  GROUP BY e.device.id")
    List<Integer> countUnstableDevices(int id);

    @Query(value = "SELECT e.device.id FROM events e WHERE e.date > ?1 AND (e.type = 'error' OR e.type='warning') AND e.device.project.id = ?2 GROUP BY e.device.id")
    List<Integer> countDevicesWithProblems(Date date, int id);
}
