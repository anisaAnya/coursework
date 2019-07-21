package com.example.eurekaclientlab2;

import com.example.CatMessage;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MsgProducer {


    @Autowired
    @Qualifier("rabbitTemplateCat")
    private RabbitTemplate rabbitCat;

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);


    public void sendCatMsg(CatMessage msg)
    {
        try {
            LOGGER.debug("<<<<< SENDING MESSAGE");
            rabbitCat.convertAndSend(msg);
            LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitCat.getRoutingKey()));
        } catch (AmqpException e) {
            LOGGER.error("Error sending Shop: ",e);
        }
    }

    public ObjectNode info()
    {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("Cat UUID", rabbitCat.getUUID());
        root.put("queueCat", rabbitCat.getRoutingKey());

        return root;
    }
}
