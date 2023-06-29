package me.wintware.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketServerDifficulty;

public class AntiBot extends Module {
   public static ArrayList<EntityPlayer> bots = new ArrayList();

   public AntiBot() {
      super("AntiBot", Category.Combat);
   }

   public void onEnable() {
      super.onEnable();
      bots.clear();
   }

   public void onDisable() {
      super.onDisable();
      bots.clear();
   }

   private boolean isInTablist(EntityLivingBase player) {
      if (mc.isSingleplayer()) {
         return true;
      } else {
         Iterator var2 = mc.getConnection().getPlayerInfoMap().iterator();

         NetworkPlayerInfo playerInfo;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            Object o = var2.next();
            playerInfo = (NetworkPlayerInfo)o;
         } while(!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName()));

         return true;
      }
   }

   @EventTarget
   public void onPreMotion(EventReceivePacket event) {
      if (!(event.getPacket() instanceof SPacketServerDifficulty) && !mc.player.isSpectator() && !mc.player.capabilities.allowFlying) {
      }

   }
}
