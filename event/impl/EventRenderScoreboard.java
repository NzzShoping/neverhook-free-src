package ru.neverhook.event.impl;

import ru.neverhook.event.Event;
import ru.neverhook.event.types.EventType;

public class EventRenderScoreboard implements Event {

    EventType state;

    public EventRenderScoreboard(EventType state) {
        this.state = state;
    }

    public EventType getState() {
        return this.state;
    }

    public void setState(EventType state) {
        this.state = state;
    }

    public boolean isPre() {
        return this.state == EventType.PRE;
    }

}
