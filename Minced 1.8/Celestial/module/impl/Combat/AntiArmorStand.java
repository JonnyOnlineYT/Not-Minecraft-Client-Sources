package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import net.minecraft.entity.Entity;

public class AntiArmorStand extends Module {
    public AntiArmorStand() {
        super("AntiArmorStand", "Автоматически удаляет все армор-стенды с мира", ModuleCategory.Combat);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Helper.mc.player == null || Helper.mc.world == null) {
            return;
        }
        for (Entity entity : Helper.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof net.minecraft.entity.item.EntityArmorStand))
                continue;
            Helper.mc.world.removeEntity(entity);
        }
    }
}
