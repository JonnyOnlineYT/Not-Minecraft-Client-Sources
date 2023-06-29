package org.moonware.client.feature.impl.misc;

import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventMessage;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class ChatAppend extends Feature {

    public ChatAppend() {
        super("ChatAppend", "Дописывает название чита в сообщение", Type.Other);
    }

    @EventTarget
    public void onChatMessage(EventMessage event) {
        if (event.getMessage().startsWith("/"))
            return;

        event.message = event.message + " | " + "MoonWare";
    }
}
