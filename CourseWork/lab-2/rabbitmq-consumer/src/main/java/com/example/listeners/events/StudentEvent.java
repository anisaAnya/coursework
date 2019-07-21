package com.example.listeners.events;

import com.example.CatMessage;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class StudentEvent extends ApplicationEvent {

    private CatMessage msg;

    public StudentEvent(Object source, CatMessage msg) {
        super(source);
        this.msg = msg;
    }

    public CatMessage getMessage() {
        return msg;
    }

}
