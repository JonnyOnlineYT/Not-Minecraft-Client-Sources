//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AirWalk extends Module {
      public void onDisable() {
            super.onDisable();
            if (!"please take a shower".equals("minecraft")) {
                  ;
            }

            if (mc.player != null) {
                  EntityPlayerSP var10000 = mc.player;
                  if ((1035966707 >>> 1 >> 4 ^ 32373959) == 0) {
                        ;
                  }

                  var10000.onGround = (boolean)((1151301145 ^ 329468056) >> 1 ^ 731782976);
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            if (var10000 != null) {
                  mc.player.onGround = (boolean)((0 >>> 4 >> 3 >>> 1 | 998311874) ^ 998311875);
                  if (mc.player.ticksExisted % (1 >> 2 & 1618297810 & 724013433 ^ 3) == (((0 & 2084698783) << 4 | 1093957933) ^ 1093957932)) {
                        if (!"buy a domain and everything else you need at namecheap.com".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        NetHandlerPlayClient var2 = mc.player.connection;
                        Position var10001 = new Position;
                        double var10003 = mc.player.posX;
                        Minecraft var10004 = mc;
                        if (((1394424206 ^ 298048699 | 525429292) ^ 1608511293) == 0) {
                              ;
                        }

                        EntityPlayerSP var3 = var10004.player;
                        if ((113263634 << 2 << 1 >>> 4 ^ 623889897) != 0) {
                              ;
                        }

                        var10001.<init>(var10003, var3.posY, mc.player.posZ, (boolean)((0 ^ 1911251215 | 1256809160) ^ 2079024078));
                        var2.sendPacket(var10001);
                  }

            }
      }

      public AirWalk() {
            super("AirWalk", "walking in air", (81797144 >> 2 >>> 2 & 4466688) << 3 ^ 35651584, Module.Category.MOVEMENT);
      }
}
