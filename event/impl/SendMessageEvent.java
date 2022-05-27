package ru.neverhook.event.impl;

import ru.neverhook.event.Event;
import ru.neverhook.event.callables.EventCancellable;

public class SendMessageEvent extends EventCancellable implements Event {

    public String message;

    public SendMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getNewMessage() {
        return newMessage;
    }

}
