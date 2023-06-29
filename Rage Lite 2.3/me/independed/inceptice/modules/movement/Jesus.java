//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Jesus extends Module {
      public NumberSetting jumpHeight;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (mc.player.isInWater()) {
                        mc.player.motionY = this.jumpHeight.getValue();
                  }

            }
      }

      public Jesus() {
            if (((325441194 ^ 234351202 ^ 446074829) & 38270044 ^ 342422772) != 0) {
                  ;
            }

            super("Jesus", "walk on water", (154174596 >> 3 ^ 559803) >>> 2 ^ 4957834, Module.Category.MOVEMENT);
            if (((91264456 >> 1 ^ 13278236) >> 3 << 4 ^ 544562759) != 0) {
                  ;
            }

            this.jumpHeight = new NumberSetting("JumpHeight", this, 3.0D, 0.1D, 10.0D, 0.1D);
            int var10001 = (0 >> 3 >>> 3 >>> 3 ^ 1477615806) >> 4 ^ 92350986;
            if (!"please get a girlfriend and stop cracking plugins".equals("please take a shower")) {
                  ;
            }

            Setting[] var1 = new Setting[var10001];
            var1[1107566051 << 1 >> 1 & 427094076 ^ 266272] = this.jumpHeight;
            this.addSettings(var1);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("minecraft")) {
                  ;
            }

      }
}
