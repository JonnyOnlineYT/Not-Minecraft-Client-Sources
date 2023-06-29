//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Category;
import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.module.Module;

public class FastEat extends Module
{
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public FastEat() {
        super("FastEat", "FastEating", Category.MISC);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (FastEat.mc.player.getHealth() > 0.0f && FastEat.mc.player.onGround && FastEat.mc.player.inventory.getCurrentItem() != null && FastEat.mc.player.inventory.getCurrentItem().getItem() instanceof ItemFood && FastEat.mc.player.getFoodStats().needFood() && FastEat.mc.gameSettings.keyBindUseItem.isPressed()) {
            for (int i = 0; i < 100; ++i) {
                FastEat.mc.player.connection.sendPacket((Packet)new CPacketPlayer(false));
            }
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
