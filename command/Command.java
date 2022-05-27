package ru.neverhook.command;

@FunctionalInterface
public interface Command {
    void execute(String... var1);
}
