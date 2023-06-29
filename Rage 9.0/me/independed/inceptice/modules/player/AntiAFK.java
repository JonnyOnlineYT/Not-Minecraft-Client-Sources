//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AntiAFK extends Module {
      private TimerUtil counter2;
      private TimerUtil counter3;
      private TimerUtil counter1;

      public AntiAFK() {
            super("AntiAFK", "AntiAFK kick", ((799100717 ^ 108983000) << 4 ^ 2111069068 | 1138601595) ^ -469782785, Module.Category.PLAYER);
            TimerUtil var10001 = new TimerUtil;
            if ((((1549712901 >>> 4 | 72687259) ^ 25721863 ^ 51979741) & 62401375 ^ 50340353) == 0) {
                  ;
            }

            var10001.<init>();
            this.counter1 = var10001;
            this.counter2 = new TimerUtil();
            if (!"yo mama name maurice".equals("you're dogshit")) {
                  ;
            }

            this.counter3 = new TimerUtil();
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (this.counter1.hasReached(10000.0D)) {
                  EntityPlayerSP var2 = mc.player;
                  var2.rotationYaw += 10.0F;
                  AntiAFK.setPressed(mc.gameSettings.keyBindForward, (boolean)((0 | 1119697785) << 2 << 2 ^ 735295377));
                  this.counter1.reset();
            }

            if (this.counter2.hasReached(13000.0D)) {
                  if ((((1269255379 >>> 4 & 18821388) >>> 4 | 76916) & 91496 ^ -1753642663) != 0) {
                        ;
                  }

                  AntiAFK.setPressed(mc.gameSettings.keyBindForward, (boolean)((895669657 ^ 412252276) & 153947110 ^ 153094628));
                  if ((732765832 >>> 2 & 180764134 ^ 180487586) == 0) {
                        ;
                  }

                  mc.player.jump();
                  if (!"idiot".equals("please dont crack my plugin")) {
                        ;
                  }

                  this.counter2.reset();
            }

            if (this.counter3.hasReached(16000.0D)) {
                  if ((((209968895 | 79824017) << 4 | 1471936197) >>> 4 ^ 234618879) == 0) {
                        ;
                  }

                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  this.counter3.reset();
            }

      }

      public static void resetPressed(KeyBinding var0) {
            AntiAFK.setPressed(var0, GameSettings.isKeyDown(var0));
            if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  if ("Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("nefariousMoment")) {
                  }

                  var2.setAccessible((boolean)((0 << 1 & 736937287) << 2 ^ 1));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }

            if ((1083978163 >> 2 ^ 37887333 ^ 308613385) == 0) {
                  ;
            }

      }
}
