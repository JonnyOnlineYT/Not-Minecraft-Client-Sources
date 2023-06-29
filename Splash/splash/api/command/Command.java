package splash.api.command;

import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 16:50, 06-Jun-20
 * Project: Client
 */
public abstract class Command {

    private String commandName;
    private String[] commandArguments;

    public Command(String commandName) {
       this.commandName = commandName;
    }

    public abstract String usage();

    public void printUsage() {
        ClientLogger.printToMinecraft(this.usage());
    }

    public abstract void executeCommand(String[] commandArguments);

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String[] getCommandArguments() {
        return commandArguments;
    }

    public void setCommandArguments(String[] commandArguments) {
        this.commandArguments = commandArguments;
    }
}
