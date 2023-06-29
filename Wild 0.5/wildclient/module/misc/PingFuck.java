// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class PingFuck extends Module
{
    public PingFuck() {
        super("PingFuck", "rotating like bitch", Category.MISC);
        Wild.instance.settingsManager.rSetting(new Setting("Ping", this, 500.0, 1.0, 10000.0, true));
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final int ping = (int)Wild.instance.settingsManager.getSettingByName(this, "Ping").getValDouble();
    }
}
