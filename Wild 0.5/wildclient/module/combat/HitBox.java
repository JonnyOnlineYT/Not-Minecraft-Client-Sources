//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import black.nigger.wildclient.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import black.nigger.wildclient.Utils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Module;

public class HitBox extends Module
{
    public HitBox() {
        super("HitBox", "Increase Hitbox size", Category.COMBAT);
        Wild.instance.settingsManager.rSetting(new Setting("Width", this, 0.3, 0.1, 3.0, false));
        Wild.instance.settingsManager.rSetting(new Setting("Height", this, 0.3, 0.1, 3.0, false));
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        for (final EntityPlayer player : Utils.getPlayersList()) {
            if (!this.check((EntityLivingBase)player)) {
                continue;
            }
            if (player == null) {
                continue;
            }
            final float width = (float)Wild.instance.settingsManager.getSettingByName(this, "Width").getValDouble();
            final float height = (float)Wild.instance.settingsManager.getSettingByName(this, "Height").getValDouble();
            Utils.setEntityBoundingBoxSize((Entity)player, width, height);
        }
    }
    
    @Override
    public void onDisable() {
        for (final EntityPlayer player : Utils.getPlayersList()) {
            Utils.setEntityBoundingBoxSize((Entity)player);
        }
        super.onDisable();
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityPlayerSP) && entity != Wrapper.INSTANCE.player() && !entity.isDead;
    }
}
