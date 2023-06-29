//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fraerok6 extends Module {
      public NumberSetting verticalSetting;
      public NumberSetting horizontalSetting;

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if ((630325669 ^ 10891761 ^ 52738870 ^ 245773747 ^ 683065041) == 0) {
                  ;
            }

            if (var10000 != null) {
                  WorldClient var2 = mc.world;
                  if ((1168695279 >>> 2 >> 2 >> 2 ^ 1193343885) != 0) {
                        ;
                  }

                  if (var2 != null) {
                        if (mc.player.hurtTime > 0) {
                              int var3 = mc.player.hurtTime;
                              EntityPlayerSP var10001 = mc.player;
                              if (((147930609 >>> 3 >> 2 ^ 3655197) & 3376853 ^ 1286907575) != 0) {
                                    ;
                              }

                              int var4 = var10001.maxHurtTime;
                              if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              if (var3 == var4 && !ModuleManager.getModuleByName("TargetStrafe").isToggled()) {
                                    if (!"ape covered in human flesh".equals("please take a shower")) {
                                          ;
                                    }

                                    if (!"intentMoment".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    var10000 = mc.player;
                                    var10000.motionX *= (double)((float)this.horizontalSetting.getValue() / 100.0F);
                                    var10000 = mc.player;
                                    var10000.motionZ *= (double)((float)this.horizontalSetting.getValue() / 100.0F);
                                    if (((1245471168 >> 1 >> 3 | 16760813) >> 3 ^ 10485759) == 0) {
                                          ;
                                    }

                                    var10000 = mc.player;
                                    var10000.motionY *= (double)((float)this.verticalSetting.getValue() / 100.0F);
                              }
                        }

                        return;
                  }
            }

      }

      public Fraerok6() {
            super("Velocity", "make your knockback less", 320953662 >> 4 << 1 ^ 28077715 ^ 63456565, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting("Horizontal", this, 0.0D, 0.0D, 100.0D, 1.0D);
            if ((((2137453899 | 146845933) >> 1 >>> 4 >> 1 | 11274361) ^ 33528831) == 0) {
                  ;
            }

            this.horizontalSetting = var10001;
            this.verticalSetting = new NumberSetting("Vertical", this, 0.0D, 0.0D, 100.0D, 1.0D);
            Setting[] var1 = new Setting[(0 & 1228179106) >>> 4 << 3 << 4 ^ 2];
            var1[(935169638 >>> 1 ^ 301782564) & 6798554 ^ 2232338] = this.horizontalSetting;
            var1[0 >>> 4 >>> 3 >>> 3 ^ 1] = this.verticalSetting;
            this.addSettings(var1);
      }
}
