//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class Velocity extends Module
{
    public Velocity() {
        super("Velocity", "i hate being knocked back", Category.COMBAT);
        Wild.instance.settingsManager.rSetting(new Setting("Horizontal", this, 50.0, 0.0, 100.0, true));
        Wild.instance.settingsManager.rSetting(new Setting("Vertical", this, 50.0, 0.0, 100.0, true));
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent e) {
        final float horizontal = (float)Wild.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble();
        final float vertical = (float)Wild.instance.settingsManager.getSettingByName(this, "Vertical").getValDouble();
        if (Velocity.mc.player.hurtTime == Velocity.mc.player.maxHurtTime && Velocity.mc.player.maxHurtTime > 0) {
            final EntityPlayerSP player = Velocity.mc.player;
            player.motionX *= horizontal / 100.0f;
            final EntityPlayerSP player2 = Velocity.mc.player;
            player2.motionY *= vertical / 100.0f;
            final EntityPlayerSP player3 = Velocity.mc.player;
            player3.motionZ *= horizontal / 100.0f;
        }
    }
}
