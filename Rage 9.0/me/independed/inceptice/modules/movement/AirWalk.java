//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AirWalk extends Module {
      public AirWalk() {
            super("AirWalk", "walking in air", ((1041568867 | 359076189) ^ 596257634) >>> 4 ^ 30397409, Module.Category.MOVEMENT);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (!"you probably spell youre as your".equals("you're dogshit")) {
                        ;
                  }

                  mc.player.onGround = (boolean)((0 & 93353539) << 4 ^ 1);
                  if ((153006113 >>> 2 >> 4 << 4 >>> 2 ^ 1021490932) != 0) {
                        ;
                  }

                  int var10000 = mc.player.ticksExisted;
                  int var10001 = (0 << 4 & 964061131) >> 4 << 3 >>> 3 ^ 3;
                  if (!"please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var10000 % var10001 == (((0 | 938263838) & 326656992) >>> 4 ^ 20349969)) {
                        Minecraft var2 = mc;
                        if ((495554910 >>> 3 & 25591417 ^ 25178665) == 0) {
                              ;
                        }

                        var2.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, (boolean)((0 & 1815295885 ^ 333905843) << 1 ^ 667811687)));
                  }

            }
      }

      public void onDisable() {
            super.onDisable();
            if (mc.player != null) {
                  mc.player.onGround = (boolean)((1981679827 >>> 2 >>> 1 & 166357056) >> 3 >>> 3 ^ 2296064);
            }
      }
}
