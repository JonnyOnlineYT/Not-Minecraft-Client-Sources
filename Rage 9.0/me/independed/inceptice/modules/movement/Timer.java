//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Timer extends Module {
      public NumberSetting timerTicks;

      public void onDisable() {
            if (mc.player != null) {
                  if (((1886130342 ^ 698220701) & 753676998 ^ 148901890) == 0) {
                        ;
                  }

                  WorldClient var10000 = mc.world;
                  if (((1394297813 >> 3 ^ 96866487) >> 3 ^ 32812857) == 0) {
                        ;
                  }

                  if (var10000 != null) {
                        super.onDisable();
                        Timer.setTimerSpeed(50.0F);
                        return;
                  }
            }

      }

      public static void setTimerSpeed(float var0) {
            if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            try {
                  Class var1 = Minecraft.class;
                  if (((510436103 >> 4 ^ 5625815 | 16554333) ^ 858802630) != 0) {
                        ;
                  }

                  Field var2 = var1.getDeclaredField("timer");
                  var2.setAccessible((boolean)((0 >> 2 >> 2 ^ 293275119) << 1 ^ 586550239));

                  try {
                        Object var3 = var2.get(Minecraft.getMinecraft());
                        Class var4 = ((Object)var3).getClass();
                        Field var10000 = var4.getDeclaredField("tickLength");
                        if (((2071754449 << 2 | 2121970635) >> 3 ^ -32903) == 0) {
                              ;
                        }

                        Field var5 = var10000;
                        var5.setAccessible((boolean)(((0 & 986640586) << 1 & 1579956737) << 1 ^ 1));
                        if (!"idiot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var5.setFloat(var3, var0);
                  } catch (IllegalAccessException var6) {
                        var6.printStackTrace();
                        return;
                  }

                  if (((1449852906 >>> 3 ^ 170824885) << 4 ^ 238859392) == 0) {
                        ;
                  }
            } catch (NoSuchFieldException var7) {
                  var7.printStackTrace();
            }

      }

      public Timer() {
            if (!"idiot".equals("stop skidding")) {
                  ;
            }

            if (((614363708 | 445884918) << 4 ^ -1253992615) != 0) {
                  ;
            }

            super("Timer", "make your game faster", 1796165329 >>> 1 >> 3 ^ 112260333, Module.Category.MOVEMENT);
            if (!"idiot".equals("nefariousMoment")) {
                  ;
            }

            this.timerTicks = new NumberSetting("Speed", this, 30.0D, 1.0D, 1000.0D, 1.0D);
            Setting[] var10001 = new Setting[0 & 1457327465 & 1019903910 ^ 1];
            var10001[(4325888 << 3 >>> 3 | 2316857) ^ 6511161] = this.timerTicks;
            this.addSettings(var10001);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (!"stringer is a good obfuscator".equals("intentMoment")) {
                        ;
                  }

                  if (((451369924 | 169098114) << 1 ^ 2041666661) != 0) {
                        ;
                  }

                  Timer.setTimerSpeed((float)this.timerTicks.getValue());
            }
      }
}
