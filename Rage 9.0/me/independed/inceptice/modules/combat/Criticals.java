//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Criticals extends Module {
      private boolean isInAir;
      public static boolean isRage = (1708529113 | 737414559) << 3 >>> 1 & 327611471 ^ 327185484;
      private TimerUtil timer;
      private double height;
      public ModeSetting modeSettingCriticals;

      public void onDisable() {
            this.height = 0.0D;
            isRage = (boolean)(20756 >> 1 ^ 5933 ^ 16295);
            int var10001 = (601340073 ^ 92208858 | 462607641) ^ 1069275003;
            if ((305145562 >>> 2 << 4 << 1 ^ -62465271) != 0) {
                  ;
            }

            this.isInAir = (boolean)var10001;
            super.onDisable();
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (this.modeSettingCriticals.activeMode == "Rage") {
                  isRage = (boolean)((0 >> 4 & 559895720 & 1666454537) >>> 2 ^ 1);
                  double var10000 = this.height;
                  if ((807671544 << 4 >>> 2 << 4 & 30142758 ^ 2825193 ^ 19573737) == 0) {
                        ;
                  }

                  if (var10000 == 0.0D) {
                        mc.player.jump();
                        this.height = mc.player.posY + 0.4854156D;
                        if ((62439280 >> 3 << 1 << 2 ^ 899482096) != 0) {
                              ;
                        }

                        this.timer.reset();
                  }

                  if (!this.isInAir) {
                        Minecraft var2 = mc;
                        if (((328074462 | 197083828) & 15755966 ^ 1970493576) != 0) {
                              ;
                        }

                        if (var2.player.fallDistance > 0.0F) {
                              mc.player.motionY = 0.0D;
                              mc.player.setPosition(mc.player.posX, this.height, mc.player.posZ);
                              int var10001 = 0 & 2008353567 & 1343814426 ^ 1;
                              if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    ;
                              }

                              this.isInAir = (boolean)var10001;
                        }
                  }

                  if (this.isInAir) {
                        mc.player.motionY = 0.0D;
                        EntityPlayerSP var3 = mc.player;
                        if ((32 ^ 28 ^ 60) == 0) {
                              ;
                        }

                        var3.fallDistance = 0.1F;
                  }

                  if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        if (((136925485 >> 4 | 1358306) >>> 2 << 2 ^ 680031462) != 0) {
                              ;
                        }

                        mc.player.motionY = -0.01D;
                  }

                  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.motionY = 0.01D;
                  }
            }

      }

      public Criticals() {
            if ((801946805 << 1 << 4 ^ 1033341561) != 0) {
                  ;
            }

            if (!"nefariousMoment".equals("ape covered in human flesh")) {
                  ;
            }

            super("Criticals", "auto critical entities", (207630146 << 1 | 282580283 | 274121096) >> 2 ^ 104200175, Module.Category.COMBAT);
            if (!"you're dogshit".equals("please dont crack my plugin")) {
                  ;
            }

            ModeSetting var10001 = new ModeSetting;
            if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                  ;
            }

            String[] var10006 = new String[((0 ^ 1323588856) & 614606905 | 69962242) ^ 78350904];
            int var10008 = (998547059 | 277968759) & 188500062 ^ 185878614;
            if (((632750955 >>> 3 >> 3 | 2365313) ^ 11984781) == 0) {
                  ;
            }

            var10006[var10008] = "Legit";
            var10006[(0 << 1 | 1187467989 | 454269005) ^ 1607981788] = "Rage";
            var10001.<init>("CritMode", this, "Legit", var10006);
            this.modeSettingCriticals = var10001;
            if (((140575830 | 72220961 | 69358485) >> 2 ^ 551000869) != 0) {
                  ;
            }

            this.height = 0.0D;
            int var1 = (761221470 << 2 & 747174101) << 1 ^ 1209041056;
            if ((((1155537031 | 883087551) ^ 626360595 ^ 425923827) >>> 2 ^ 305468247) == 0) {
                  ;
            }

            this.isInAir = (boolean)var1;
            this.timer = new TimerUtil();
            Setting[] var2 = new Setting[(0 & 1740605206) >>> 1 << 1 ^ 1218624139 ^ 1218624138];
            var2[((696461036 ^ 213253259) & 13250955) >>> 4 ^ 8448] = this.modeSettingCriticals;
            this.addSettings(var2);
      }
}
