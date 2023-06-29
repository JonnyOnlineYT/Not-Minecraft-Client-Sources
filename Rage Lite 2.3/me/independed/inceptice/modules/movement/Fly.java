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

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if ((1288529999 >> 3 >> 3 & 2522004 ^ 2240896) == 0) {
                  ;
            }

            if (mc.player != null) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  if (mc.gameSettings.keyBindJump.isPressed() && !mc.player.onGround) {
                        Minecraft var10000 = mc;
                        if (((1325777326 ^ 502974234 ^ 469673840) & 314013514 ^ 74048) == 0) {
                              ;
                        }

                        var10000.player.motionY = this.flyHeight.getValue();
                        double var2 = mc.player.posX;
                        double var4 = mc.player.posY;
                        double var6 = mc.player.posZ;
                        if (((13342740 | 5902433) >>> 2 >>> 2 ^ 28442953) != 0) {
                              ;
                        }

                        NetHandlerPlayClient var8 = mc.player.connection;
                        PositionRotation var10001 = new PositionRotation;
                        double var10004 = var4 + this.flyHeight.getValue();
                        float var10006 = mc.player.rotationYaw;
                        if ((620619858 ^ 9183686 ^ 266773316 ^ -1957087841) != 0) {
                              ;
                        }

                        EntityPlayerSP var10007 = mc.player;
                        if (((960643744 ^ 822225985 | 96522060) ^ 2637665 ^ -132852180) != 0) {
                              ;
                        }

                        var10001.<init>(var2, var10004, var6, var10006, var10007.rotationPitch, (boolean)((0 | 124871006) << 1 ^ 249742013));
                        var8.sendPacket(var10001);
                        var10000 = mc;
                        if (!"idiot".equals("nefariousMoment")) {
                              ;
                        }

                        EntityPlayerSP var9 = var10000.player;
                        double var10002 = var4 + this.flyHeight.getValue();
                        float var10 = mc.player.rotationYaw;
                        float var10005 = mc.player.rotationPitch;
                        int var11 = (0 & 692210375) >>> 4 ^ 2120499756 ^ 2120499757;
                        if (((1235288581 | 1051264596) & 372802777 ^ 351317024 ^ 3932848 ^ 48540353) == 0) {
                              ;
                        }

                        var9.setPositionAndRotationDirect(var2, var10002, var6, var10, var10005, var11, (boolean)((888528442 >> 2 | 97829240) & 91090175 ^ 91088126));
                  }

            }
      }

      public Fly() {
            super("Fly", "you can fly", (1019779860 >>> 3 ^ 3173338 | 91515245) ^ 134052349, Module.Category.MOVEMENT);
            if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            this.flyHeight = new NumberSetting("HeightJump", this, 0.4D, 0.1D, 5.0D, 0.1D);
            int var10001 = 0 >>> 4 >>> 2 >>> 4 ^ 1;
            if (((1424153674 ^ 91388480) >>> 2 << 4 ^ -283569849) != 0) {
                  ;
            }

            Setting[] var1 = new Setting[var10001];
            var1[(1688669486 ^ 191845544 ^ 1034136925 | 1234728957) ^ 1543239679] = this.flyHeight;
            this.addSettings(var1);
      }

      public void onDisable() {
            if (mc.player != null) {
                  if (((331212805 | 114628256 | 189921450) ^ 536739503) == 0) {
                        ;
                  }

                  super.onDisable();
            }
      }
}
