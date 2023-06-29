package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class FastClimb extends Feature {

    public static ListSetting ladderMode;
    public static NumberSetting ladderSpeed;

    public FastClimb() {
        super("FastClimb", "Позволяет быстро забираться по лестницам и лианам", Type.Movement);
        ladderMode = new ListSetting("FastClimb Mode", "Matrix", () -> true, "Matrix", "Vanilla");
        ladderSpeed = new NumberSetting("Ladder Speed", 0.5F, 0.1F, 2F, 0.1F, () -> ladderMode.currentMode.equals("Vanilla"));
        addSettings(ladderMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        setSuffix(ladderMode.getCurrentMode());
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        switch (ladderMode.getOptions()) {
            case "Matrix":
                if (Minecraft.player.isOnLadder() && Minecraft.player.isCollidedHorizontally && MovementHelper.isMoving()) {
                    Minecraft.player.motionY += 0.312f;
                    event.setOnGround(true);
                }
                break;
            case "Vanilla":
                if (Minecraft.player.isOnLadder() && Minecraft.player.isCollidedHorizontally && MovementHelper.isMoving()) {
                    Minecraft.player.motionY += ladderSpeed.getNumberValue();
                }
                break;
        }
    }
}

