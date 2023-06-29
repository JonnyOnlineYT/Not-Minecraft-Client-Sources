//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class wTap extends Module {
      public wTap() {
            int var10003 = ((1654384531 | 118923997) >>> 3 ^ 100764356 | 109152832) ^ 250871423;
            Module.Category var10004 = Module.Category.COMBAT;
            if (((1868220725 ^ 1304489922) & 185224363 ^ 34210979) == 0) {
                  ;
            }

            super("wTap", "automatically push enemy", var10003, var10004);
      }

      @SubscribeEvent
      public void onAttack(AttackEntityEvent var1) {
            if (mc.player != null) {
                  boolean var10000 = var1.getTarget() instanceof EntityLivingBase;
                  if (((1066440217 ^ 967025852) << 2 >>> 3 ^ -770277890) != 0) {
                        ;
                  }

                  if (var10000) {
                        if (mc.player.isSprinting()) {
                              EntityPlayerSP var2 = mc.player;
                              int var10001 = (1141195222 >> 3 >>> 2 | 11425509) ^ 44990191;
                              if (!"idiot".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              var2.setSprinting((boolean)var10001);
                        }

                        NetHandlerPlayClient var3 = mc.player.connection;
                        CPacketEntityAction var4 = new CPacketEntityAction;
                        EntityPlayerSP var10003 = mc.player;
                        Action var10004 = Action.START_SPRINTING;
                        if (((1784361425 << 1 | 259678135) << 4 ^ 241523167 ^ -243096913) == 0) {
                              ;
                        }

                        var4.<init>(var10003, var10004);
                        var3.sendPacket(var4);
                        mc.player.setSprinting((boolean)(((0 | 1333858488) ^ 850789447 | 1185138862) ^ 2142756094));
                  }

            }
      }
}
