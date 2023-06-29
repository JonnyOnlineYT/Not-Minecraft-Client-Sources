//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.misc;

import black.nigger.wildclient.module.Category;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Module;

public class FastUse extends Module
{
    @SubscribeEvent
    public void onPlayerTickEvent(final TickEvent.PlayerTickEvent playerTickEvent) {
        if (FastUse.mc.player.isHandActive() && FastUse.mc.player.getItemInUseMaxCount() >= 3 && (FastUse.mc.player.getHeldItemMainhand().getItem() == Items.BOW || FastUse.mc.player.getHeldItemOffhand().getItem() == Items.BOW)) {
            FastUse.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastUse.mc.player.getHorizontalFacing()));
            FastUse.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(FastUse.mc.player.getActiveHand()));
            FastUse.mc.player.stopActiveHand();
        }
    }
    
    public FastUse() {
        super("FastUse", "use items faster", Category.MISC);
    }
}
