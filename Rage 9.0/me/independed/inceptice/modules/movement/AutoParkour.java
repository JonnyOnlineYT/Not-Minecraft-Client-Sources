//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoParkour extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (spidiki5.isMoving() && mc.player.onGround && !mc.player.isSneaking() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                        WorldClient var10000 = mc.world;
                        Minecraft var10001 = mc;
                        if (((2112442422 | 491715229) << 3 & 488411928 ^ 219975960) == 0) {
                              ;
                        }

                        EntityPlayerSP var3 = var10001.player;
                        AxisAlignedBB var10002 = mc.player.getEntityBoundingBox();
                        if (((185571733 | 92821240) << 3 << 3 ^ -76133886) != 0) {
                              ;
                        }

                        if (((694353909 >>> 1 >> 2 | 17615336) >>> 4 ^ -1029758661) != 0) {
                              ;
                        }

                        boolean var2 = var10000.getCollisionBoxes(var3, var10002.offset(0.0D, -0.5D, 0.0D).expand(-0.001D, 0.0D, -0.001D)).isEmpty();
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        if (var2 && mc.player.moveForward != 0.0F) {
                              mc.player.jump();
                        }
                  }

            }
      }

      public AutoParkour() {
            super("AutoParkour", "automatically parkour", (1856155545 << 4 >> 2 | 518341607) & 1801543197 ^ 1784765957, Module.Category.MOVEMENT);
      }
}
