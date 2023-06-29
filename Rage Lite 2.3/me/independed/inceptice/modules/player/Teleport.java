//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Teleport extends Module {
      public Teleport() {
            if ((((1951746275 >>> 1 | 743571179) >> 4 | 28828748) ^ 66584559) == 0) {
                  ;
            }

            if (((1972662614 >> 4 & 106775429) << 1 ^ 213026314) == 0) {
                  ;
            }

            int var10003 = 826911724 >> 2 >> 3 ^ 25840991;
            if ((608760415 << 4 >>> 4 & 9943064 ^ 'ë€?') == 0) {
                  ;
            }

            super("Teleport", "teleport you on block that you are looking at", var10003, Module.Category.PLAYER);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (Minecraft.getMinecraft().objectMouseOver != null) {
                        if (!"yo mama name maurice".equals("please go outside")) {
                              ;
                        }

                        if (Minecraft.getMinecraft().objectMouseOver.typeOfHit == Type.BLOCK) {
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("minecraft")) {
                                    ;
                              }

                              int var2 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
                              int var3 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY();
                              int var4 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
                              EntityPlayerSP var10000 = Minecraft.getMinecraft().player;
                              if (!"nefariousMoment".equals("please go outside")) {
                                    ;
                              }

                              var10000.setPosition((double)var2, (double)(var3 + ((0 & 731186376) << 4 ^ 4)), (double)var4);
                        }
                  }

            }
      }
}
