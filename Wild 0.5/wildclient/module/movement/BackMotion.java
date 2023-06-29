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

public class BackMotion extends Module
{
    public BackMotion() {
        super("BackMotion", "i hate being knocked back", Category.COMBAT);
        Wild.instance.settingsManager.rSetting(new Setting("Horizontal", this, 100.0, 0.0, 500.0, true));
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent e) {
        final float horizontal = (float)Wild.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble();
        if (BackMotion.mc.player.hurtTime == BackMotion.mc.player.maxHurtTime && BackMotion.mc.player.maxHurtTime > 0) {
            final EntityPlayerSP player = BackMotion.mc.player;
            player.motionX *= horizontal / -100.0f;
            final EntityPlayerSP player2 = BackMotion.mc.player;
            player2.motionZ *= horizontal / -100.0f;
        }
    }
}
