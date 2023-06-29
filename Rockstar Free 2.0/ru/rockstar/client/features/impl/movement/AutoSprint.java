package ru.rockstar.client.features.impl.movement;

import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.player.Scaffold;

public class AutoSprint extends Feature {

    public AutoSprint() {
        super("AutoSprint", "Зажимает CTRL за вас, что бы быстро бежать",0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
            if (!(Main.instance.featureDirector.getFeatureByClass(Scaffold.class).isToggled() && Scaffold.sprintoff.getBoolValue())) {
                if (!(Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && KillAura.stopSprint.getBoolValue() && KillAura.target != null)) {
                    mc.player.setSprinting(MovementHelper.isMoving());
                }
        }
    }
}
