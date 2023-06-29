//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AntiAFK extends Module {
      private TimerUtil counter3;
      private TimerUtil counter1;
      private TimerUtil counter2;

      public AntiAFK() {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            super("AntiAFK", "AntiAFK kick", 1074143244 << 1 >>> 2 ^ 537071622, Module.Category.PLAYER);
            this.counter1 = new TimerUtil();
            this.counter2 = new TimerUtil();
            this.counter3 = new TimerUtil();
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            if (this.counter1.hasReached(10000.0D)) {
                  EntityPlayerSP var2 = mc.player;
                  var2.rotationYaw += 10.0F;
                  AntiAFK.setPressed(mc.gameSettings.keyBindForward, (boolean)((0 ^ 773881249) & 670471232 ^ 639636481));
                  this.counter1.reset();
            }

            if (this.counter2.hasReached(13000.0D)) {
                  AntiAFK.setPressed(mc.gameSettings.keyBindForward, (boolean)(943943899 ^ 84210472 ^ 716072361 ^ 401204826));
                  if (((143397888 ^ 125295217) << 1 >> 3 ^ -1506679067) != 0) {
                        ;
                  }

                  if ((1983295922 >> 2 >> 1 << 1 ^ 495823980) == 0) {
                        ;
                  }

                  Minecraft var10000 = mc;
                  if (!"you're dogshit".equals("please dont crack my plugin")) {
                        ;
                  }

                  var10000.player.jump();
                  this.counter2.reset();
            }

            if (this.counter3.hasReached(16000.0D)) {
                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  this.counter3.reset();
            }

      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var10000 = var0.getClass().getDeclaredField("pressed");
                  if (!"i hope you catch fire ngl".equals("intentMoment")) {
                        ;
                  }

                  Field var2 = var10000;
                  if ((741392393 ^ 244287991 ^ 578089690 ^ 1612808 ^ 13838124) == 0) {
                        ;
                  }

                  var2.setAccessible((boolean)((0 >>> 3 & 1380824152 & 412836360) >>> 4 ^ 1));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }
      }

      public static void resetPressed(KeyBinding var0) {
            if (((207550875 << 4 >>> 1 | 100288420) >>> 4 ^ 1136033175) != 0) {
                  ;
            }

            AntiAFK.setPressed(var0, GameSettings.isKeyDown(var0));
            if (((177377991 ^ 176216870) >> 4 ^ 74910) == 0) {
                  ;
            }

      }
}
