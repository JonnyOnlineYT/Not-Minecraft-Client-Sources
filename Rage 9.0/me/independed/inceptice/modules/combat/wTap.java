//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class wTap extends Module {
      public wTap() {
            super("wTap", "automatically push enemy", (262970677 | 194021703) << 3 << 1 ^ -70658192, Module.Category.COMBAT);
      }

      @SubscribeEvent
      public void onAttack(AttackEntityEvent var1) {
            if ((((1392651399 | 805759677) >> 2 & 473455253) << 1 ^ 939537674) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if (var1.getTarget() instanceof EntityLivingBase) {
                        if (((1128533632 >>> 4 ^ 42602792) << 1 >> 3 & 19985856 ^ 18912320) == 0) {
                              ;
                        }

                        EntityPlayerSP var10000 = mc.player;
                        if (((2107348223 ^ 1550304528 ^ 145207054) >>> 2 << 4 ^ -1519387776) == 0) {
                              ;
                        }

                        if (var10000.isSprinting()) {
                              mc.player.setSprinting((boolean)(37757056 >> 4 ^ 790438 ^ 1749224 ^ 3325254));
                        }

                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SPRINTING));
                        var10000 = mc.player;
                        if ((65820 ^ 515097598) != 0) {
                              ;
                        }

                        var10000.setSprinting((boolean)((0 | 1183085968 | 128760956) << 2 ^ 515110897));
                  }

            }
      }
}
