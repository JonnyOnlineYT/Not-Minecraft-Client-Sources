package de.strafe.command;

public abstract class Command {
    private final String name;
    private final String alias;
    private boolean toggle;


    public Command(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }
    public abstract void onCommand();
}
