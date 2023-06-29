package ru.rockstar.client.features.impl.player;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class SlowFall extends Feature {

    public static NumberSetting speed;
    private final TimerHelper timerHelper = new TimerHelper();

    public SlowFall() {
        super("SlowFall", "Вы падаете медленнее", 0,Category.PLAYER);
        speed = new NumberSetting("Speed", 0.2f, 0.1f, 0.9f, 0.1F, () -> true);
        addSettings(speed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if(mc.player.fallDistance > 1f) {
        	mc.timer.timerSpeed = speed.getNumberValue();
        } else {
        	mc.timer.timerSpeed = 1;
        }
    }
}
