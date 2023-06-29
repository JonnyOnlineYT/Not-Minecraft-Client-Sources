//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoWeb extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((994481191 << 3 | 1912687762) ^ -97129030) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if (!"you probably spell youre as your".equals("please dont crack my plugin")) {
                        ;
                  }

                  BlockPos var2 = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                  BlockPos var10000 = new BlockPos;
                  double var10002 = mc.player.posX;
                  double var10003 = mc.player.posY;
                  if (((1954063439 >> 3 >>> 3 >> 1 | 5749341) ^ 16776029) == 0) {
                        ;
                  }

                  var10000.<init>(var10002, var10003 + 1.0D, mc.player.posZ);
                  BlockPos var3 = var10000;
                  if ((748652566 << 3 ^ 1311880414 ^ 192494730 ^ 565496036) == 0) {
                        ;
                  }

                  WorldClient var4 = mc.world;
                  if (((642187288 >>> 1 | 133018271) ^ 1563784709) != 0) {
                        ;
                  }

                  if (var4.getBlockState(var2).getBlock() != Blocks.WEB) {
                        var4 = mc.world;
                        if (!"please go outside".equals("please go outside")) {
                              ;
                        }

                        if ((1354482635 << 1 & 1290124734 ^ 6653334) == 0) {
                              ;
                        }

                        if (var4.getBlockState(var3).getBlock() != Blocks.WEB) {
                              return;
                        }
                  }

                  if (mc.gameSettings.keyBindForward.isKeyDown()) {
                        LongJump.setMoveSpeed(0.25D);
                  }

            }
      }

      public NoWeb() {
            super("NoWeb", "fast walk in webs", ((1853749991 >>> 2 | 278647360) ^ 220702485) >>> 1 >>> 1 ^ 95295099, Module.Category.MOVEMENT);
            if (!"i hope you catch fire ngl".equals("intentMoment")) {
                  ;
            }

      }
}
