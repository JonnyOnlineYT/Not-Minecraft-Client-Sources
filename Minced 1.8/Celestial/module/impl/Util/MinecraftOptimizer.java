package Celestial.module.impl.Util;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.otherutils.gayutil.EventLight;
import Celestial.ui.settings.impl.BooleanSetting;
import net.minecraft.world.EnumSkyBlock;

public class MinecraftOptimizer extends Module {
    public static BooleanSetting entity;
    public MinecraftOptimizer() {
        super("MinecraftOptimizer", "", ModuleCategory.Util);
        entity = new BooleanSetting("del entity", false, () -> true);
        addSettings(entity);
    }
    public BooleanSetting light = new BooleanSetting("Light", true);
    public BooleanSetting entities = new BooleanSetting("Entities", true);

    @EventTarget
    public void onWorldLight(EventLight event) {
        if (light.getCurrentValue()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.cancel();
            }
            if (event.getEnumSkyBlock() == EnumSkyBlock.BLOCK) {
                event.cancel();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

}
