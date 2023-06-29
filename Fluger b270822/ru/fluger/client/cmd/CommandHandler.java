/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd;

import ru.fluger.client.cmd.CommandManager;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventMessage;

public class CommandHandler {
    public CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public void onMessage(EventMessage event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}

