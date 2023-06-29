package org.moonware.client.feature.impl.player;

import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventWebSolid;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class SolidWeb extends Feature {

    public SolidWeb() {
        super("SolidWeb", "Делает паутину полноценным блоком", Type.Other);
    }

    @EventTarget
    public void onWebSolid(EventWebSolid event) {
        event.setCancelled(true);
    }
}
