//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Fly extends Module {
      public NumberSetting flyHeight;

      public void onDisable() {
            if (mc.player != null) {
                  super.onDisable();
            }
      }

      public Fly() {
            if ((((1667711492 | 718290093) << 1 | 1773647647) >> 4 >> 3 ^ -2) == 0) {
                  ;
            }

            super("Fly", "you can fly", (616391635 >>> 1 << 3 | 1856156543) ^ 1561610485 ^ -1545341046, Module.Category.MOVEMENT);
            NumberSetting var10001 = new NumberSetting;
            if (((664730302 | 654858647) & 144973722 & 3804319 ^ 2202) == 0) {
                  ;
            }

            var10001.<init>("HeightJump", this, 0.4D, 0.1D, 5.0D, 0.1D);
            this.flyHeight = var10001;
            int var1 = 0 >>> 3 ^ 808192677 ^ 245316466 ^ 55774300 ^ 683661587 ^ 358605977;
            if (((2058733719 ^ 619645858 | 396135540 | 422740400) << 1 ^ 1279818606 ^ -206183292) == 0) {
                  ;
            }

            Setting[] var2 = new Setting[var1];
            int var10003 = (822318432 | 167430164) >> 2 ^ 243201885;
            if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var2[var10003] = this.flyHeight;
            this.addSettings(var2);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if ((1531824689 << 4 >> 1 ^ -630304376) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if (mc.gameSettings.keyBindJump.isPressed() && !mc.player.onGround) {
                        mc.player.motionY = this.flyHeight.getValue();
                        Minecraft var10000 = mc;
                        if ((239380084 << 2 >>> 4 >>> 4 ^ 3740313) == 0) {
                              ;
                        }

                        double var2 = var10000.player.posX;
                        if ((786334205 >> 3 >> 3 ^ 12286471) == 0) {
                              ;
                        }

                        double var4 = mc.player.posY;
                        double var6 = mc.player.posZ;
                        if (((209932584 ^ 136083957 ^ 15649547) << 3 ^ 1763131475) != 0) {
                              ;
                        }

                        NetHandlerPlayClient var8 = mc.player.connection;
                        PositionRotation var10001 = new PositionRotation;
                        double var10004 = var4 + this.flyHeight.getValue();
                        EntityPlayerSP var10006 = mc.player;
                        if (((1858187742 >> 1 >>> 1 >>> 2 >> 4 | 4133904) ^ 8377777) == 0) {
                              ;
                        }

                        var10001.<init>(var2, var10004, var6, var10006.rotationYaw, mc.player.rotationPitch, (boolean)(0 << 4 >> 3 ^ 153774721 ^ 153774720));
                        if (!"yo mama name maurice".equals("please go outside")) {
                              ;
                        }

                        var8.sendPacket(var10001);
                        if ((270135683 << 1 & 97115919 ^ 73127 ^ 1991927130) != 0) {
                              ;
                        }

                        EntityPlayerSP var9 = mc.player;
                        double var10002 = var4 + this.flyHeight.getValue();
                        float var10 = mc.player.rotationYaw;
                        if (!"ape covered in human flesh".equals("intentMoment")) {
                              ;
                        }

                        var9.setPositionAndRotationDirect(var2, var10002, var6, var10, mc.player.rotationPitch, ((0 | 1768751194) & 1405462571 ^ 876619452) & 463331336 ^ 285548545, (boolean)((1185946341 << 3 & 810307675) >> 3 & 5476909 ^ 513));
                  }

            }
      }
}
