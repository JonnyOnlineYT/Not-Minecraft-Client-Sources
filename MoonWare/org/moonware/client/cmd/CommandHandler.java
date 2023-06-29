package org.moonware.client.cmd;

import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventMessage;

public class CommandHandler {

    public CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public void onMessage(EventMessage event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(commandManager.execute(msg));
        }
    }
}
