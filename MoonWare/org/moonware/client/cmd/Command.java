package org.moonware.client.cmd;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}