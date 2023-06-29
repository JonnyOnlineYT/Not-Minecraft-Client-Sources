package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender
{
    /** RCon string buffer for log. */
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RConConsoleSource(MinecraftServer serverIn)
    {
        server = serverIn;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "Rcon";
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component)
    {
        buffer.append(component.asString());
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return server.getEntityWorld();
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback()
    {
        return true;
    }

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return server;
    }
}
