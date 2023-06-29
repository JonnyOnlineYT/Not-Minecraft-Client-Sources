//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSplashPotion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoPotion extends Module {
      public NumberSetting healthSet;
      private TimerUtil timer;
      private int delay;
      private ArrayList slots;

      public static void resetPressed(KeyBinding var0) {
            if (((56724997 ^ 20940651 | 402933) << 1 ^ 79453182) == 0) {
                  ;
            }

            if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                  ;
            }

            AutoPotion.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSplashPotion) {
                  double var10000 = (double)mc.player.getHealth();
                  double var10001 = this.healthSet.getValue();
                  if (((1728085864 >> 1 | 335018226) ^ -660595994) != 0) {
                        ;
                  }

                  if (var10000 < var10001) {
                        mc.player.rotationPitch = 90.0F;
                  }
            }

      }

      public AutoPotion() {
            super("AutoPotion", "throws health potion automatically", 309363200 >>> 2 ^ 77340800, Module.Category.COMBAT);
            if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                  ;
            }

            this.slots = new ArrayList();
            this.delay = (851 >> 1 | 99) & 270 ^ 738;
            this.timer = new TimerUtil();
            NumberSetting var10001 = new NumberSetting;
            if (((93736045 ^ 73626369) >>> 4 ^ 288858422) != 0) {
                  ;
            }

            var10001.<init>("Health", this, 10.0D, 0.0D, 20.0D, 1.0D);
            this.healthSet = var10001;
            if ((291762017 << 3 >>> 4 << 3 ^ 1167048064) == 0) {
                  ;
            }

            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                  ;
            }

      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var2 = var0.getClass().getDeclaredField("pressed");
                  var2.setAccessible((boolean)((0 >> 3 ^ 1833987440) >>> 3 ^ 229248431));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  if (((999808540 << 4 & 2121782564) << 2 ^ 1746099688) == 0) {
                  }

                  RuntimeException var10000 = new RuntimeException;
                  if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var10000.<init>(var3);
                  throw var10000;
            }
      }
}
