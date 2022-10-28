package com.example.Deviceservice.repositories;

import com.example.Deviceservice.model.database.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Deviceservice.model.database.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer>{

    @Query(value = "SELECT e FROM events e WHERE e.device.id = ?1 AND e.type = 'error'")
    List<Event> isErrorsById(Integer id);

    int countEventByDevice_IdAndType(int id, Type type);

}
