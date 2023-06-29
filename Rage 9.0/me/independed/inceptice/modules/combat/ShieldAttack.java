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
            if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                  ;
            }

            super("ShieldAttack", "attack entities   ", 1212516311 >>> 1 >>> 1 >>> 4 ^ 18945567, Module.Category.COMBAT);
      }

      @SubscribeEvent
      public void onPlayerTick(MouseEvent var1) {
            if (mc.player != null && mc.world != null && mc.player.isHandActive() && var1.isButtonstate() && mc.pointedEntity != null) {
                  mc.player.connection.sendPacket(new CPacketUseEntity(mc.pointedEntity));
                  EntityPlayerSP var10000 = mc.player;
                  if ((((945367057 ^ 496640368) & 326582849) << 3 ^ -919899952) != 0) {
                        ;
                  }

                  var10000.swingArm(EnumHand.MAIN_HAND);
                  mc.player.resetCooldown();
            }

      }
}
