//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Sprint extends Module {
      public ModeSetting modeSetting;

      public void onDisable() {
            super.onDisable();
            if (((947639451 ^ 938406521) >>> 3 >>> 3 >> 3 & 429328 ^ 1428001052) != 0) {
                  ;
            }

            if (mc.player != null) {
                  mc.player.setSprinting((boolean)((1325642324 << 3 >> 2 ^ 52238137) & 456705275 ^ 421019793));
            }
      }

      public Sprint() {
            if (!"buy a domain and everything else you need at namecheap.com".equals("idiot")) {
                  ;
            }

            super("Sprint", "auto runs", (1498276323 >>> 1 | 343794506) >> 1 >>> 2 ^ 127925887, Module.Category.MOVEMENT);
            ModeSetting var10001 = new ModeSetting;
            int var10006 = ((0 ^ 467095812) >>> 2 | 6535909 | 22334112) ^ 133693159;
            if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                  ;
            }

            String[] var1 = new String[var10006];
            var1[((170704566 | 46773605) ^ 34935033) << 1 ^ 301030940] = "Simple";
            var1[(0 & 179332963) >>> 2 ^ 1] = "Rage";
            var10001.<init>("Mode", this, "Simple", var1);
            this.modeSetting = var10001;
            Setting[] var2 = new Setting[((0 | 1417622413 | 332532825) ^ 1230384803) >> 4 ^ 32149782];
            var2[1349868834 << 1 >> 4 >> 1 ^ -49850926] = this.modeSetting;
            this.addSettings(var2);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (this.modeSetting.activeMode == "Simple") {
                        float var4;
                        int var10000 = (var4 = mc.player.moveForward - 0.0F) == 0.0F ? 0 : (var4 < 0.0F ? -1 : 1);
                        if (((1906481987 >> 2 | 16393660 | 34609223) ^ 519749631) == 0) {
                              ;
                        }

                        if (var10000 > 0 && !mc.player.isSneaking()) {
                              mc.player.setSprinting((boolean)((0 << 2 & 1871392360) >>> 4 ^ 1));
                        }
                  } else {
                        Minecraft var2 = mc;
                        if (!"please dont crack my plugin".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        EntityPlayerSP var3 = var2.player;
                        if (((99856875 >> 4 | 3907765) >> 2 << 2 ^ 203945574) != 0) {
                              ;
                        }

                        var3.setSprinting((boolean)((0 & 1149239662) >> 4 ^ 1));
                  }

            }
      }
}
