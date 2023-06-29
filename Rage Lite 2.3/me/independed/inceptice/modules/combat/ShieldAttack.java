//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShieldAttack extends Module {
      public ShieldAttack() {
            super("UseAttack", "attack entities   ", (344886806 | 270850488) << 4 << 3 ^ 1466949376, Module.Category.COMBAT);
      }

      @SubscribeEvent
      public void onPlayerTick(MouseEvent var1) {
            if (mc.player != null && mc.world != null && mc.player.isHandActive() && var1.isButtonstate() && mc.pointedEntity != null) {
                  mc.player.connection.sendPacket(new CPacketUseEntity(mc.pointedEntity));
                  if (!"please take a shower".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  EntityPlayerSP var10000 = mc.player;
                  if (((1444815821 << 2 ^ 1085782696) >>> 3 ^ 1883886245) != 0) {
                        ;
                  }

                  var10000.resetCooldown();
            }

      }
}
