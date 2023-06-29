package net.minecraft.command;

public class CommandException extends Exception
{
    private final Object[] errorObjects;

    public CommandException(String message, Object... objects)
    {
        super(message);
        errorObjects = objects;
    }

    public Object[] getErrorObjects()
    {
        return errorObjects;
    }

    public synchronized Throwable fillInStackTrace()
    {
        return this;
    }
}
