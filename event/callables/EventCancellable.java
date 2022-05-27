package ru.neverhook.event.callables;

import ru.neverhook.event.Cancellable;
import ru.neverhook.event.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    protected EventCancellable() {
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
