//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.movement;

import black.nigger.wildclient.module.Category;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Module;

public class Fly extends Module
{
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (Fly.mc.gameSettings.keyBindJump.isPressed()) {
            Fly.mc.player.motionY = 0.413213;
            final double d = Fly.mc.player.posX;
            final double d2 = Fly.mc.player.posY;
            final double d3 = Fly.mc.player.posZ;
            Fly.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(d, d2 + 0.413213, d3, Fly.mc.player.rotationYaw, Fly.mc.player.rotationPitch, true));
            Fly.mc.player.setPositionAndRotationDirect(d, d2 + 0.413213, d3, Fly.mc.player.rotationYaw, Fly.mc.player.rotationPitch, 1, false);
        }
    }
    
    public Fly() {
        super("Fly", "you can fly", Category.MOVEMENT);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
