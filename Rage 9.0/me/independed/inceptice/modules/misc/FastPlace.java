//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FastPlace extends Module {
      public FastPlace() {
            if ((915671301 << 1 >> 1 ^ 915671301) == 0) {
                  ;
            }

            super("FastPlace", "fast placing blocks", ((67656 | 28436 | 67703) << 2 | 200076) ^ 507388, Module.Category.WORLD);
      }

      public static void setPressed(Minecraft var0) {
            try {
                  Field var1 = var0.getClass().getDeclaredField("rightClickDelayTimer");
                  if (((1174702333 >>> 4 >> 3 ^ 8674031) & 250733 ^ 21356) == 0) {
                        ;
                  }

                  var1.setAccessible((boolean)((0 ^ 2023610509 | 240715973) ^ 2128468172));
                  if (((1112790040 >> 1 ^ 386841570 | 822332291) ^ -626762463) != 0) {
                        ;
                  }

                  var1.setInt(var0, 1050632 ^ 1050632);
            } catch (ReflectiveOperationException var2) {
                  if ((501585548 >> 1 >>> 1 ^ 49137468 ^ -567424542) == 0) {
                  }

                  RuntimeException var10000 = new RuntimeException(var2);
                  if ((85987905 ^ 22895125 ^ 1206437477) != 0) {
                        ;
                  }

                  throw var10000;
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  FastPlace.setPressed(mc);
            }
      }
}
