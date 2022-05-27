package ru.neverhook.event;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean state);

}
