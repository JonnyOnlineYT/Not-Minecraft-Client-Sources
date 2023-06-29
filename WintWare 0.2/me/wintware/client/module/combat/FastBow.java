package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class FastBow extends Module {
   public FastBow() {
      super("FastBow", Category.Combat);
   }

   @EventTarget
   public void onEventUpdate(EventUpdate e) {
      if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && (double)mc.player.getItemInUseMaxCount() >= 2.4D) {
         mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
         mc.player.stopActiveHand();
      }

   }
}
