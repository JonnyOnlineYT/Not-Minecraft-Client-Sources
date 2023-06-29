package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.feature.impl.misc.TeleportExploit;
import org.moonware.client.helpers.player.MovementHelper;

public class AutoSprint extends Feature {

    public AutoSprint() {
        super("AutoSprint", "Зажимает CTRL за вас, что бы быстро бежать", Type.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(MoonWare.featureManager.getFeatureByClass(TeleportExploit.class).getState())) {
            if (!(MoonWare.featureManager.getFeatureByClass(Scaffold.class).getState() && Scaffold.sprintoff.getBoolValue())) {
                if (!(MoonWare.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null)) {
                    Minecraft.player.setSprinting(MovementHelper.isMoving());
                }
            }
        }
    }
}
