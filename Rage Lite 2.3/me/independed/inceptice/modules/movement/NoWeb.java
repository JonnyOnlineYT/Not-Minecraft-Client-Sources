//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class NoWeb extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (!"stringer is a good obfuscator".equals("yo mama name maurice")) {
                  ;
            }

            if (mc.player != null) {
                  BlockPos var2 = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                        ;
                  }

                  BlockPos var3 = new BlockPos(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ);
                  if ((mc.world.getBlockState(var2).getBlock() == Blocks.WEB || mc.world.getBlockState(var3).getBlock() == Blocks.WEB) && mc.gameSettings.keyBindForward.isKeyDown()) {
                        LongJump.setMoveSpeed(0.27000001072883606D);
                  }

            }
      }

      public NoWeb() {
            int var10003 = (527899537 >>> 1 | 259747571) << 4 >>> 2 ^ 1062499075 ^ 12307695;
            if (!"you're dogshit".equals("ape covered in human flesh")) {
                  ;
            }

            super("NoWeb", "fast walk in webs", var10003, Module.Category.MOVEMENT);
      }
}
