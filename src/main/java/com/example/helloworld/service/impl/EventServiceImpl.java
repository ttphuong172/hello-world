package com.example.helloworld.service.impl;

import com.example.helloworld.model.Event;
import com.example.helloworld.repository.EventRepository;
import com.example.helloworld.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
