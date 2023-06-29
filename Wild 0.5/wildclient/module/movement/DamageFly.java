//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class DamageFly extends Module
{
    public DamageFly() {
        super("DamageFly", "fly", Category.MOVEMENT);
        Wild.instance.settingsManager.rSetting(new Setting("Horizontal", this, 300.0, 0.0, 600.0, true));
        Wild.instance.settingsManager.rSetting(new Setting("Vertical", this, 300.0, 0.0, 600.0, true));
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent e) {
        final float horizontal = (float)Wild.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble();
        final float vertical = (float)Wild.instance.settingsManager.getSettingByName(this, "Vertical").getValDouble();
        if (DamageFly.mc.player.hurtTime == DamageFly.mc.player.maxHurtTime && DamageFly.mc.player.maxHurtTime > 0) {
            DamageFly.mc.player.jump();
            final EntityPlayerSP player = DamageFly.mc.player;
            player.motionX *= horizontal / 100.0f;
            final EntityPlayerSP player2 = DamageFly.mc.player;
            player2.motionY *= vertical / 100.0f;
            final EntityPlayerSP player3 = DamageFly.mc.player;
            player3.motionZ *= horizontal / 100.0f;
        }
    }
}
