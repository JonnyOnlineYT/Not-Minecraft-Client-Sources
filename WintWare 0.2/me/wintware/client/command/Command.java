package me.wintware.client.command;

@FunctionalInterface
public interface Command {
   void execute(String... var1);
}
