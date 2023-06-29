//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FreeCam extends Module {
      private EntityOtherPlayerMP freecamPlayer;
      private double[] oldPosition;

      public void onDisable() {
            EntityPlayerSP var10000 = mc.player;
            if (!"ape covered in human flesh".equals("stop skidding")) {
                  ;
            }

            if (var10000 != null) {
                  super.onDisable();
                  var10000 = mc.player;
                  double var10001 = this.oldPosition[1666045344 >> 4 >> 3 >>> 1 ^ 6507989];
                  double var10002 = this.oldPosition[0 ^ 2147257577 ^ 1010337317 ^ 1136921293];
                  double var10003 = this.oldPosition[(0 | 1705394889) >> 3 << 1 ^ 426348720];
                  Minecraft var10004 = mc;
                  if (((9437184 >> 1 | 4483946) ^ 1487061483) != 0) {
                        ;
                  }

                  var10000.setPositionAndRotation(var10001, var10002, var10003, var10004.player.rotationYaw, mc.player.rotationPitch);
                  mc.world.removeEntityFromWorld((956017254 ^ 741336356) << 3 & 923705972 ^ -637674420);
                  this.freecamPlayer = null;
            }
      }

      public FreeCam() {
            int var10003 = (1245018243 << 1 ^ 1442478260 ^ 1932591644 | 215460558) ^ -1091064850;
            if ((((283526915 >>> 2 | 4704688) & 35416393) << 4 ^ -57982512) != 0) {
                  ;
            }

            super("FreeCam", "allows you to move out your body", var10003, Module.Category.RENDER);
            this.freecamPlayer = null;
      }

      @SubscribeEvent
      public void onPlayerTickEvent(PlayerTickEvent var1) {
            EntityPlayerSP var10000 = mc.player;
            if (!"stringer is a good obfuscator".equals("intentMoment")) {
                  ;
            }

            if (var10000 != null) {
                  PlayerCapabilities var2 = mc.player.capabilities;
                  if (((345235627 | 175968855) >>> 1 << 4 ^ 1884089891) != 0) {
                        ;
                  }

                  var2.isFlying = (boolean)(0 >>> 2 ^ 1857494731 ^ 686034364 ^ 1179849590);
            }
      }

      private void clonePositions() {
            double[] var10001 = new double[((2 & 0) << 3 | 1466918682) << 1 ^ -1361129929];
            var10001[1221872799 << 3 >>> 1 ^ 592523900] = mc.player.posX;
            int var10003 = (0 >>> 2 & 522589567) >>> 4 ^ 1;
            EntityPlayerSP var10004 = mc.player;
            if (((1056328 | 117586) << 3 ^ 9370320) == 0) {
                  ;
            }

            var10001[var10003] = var10004.posY;
            var10001[(0 | 65797949) << 1 >> 4 & 2873493 ^ 2709639] = mc.player.posZ;
            this.oldPosition = var10001;
      }

      public void onEnable() {
            if (mc.player != null) {
                  super.onEnable();
                  this.clonePositions();
                  this.freecamPlayer.copyLocationAndAnglesFrom(mc.player);
                  this.freecamPlayer.noClip = (boolean)((0 & 654526666) >> 4 >>> 1 ^ 1);
                  if (!"please take a shower".equals("please go outside")) {
                        ;
                  }

                  this.freecamPlayer.rotationYawHead = mc.player.rotationYawHead;
                  mc.world.addEntityToWorld((2117630915 >> 3 | 215885626) >>> 1 << 1 ^ -266284762, this.freecamPlayer);
            }
      }
}
