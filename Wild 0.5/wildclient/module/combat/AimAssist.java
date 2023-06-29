//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraft.entity.player.EntityPlayer;
import black.nigger.wildclient.Wrapper;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.Iterator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import black.nigger.wildclient.Utils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import net.minecraft.entity.EntityLivingBase;
import black.nigger.wildclient.module.Module;

public class AimAssist extends Module
{
    public EntityLivingBase target;
    
    public AimAssist() {
        super("AimBot", "aim", Category.COMBAT);
    }
    
    @Override
    public void onDisable() {
        this.target = null;
        super.onDisable();
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.updateTarget();
        Utils.assistFaceEntity((Entity)this.target, 360.0f, 360.0f);
        this.target = null;
        super.onClientTick(event);
    }
    
    void updateTarget() {
        for (final Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            if (!this.check(entity)) {
                continue;
            }
            this.target = entity;
        }
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && entity != Wrapper.INSTANCE.player() && !entity.isDead && !entity.isInvisible() && !(entity instanceof EntityPlayer) && AimAssist.mc.player.getDistance((Entity)entity) >= 4.2;
    }
}
