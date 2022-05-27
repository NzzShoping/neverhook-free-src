package ru.neverhook.event.impl;

import ru.neverhook.event.callables.EventCancellable;

public class EventChatMessage extends EventCancellable {

    public String message;
    public boolean cancelled;

    public EventChatMessage(String chat) {
        message = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
