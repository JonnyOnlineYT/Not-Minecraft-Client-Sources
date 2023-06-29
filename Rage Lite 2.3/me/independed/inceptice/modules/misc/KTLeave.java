//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayer.Position;

public class KTLeave extends Module {
      public NumberSetting highVis = new NumberSetting("Height", this, 100.0D, 1.0D, 500.0D, 1.0D);
      private double toAdd;

      public void onDisable() {
            super.onDisable();
            if (mc.world != null && mc.player != null) {
                  mc.player.isDead = (boolean)(33554692 >> 2 >>> 1 ^ 3674946 ^ 7869282);
            }

      }

      public void onEnable() {
            if (!"stringer is a good obfuscator".equals("stop skidding")) {
                  ;
            }

            super.onEnable();
            WorldClient var10000 = mc.world;
            if (((166603863 | 110437103) << 2 ^ 1029868330 ^ 354171913) != 0) {
                  ;
            }

            if (var10000 != null && mc.player != null) {
                  this.toAdd = mc.player.posY + this.highVis.getValue();
                  double var1 = mc.player.posY;

                  while(true) {
                        if (!"intentMoment".equals("nefariousMoment")) {
                              ;
                        }

                        if (var1 >= this.toAdd) {
                              mc.player.isDead = (boolean)((0 >>> 2 & 1683818193) >>> 3 ^ 1);
                              break;
                        }

                        mc.player.setPositionAndUpdate(mc.player.posX, var1, mc.player.posZ);
                        NetHandlerPlayClient var3 = mc.player.connection;
                        if (!"minecraft".equals("please go outside")) {
                              ;
                        }

                        var3.sendPacket(new Position(mc.player.posX, var1, mc.player.posZ, (boolean)((0 >>> 2 | 1144159623) ^ 1144159622)));
                        var1 += 2.0D;
                  }
            }

      }

      public KTLeave() {
            super("KTLeave", "100 blocks upper", (2934952 ^ 67456) << 1 & 3402409 ^ 1280512, Module.Category.WORLD);
            Setting[] var10001 = new Setting[(0 << 1 >>> 2 >>> 1 | 1205947924) ^ 1205947925];
            var10001[34738192 << 3 >> 2 ^ 69476384] = this.highVis;
            this.addSettings(var10001);
      }
}
