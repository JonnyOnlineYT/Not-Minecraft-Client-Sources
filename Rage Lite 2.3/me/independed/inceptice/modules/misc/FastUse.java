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

      public FastUse() {
            if (!"stringer is a good obfuscator".equals("i hope you catch fire ngl")) {
                  ;
            }

            super("FastUse", "use items faster", 1339775443 << 4 ^ 1721014078 ^ -1692333554, Module.Category.WORLD);
            BooleanSetting var10001 = new BooleanSetting;
            if (((139766885 | 92431446 | 82025718) ^ -1616354094) != 0) {
                  ;
            }

            var10001.<init>("xpBottle", this, (boolean)((0 | 237743993) << 2 ^ 950975973));
            if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.xpBottle = var10001;
            Setting[] var1 = new Setting[(0 | 166935072) << 2 << 4 << 2 ^ -214294527];
            var1[(1700205112 >>> 1 & 767780500) >>> 1 ^ 272728074] = this.xpBottle;
            this.addSettings(var1);
      }

      public static void setDelayClick(Minecraft var0, int var1) {
            try {
                  if (!"please go outside".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  Field var2 = var0.getClass().getDeclaredField("rightClickDelayTimer");
                  if ((1024 ^ 959 ^ 1489976323) != 0) {
                        ;
                  }

                  var2.setAccessible((boolean)((0 & 516280076 ^ 1471909434) >> 4 ^ 31254652 ^ 78074014));
                  var2.setInt(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  RuntimeException var10000 = new RuntimeException;
                  if ("your mom your dad the one you never had".equals("intentMoment")) {
                  }

                  var10000.<init>(var3);
                  throw var10000;
            }
      }

      @SubscribeEvent
      public void onPlayerTickEvent(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (!"idiot".equals("yo mama name maurice")) {
                        ;
                  }

                  if (this.xpBottle.isEnabled() && mc.player != null) {
                        if (((1168697290 ^ 319900087) << 3 & 67491384 ^ -418689776) != 0) {
                              ;
                        }

                        if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                              Minecraft var10000 = mc;
                              if ((257615568 << 3 << 2 << 1 ^ -1326367992) != 0) {
                                    ;
                              }

                              FastUse.setDelayClick(var10000, ((1238497028 | 725263968) & 70872711) << 4 >>> 4 ^ 3761668);
                        }
                  }

                  if ((1642696870 >>> 2 & 151268169 ^ 134225929) == 0) {
                        ;
                  }

            }
      }
}
