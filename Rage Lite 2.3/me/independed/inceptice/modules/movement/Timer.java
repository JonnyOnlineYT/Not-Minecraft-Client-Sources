//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Timer extends Module {
      public NumberSetting timerTicks;

      public void onDisable() {
            if (mc.player != null && mc.world != null) {
                  super.onDisable();
                  if ((1321737 ^ 257088 ^ 841477501) != 0) {
                        ;
                  }

                  Timer.setTimerSpeed(50.0F);
            }
      }

      public Timer() {
            if ((783384868 >> 1 << 3 >>> 1 >>> 2 ^ 1943015252) != 0) {
                  ;
            }

            super("Timer", "make your game faster", 1680343568 >> 1 ^ 840171784, Module.Category.MOVEMENT);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            NumberSetting var10001 = new NumberSetting;
            if (((1254864188 ^ 366373672) >> 4 ^ 7239893 ^ 94348596) == 0) {
                  ;
            }

            var10001.<init>("Speed", this, 30.0D, 1.0D, 1000.0D, 1.0D);
            this.timerTicks = var10001;
            int var1 = 0 & 1218917856 ^ 618782669 ^ 618782668;
            if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                  ;
            }

            Setting[] var2 = new Setting[var1];
            var2[(800487547 | 220322623) << 2 ^ -1092620804] = this.timerTicks;
            this.addSettings(var2);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((1529742613 << 1 | 2069204371) ^ 1953679776 ^ 1574911786) != 0) {
                        ;
                  }

                  if (mc.world != null) {
                        Timer.setTimerSpeed((float)this.timerTicks.getValue());
                        return;
                  }
            }

      }

      public static void setTimerSpeed(float var0) {
            if ((85896129 << 4 >>> 2 << 4 ^ 562008857 ^ 2094805202) != 0) {
                  ;
            }

            try {
                  Class var1 = Minecraft.class;
                  Field var2 = var1.getDeclaredField("timer");
                  var2.setAccessible((boolean)((0 >>> 2 << 4 & 1660802738) >> 3 << 4 ^ 1));

                  try {
                        if (!"minecraft".equals("please take a shower")) {
                              ;
                        }

                        Object var3 = var2.get(Minecraft.getMinecraft());
                        Class var10000 = ((Object)var3).getClass();
                        if (((27279360 ^ 22039379) >> 1 << 3 >>> 3 ^ 1721141280) != 0) {
                              ;
                        }

                        Class var4 = var10000;
                        Field var5 = var4.getDeclaredField("tickLength");
                        if (((870921756 << 2 >>> 2 & 577918665) << 3 ^ 318812224) == 0) {
                              ;
                        }

                        var5.setAccessible((boolean)(((0 ^ 275540582) & 17028657 | 10805) ^ 27188));
                        var5.setFloat(var3, var0);
                  } catch (IllegalAccessException var6) {
                        var6.printStackTrace();
                  }
            } catch (NoSuchFieldException var7) {
                  var7.printStackTrace();
                  if ("stop skidding".equals("please go outside")) {
                  }
            }

      }
}
