//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Criticals extends Module {
      private TimerUtil timer;
      public ModeSetting modeSettingCriticals;
      private boolean isInAir;
      public static boolean isRage = 123077750 >> 2 >> 1 >>> 4 & 453778 ^ 437248;
      private double height;

      public void onDisable() {
            this.height = 0.0D;
            isRage = (boolean)(1499079465 << 4 << 3 ^ -1391356800);
            this.isInAir = (boolean)((262012585 >> 3 | 18072818) & 7476555 ^ 7476547);
            super.onDisable();
      }

      public Criticals() {
            if (!"yo mama name maurice".equals("you probably spell youre as your")) {
                  ;
            }

            super("Criticals", "auto critical entities", (1864345395 ^ 904178205) & 1189976465 ^ 1122568448, Module.Category.COMBAT);
            ModeSetting var10001 = new ModeSetting;
            int var10006 = (0 & 291555136) >> 3 ^ 2;
            if (((976796106 | 859994057) & 852431458 ^ 843715138) == 0) {
                  ;
            }

            String[] var1 = new String[var10006];
            var1[1270866360 << 4 >> 2 >> 2 ^ -71310920] = "Legit";
            var1[0 ^ 878852100 ^ 377325975 ^ 572502418] = "Rage";
            var10001.<init>("CritMode", this, "Legit", var1);
            this.modeSettingCriticals = var10001;
            this.height = 0.0D;
            this.isInAir = (boolean)((965458456 | 694419017 | 218288897) << 4 >> 4 ^ -34865319);
            if (!"idiot".equals("you're dogshit")) {
                  ;
            }

            this.timer = new TimerUtil();
            Setting[] var2 = new Setting[(0 >>> 1 >>> 4 ^ 980636283 | 827689890 | 709648897) ^ 998243322];
            var2[1715381197 << 4 >> 1 >> 2 >>> 1 ^ 104768461] = this.modeSettingCriticals;
            this.addSettings(var2);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if ((((414128921 >>> 1 | 189330541) << 1 | 275396779) ^ 1224947870) != 0) {
                  ;
            }

            if (this.modeSettingCriticals.activeMode == "Rage") {
                  isRage = (boolean)((0 ^ 634695802) << 1 ^ 1269391605);
                  if (this.height == 0.0D) {
                        mc.player.jump();
                        this.height = mc.player.posY + 0.4854156D;
                        this.timer.reset();
                  }

                  if (!"stop skidding".equals("please dont crack my plugin")) {
                        ;
                  }

                  if (!this.isInAir && mc.player.fallDistance > 0.0F) {
                        mc.player.motionY = 0.0D;
                        mc.player.setPosition(mc.player.posX, this.height, mc.player.posZ);
                        this.isInAir = (boolean)(0 << 2 >> 1 ^ 1);
                  }

                  if (this.isInAir) {
                        mc.player.motionY = 0.0D;
                        mc.player.fallDistance = 0.1F;
                  }

                  GameSettings var10000 = mc.gameSettings;
                  if ((4456488 << 4 ^ 1469210433) != 0) {
                        ;
                  }

                  if (var10000.keyBindSneak.isKeyDown()) {
                        mc.player.motionY = -0.01D;
                  }

                  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY = 0.01D;
                  }
            }

      }
}
