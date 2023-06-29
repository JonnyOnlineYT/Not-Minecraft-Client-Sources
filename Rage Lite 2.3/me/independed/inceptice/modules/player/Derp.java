//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Derp extends Module {
      private int yaw;
      private NumberSetting speed;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((91968992 | 14189929 | 76883118) ^ 45660919 ^ 2134584274) != 0) {
                        ;
                  }

                  if (mc.world != null) {
                        EntityPlayerSP var10000 = mc.player;
                        if (((1180169 & 11712) << 2 ^ 0) == 0) {
                              ;
                        }

                        int var10001 = this.yaw;
                        if (!"nefariousMoment".equals("nefariousMoment")) {
                              ;
                        }

                        var10000.rotationYawHead = (float)var10001;
                        mc.player.renderYawOffset = (float)this.yaw;
                        if (!"you probably spell youre as your".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var10001 = this.yaw;
                        if (((1694861596 | 764007994 | 933012467) ^ -1408862814) != 0) {
                              ;
                        }

                        this.yaw = (int)((double)var10001 + this.speed.getValue());
                        return;
                  }
            }

      }

      public Derp() {
            super("Derp", "rotating like bitch", (2035581964 << 3 >>> 2 & 391029875) << 3 ^ -1874853760, Module.Category.PLAYER);
            if ((834263999 << 3 >>> 2 ^ 205706002 ^ 791766124) == 0) {
                  ;
            }

            this.speed = new NumberSetting("Speed", this, 5.0D, 1.0D, 50.0D, 1.0D);
            this.yaw = (408752024 >>> 4 | 8421870 | 24201194) ^ 32888831;
            Setting[] var10001 = new Setting[(0 ^ 1006138106) << 1 ^ 2012276213];
            int var10003 = (1928190248 | 1177473102) << 1 >>> 3 ^ 498857563;
            NumberSetting var10004 = this.speed;
            if ((537567205 >> 2 >>> 4 >> 1 ^ 833594 ^ -1581103629) != 0) {
                  ;
            }

            var10001[var10003] = var10004;
            this.addSettings(var10001);
      }
}
