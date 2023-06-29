//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import java.lang.reflect.Field;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FastUse extends Module {
      public BooleanSetting xpBottle;

      @SubscribeEvent
      public void onPlayerTickEvent(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (this.xpBottle.isEnabled() && mc.player != null && (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)) {
                        FastUse.setDelayClick(mc, (2089099538 | 1919433062) & 1698285798 & 913617572 ^ 606076964);
                  }

            }
      }

      public FastUse() {
            super("FastUse", "use items faster", 1052502615 << 3 & 1346849730 & 120736111 ^ 147456, Module.Category.WORLD);
            if ((1686927731 >>> 4 >>> 3 >> 2 ^ 3294780) == 0) {
                  ;
            }

            BooleanSetting var10001 = new BooleanSetting;
            if (((5794265 >> 4 & 182483 & 5062 | 99) ^ 227) == 0) {
                  ;
            }

            var10001.<init>("xpBottle", this, (boolean)((0 << 1 | 683449739) << 1 ^ 1366899479));
            this.xpBottle = var10001;
            Setting[] var1 = new Setting[(0 | 1257934421) >> 4 >>> 4 ^ 4913807];
            var1[(590132065 | 422095234) >> 2 & 237470306 ^ 235078240] = this.xpBottle;
            this.addSettings(var1);
      }

      public static void setDelayClick(Minecraft var0, int var1) {
            try {
                  Field var2 = var0.getClass().getDeclaredField("rightClickDelayTimer");
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var2.setAccessible((boolean)(0 >>> 3 & 76608005 & 1465154139 ^ 1));
                  var2.setInt(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  throw new RuntimeException(var3);
            }
      }
}
