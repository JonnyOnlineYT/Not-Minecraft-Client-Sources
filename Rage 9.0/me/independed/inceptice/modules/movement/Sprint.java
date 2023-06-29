//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Sprint extends Module {
      public ModeSetting modeSetting;

      public void onDisable() {
            super.onDisable();
            EntityPlayerSP var10000 = mc.player;
            if (((294279814 ^ 141516489 ^ 153108884) >>> 1 << 4 << 2 ^ 180672129) != 0) {
                  ;
            }

            if (var10000 != null) {
                  mc.player.setSprinting((boolean)(((1121775913 ^ 753886549) & 1749590994) >> 4 & 25619094 ^ 8390660));
            }
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (!"please go outside".equals("nefariousMoment")) {
                        ;
                  }

                  if (this.modeSetting.activeMode == "Simple") {
                        EntityPlayerSP var10000 = mc.player;
                        if ((717824268 >>> 3 >> 4 ^ 5608002) == 0) {
                              ;
                        }

                        if (var10000.moveForward > 0.0F && !mc.player.isSneaking()) {
                              var10000 = mc.player;
                              int var10001 = (0 ^ 1049232311 ^ 530405123 ^ 474965658) >> 1 ^ 514595094;
                              if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              var10000.setSprinting((boolean)var10001);
                        }
                  } else {
                        mc.player.setSprinting((boolean)((0 >>> 4 ^ 228854564) >>> 3 ^ 28606821));
                  }

                  if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

            }
      }

      public Sprint() {
            super("Sprint", "auto runs", (711185845 >>> 3 >>> 2 & 7217658) << 3 >> 4 ^ 2162772, Module.Category.MOVEMENT);
            String[] var10006 = new String[(0 >> 3 << 3 ^ 112044224) & 45014717 ^ 44861570];
            var10006[(1805706332 ^ 741417343) >>> 1 >> 3 ^ 75045970] = "Simple";
            var10006[(0 & 804669274 ^ 2130352203 | 1083839178) ^ 2130352842] = "Rage";
            this.modeSetting = new ModeSetting("Mode", this, "Simple", var10006);
            Setting[] var10001 = new Setting[(0 ^ 1489647498) >> 1 ^ 744823748];
            var10001[(493106589 << 3 << 3 | 779607907) ^ 2139094883] = this.modeSetting;
            this.addSettings(var10001);
      }
}
