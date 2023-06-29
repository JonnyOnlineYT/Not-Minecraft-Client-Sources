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
            if (((1704995049 ^ 793585059) >> 2 & 219440675 ^ 1065474) == 0) {
                  ;
            }

            super("Teleport", "teleport you on block that you are looking at", 1126523760 >> 4 >> 1 & 11228476 ^ 424591 ^ 1014679, Module.Category.PLAYER);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  Minecraft var10000 = Minecraft.getMinecraft();
                  if (((1024328623 >> 3 << 1 ^ 226879148) & 44912122 ^ 42222658) == 0) {
                        ;
                  }

                  if (var10000.objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == Type.BLOCK) {
                        int var2 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
                        if (!"stop skidding".equals("intentMoment")) {
                              ;
                        }

                        int var3 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY();
                        int var4 = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
                        EntityPlayerSP var5 = Minecraft.getMinecraft().player;
                        double var10001 = (double)var2;
                        int var10003 = (3 | 2) << 2 << 2 ^ 52;
                        if ((((18988770 ^ 8591240) & 24059630) << 2 ^ 122755040) != 0) {
                              ;
                        }

                        int var10002 = var3 + var10003;
                        if (!"please take a shower".equals("you probably spell youre as your")) {
                              ;
                        }

                        var5.setPosition(var10001, (double)var10002, (double)var4);
                  }

            }
      }
}
