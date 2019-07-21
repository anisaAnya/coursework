package com.example.listeners.impl;

import com.example.CatMessage;
import com.example.listeners.RabbitListener;
import com.example.listeners.events.EventsPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerStudent implements RabbitListener<CatMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerStudent.class);

    @Autowired
    private EventsPublisher publisher;

    @Override
    public void receiveMessage(CatMessage message) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            LOGGER.debug("Receive Message Cat: \n"+json);

            publisher.publishStudent(message);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error: ", e);
        }

    }



}
