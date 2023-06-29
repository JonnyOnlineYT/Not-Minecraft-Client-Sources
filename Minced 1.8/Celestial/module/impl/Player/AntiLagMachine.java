package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.otherutils.gayutil.EventLight;
import net.minecraft.world.EnumSkyBlock;

public class AntiLagMachine extends Module {
    public AntiLagMachine() {
        super("AntiLagMachine", "", ModuleCategory.Player);
    }
    @EventTarget
    public void onWorldLight(EventLight event) {
        if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
            event.cancel();
        }
    }

}