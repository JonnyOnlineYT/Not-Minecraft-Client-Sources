//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoShift extends Module {
      public boolean shouldShifting;
      TimerUtil timer;
      NumberSetting delayShifting;

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  if ((1263218807 >> 1 >>> 1 & 203424806 ^ 1624918803) != 0) {
                        ;
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  var2.setAccessible((boolean)((0 ^ 1457806391) >>> 3 << 3 ^ 1300359204 ^ 459650069));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  RuntimeException var10000 = new RuntimeException;
                  if (((150256747 >>> 3 >> 3 << 3 | 1307187) ^ 18872251) != 0) {
                  }

                  var10000.<init>(var3);
                  throw var10000;
            }

            if ((1237468442 << 2 >>> 2 ^ 163726618) == 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerAttack(AttackEntityEvent var1) {
            if (mc.player != null) {
                  KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), (boolean)(0 >> 1 << 4 ^ 1));
                  this.shouldShifting = (boolean)((0 | 1934769222) ^ 333592391 ^ 902407884 ^ 1434037708);
                  this.timer.reset();
            }
      }

      public AutoShift() {
            super("AutoShift", "presses shift when you hit entity", (259982217 ^ 63322901) & 75819209 & 17977924 ^ 1485363352 ^ 1485363352, Module.Category.COMBAT);
            if ((2139293622 >> 4 << 4 >>> 1 ^ 1069646808) == 0) {
                  ;
            }

            this.timer = new TimerUtil();
            this.delayShifting = new NumberSetting("TimeShift", this, 150.0D, 30.0D, 500.0D, 10.0D);
            this.shouldShifting = (boolean)(46436552 >> 4 ^ 2902284);
            Setting[] var10001 = new Setting[0 >>> 3 >>> 4 >>> 4 >> 4 ^ 1333593435 ^ 1333593434];
            if (((20952833 ^ 20474041) >> 3 >>> 4 ^ 459109714) != 0) {
                  ;
            }

            int var10003 = 1364467825 >>> 2 << 2 ^ 1364467824;
            if ((2001268786 >> 3 >> 1 << 1 ^ 250158598) == 0) {
                  ;
            }

            var10001[var10003] = this.delayShifting;
            this.addSettings(var10001);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if ((1209905753 >>> 4 >> 2 >>> 3 ^ -1085729695) != 0) {
                  ;
            }

            if (mc.player != null && mc.world != null) {
                  if (this.timer.hasReached(this.delayShifting.getValue())) {
                        if (!"please take a shower".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        Minecraft var10000 = mc;
                        if ((12783882 >>> 3 ^ 1597985) == 0) {
                              ;
                        }

                        EntityPlayerSP var2 = var10000.player;
                        if (((1585012428 ^ 131407370 ^ 1219907404) >> 1 ^ 1712257853) != 0) {
                              ;
                        }

                        if (var2.isSneaking() && this.shouldShifting) {
                              KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), (boolean)(1398197371 << 3 << 3 ^ -709681472));
                              this.shouldShifting = (boolean)((34342584 | 13194368) ^ 47011512);
                              if (((35336149 << 3 | 155918425) ^ -1055621713) != 0) {
                                    ;
                              }

                              this.timer.reset();
                        }
                  }

            } else {
                  if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                        ;
                  }

            }
      }

      public static void resetPressed(KeyBinding var0) {
            AutoShift.setPressed(var0, GameSettings.isKeyDown(var0));
      }
}
