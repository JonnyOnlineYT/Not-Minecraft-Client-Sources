//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer.Position;

public class KTLeave extends Module {
      private double toAdd;
      public NumberSetting highVis = new NumberSetting("Height", this, 100.0D, 1.0D, 500.0D, 1.0D);

      public KTLeave() {
            super("KTLeave", "100 blocks upper", 1919461251 >> 3 ^ 138605367 ^ 20625453 ^ 120938474, Module.Category.WORLD);
            Setting[] var10001 = new Setting[((0 & 471019435) >> 2 >> 1 | 84455893) ^ 84455892];
            var10001[(906370740 ^ 96974229) >>> 4 >> 3 ^ 6783830] = this.highVis;
            if (!"you probably spell youre as your".equals("please take a shower")) {
                  ;
            }

            this.addSettings(var10001);
      }

      public void onDisable() {
            super.onDisable();
            if (mc.world != null) {
                  EntityPlayerSP var10000 = mc.player;
                  if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (var10000 != null) {
                        mc.player.isDead = (boolean)(106346096 >> 3 >> 4 ^ 830828);
                  }
            }

      }

      public void onEnable() {
            super.onEnable();
            if (mc.world != null && mc.player != null) {
                  this.toAdd = mc.player.posY + this.highVis.getValue();

                  for(double var1 = mc.player.posY; var1 < this.toAdd; var1 += 2.0D) {
                        mc.player.setPositionAndUpdate(mc.player.posX, var1, mc.player.posZ);
                        mc.player.connection.sendPacket(new Position(mc.player.posX, var1, mc.player.posZ, (boolean)(0 >> 3 << 2 << 3 >> 3 ^ 159201732 ^ 159201733)));
                  }

                  EntityPlayerSP var10000 = mc.player;
                  if ((2077178375 >> 4 ^ 101233288 ^ 20785161 ^ 9005345) == 0) {
                        ;
                  }

                  int var10001 = (0 >>> 3 & 711152214) >>> 1 >>> 3 ^ 1;
                  if (!"yo mama name maurice".equals("please go outside")) {
                        ;
                  }

                  var10000.isDead = (boolean)var10001;
            }

      }
}
