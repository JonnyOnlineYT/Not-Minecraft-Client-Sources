package ru.rockstar.client.features.impl.movement;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class FastClimb extends Feature {

    public static ListSetting ladderMode;
    public static NumberSetting ladderSpeed;

    public FastClimb() {
        super("FastClimb", "ѕозвол€ет быстро забиратьс€ по лестницам и лианам",0, Category.PLAYER);
        ladderMode = new ListSetting("FastClimb Mode", "Matrix", () -> true, "Matrix", "Vanilla");
        ladderSpeed = new NumberSetting("Ladder Speed", 0.5F, 0.1F, 2F, 0.1F, () -> ladderMode.currentMode.equals("Vanilla"));
        addSettings(ladderMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        this.setSuffix(ladderMode.getCurrentMode(), true);
        if (mc.player == null || mc.world == null)
            return;
        switch (ladderMode.getOptions()) {
            case "Matrix":
                if (mc.player.isOnLadder() && mc.player.isCollidedHorizontally && MovementHelper.isMoving()) {
                    mc.player.motionY += 0.312f;
                    event.setGround(true);
                }
                break;
            case "Vanilla":
                if (mc.player.isOnLadder() && mc.player.isCollidedHorizontally && MovementHelper.isMoving()) {
                    mc.player.motionY += ladderSpeed.getNumberValue();
                }
                break;
        }
    }
}

