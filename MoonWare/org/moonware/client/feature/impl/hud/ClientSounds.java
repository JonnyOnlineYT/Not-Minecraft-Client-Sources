package org.moonware.client.feature.impl.hud;

import org.moonware.client.feature.impl.Type;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class ClientSounds extends Feature {
    public static ListSetting attacksounds = new ListSetting("Attack sound", "Skeet.cc", () -> true, "Skeet.cc", "NeverLose");
    public static NumberSetting value = new NumberSetting("volume", 3, 0, 10, 0.1F, () -> true);
    public ClientSounds() {
        super("ClientSounds", "Звуки при включении функции и выключении", Type.Hud);
        addSettings(attacksounds, value);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}
