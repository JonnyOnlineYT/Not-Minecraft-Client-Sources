package ua.apraxia.modules.impl.world;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.utility.Utility;
import net.minecraft.potion.Potion;

@SuppressWarnings("all")
public class FullBright extends Module {


    public FullBright() {
        super("FullBright", Categories.World);
        addSetting();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (isModuleState()) {
                Utility.mc.gameSettings.gammaSetting = 10f;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Utility.mc.gameSettings.gammaSetting = 0.1f;
        Utility.mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
