//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FreeCam extends Module {
      private double[] oldPosition;
      private EntityOtherPlayerMP freecamPlayer = null;

      private void clonePositions() {
            double[] var10001 = new double[(1 >>> 2 << 1 | 114788118) ^ 114788117];
            var10001[(110856214 >> 1 | 22178352 | 40224313) >> 1 ^ 29357853] = mc.player.posX;
            var10001[(0 >>> 4 ^ 576239257) & 511730742 ^ 33563665] = mc.player.posY;
            var10001[1 >> 3 >>> 2 >>> 2 ^ 2] = mc.player.posZ;
            this.oldPosition = var10001;
      }

      public void onDisable() {
            if (mc.player != null) {
                  super.onDisable();
                  EntityPlayerSP var10000 = mc.player;
                  if (((37026014 | 33630667) << 3 >> 1 >> 2 >> 3 ^ 55341094) != 0) {
                        ;
                  }

                  double var10001 = this.oldPosition[487565621 << 2 << 4 ^ 65973991 ^ 1074062247];
                  if ((968867126 >> 3 & 75266592 ^ 70545952) == 0) {
                        ;
                  }

                  double var10002 = this.oldPosition[((0 ^ 1567827375) >> 2 >>> 2 << 3 | 459322762) ^ 1073330139];
                  double var10003 = this.oldPosition[(0 ^ 1360827892) >> 4 >>> 4 ^ 5315735];
                  Minecraft var10004 = mc;
                  if (!"stop skidding".equals("please take a shower")) {
                        ;
                  }

                  var10000.setPositionAndRotation(var10001, var10002, var10003, var10004.player.rotationYaw, mc.player.rotationPitch);
                  mc.world.removeEntityFromWorld((1638611590 ^ 1202324680) << 1 ^ -1275231552);
                  this.freecamPlayer = null;
            }
      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
                  this.clonePositions();
                  this.freecamPlayer.copyLocationAndAnglesFrom(mc.player);
                  EntityOtherPlayerMP var10000 = this.freecamPlayer;
                  if (((915392854 ^ 145103279) << 1 ^ 1128734897 ^ 803626090 ^ 803822367) != 0) {
                        ;
                  }

                  var10000.noClip = (boolean)(0 >> 1 >>> 2 >>> 4 << 2 & 749612882 ^ 1);
                  this.freecamPlayer.rotationYawHead = mc.player.rotationYawHead;
                  mc.world.addEntityToWorld((882157314 ^ 336482080) >>> 2 >>> 4 ^ 2605692 ^ -10856576, this.freecamPlayer);
            }
      }

      @SubscribeEvent
      public void onPlayerTickEvent(PlayerTickEvent var1) {
            if (mc.player != null) {
                  mc.player.capabilities.isFlying = (boolean)((0 & 809761170) << 4 & 1907076766 ^ 1);
            }
      }

      public FreeCam() {
            super("FreeCam", "allows you to move out your body", (193264973 ^ 110017493 ^ 196381644 | 35923168) ^ 113176052, Module.Category.RENDER);
      }
}
