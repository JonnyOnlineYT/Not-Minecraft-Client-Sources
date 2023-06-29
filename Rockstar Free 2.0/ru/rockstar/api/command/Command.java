package ru.rockstar.api.command;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}