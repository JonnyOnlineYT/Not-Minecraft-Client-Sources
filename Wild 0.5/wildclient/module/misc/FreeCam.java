//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import black.nigger.wildclient.module.Module;

public class FreeCam extends Module
{
    private double[] oldPosition;
    private EntityOtherPlayerMP freecamPlayer;
    
    public FreeCam() {
        super("FreeCam", "freecum", Category.MISC);
        this.freecamPlayer = null;
    }
    
    @SubscribeEvent
    public void onPlayerTickEvent(final TickEvent.PlayerTickEvent playerTickEvent) {
        FreeCam.mc.player.capabilities.isFlying = true;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FreeCam.mc.player.setPositionAndRotation(this.oldPosition[0], this.oldPosition[1], this.oldPosition[2], FreeCam.mc.player.rotationYaw, FreeCam.mc.player.rotationPitch);
        FreeCam.mc.world.removeEntityFromWorld(-420);
        this.freecamPlayer = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.clonePositions();
        this.freecamPlayer.noClip = true;
        this.freecamPlayer.rotationYawHead = FreeCam.mc.player.rotationYawHead;
        FreeCam.mc.world.addEntityToWorld(-420, (Entity)this.freecamPlayer);
    }
    
    private void clonePositions() {
        this.oldPosition = new double[] { FreeCam.mc.player.posX, FreeCam.mc.player.posY, FreeCam.mc.player.posZ };
    }
}
