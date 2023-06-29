//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class SetBack extends Module {
      public NumberSetting fallDistanceValue;
      public ModeSetting modeValue;
      private double prevY;
      private double prevX;
      private double prevZ;

      public void onDisable() {
            this.prevX = 0.0D;
            this.prevY = 0.0D;
            this.prevZ = 0.0D;
            if (((33575937 >>> 4 | 695477) >> 1 ^ 73797011) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  if (mc.player.onGround) {
                        Minecraft var10001 = mc;
                        if ((1022557505 >>> 2 >> 1 ^ 127819688) == 0) {
                              ;
                        }

                        this.prevX = var10001.player.posX;
                        this.prevY = mc.player.posY;
                        this.prevZ = mc.player.posZ;
                  }

                  double var10000 = (double)mc.player.fallDistance;
                  double var10 = this.fallDistanceValue.getValue();
                  if (!"please take a shower".equals("stop skidding")) {
                        ;
                  }

                  if (var10000 > var10) {
                        String var2 = this.modeValue.activeMode;
                        String var7 = var2.toLowerCase();
                        if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        String var3 = var7;
                        int var8 = (380796785 >> 4 | 19502329 | 20888527) ^ -25149440;
                        if (!"please dont crack my plugin".equals("intentMoment")) {
                              ;
                        }

                        int var5 = var8;
                        switch(var3.hashCode()) {
                        case -1718744413:
                              if (var3.equals("ongroundspoof")) {
                                    var5 = (1 << 4 >> 1 | 3) ^ 9;
                                    if (!"idiot".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }
                              }
                              break;
                        case -757065121:
                              if (var3.equals("flyflag")) {
                                    var5 = (0 ^ 1780389008 ^ 1065543753) & 437951751 ^ 270015488;
                              }
                              break;
                        case -198873454:
                              boolean var9 = var3.equals("teleportback");
                              if ((((317873577 ^ 146670625) & 414138095) >> 1 ^ 201722948) == 0) {
                                    ;
                              }

                              if (var9) {
                                    var5 = (2042517049 >> 4 | 110540527) << 4 >>> 2 ^ 511696828;
                              }
                        }

                        switch(var5) {
                        case 0:
                              mc.player.setPositionAndUpdate((double)((int)this.prevX), (double)((int)this.prevY), (double)((int)this.prevZ));
                              EntityPlayerSP var12 = mc.player;
                              if (((647776468 ^ 51872377) >>> 1 << 2 ^ 1096982831 ^ 175453303) == 0) {
                                    ;
                              }

                              var12.fallDistance = 0.0F;
                              mc.player.motionY = 0.0D;
                              if ((((546832752 ^ 355273414) >>> 1 | 73596157) ^ 519765503) == 0) {
                                    ;
                              }
                              break;
                        case 1:
                              EntityPlayerSP var6 = mc.player;
                              var10 = var6.motionY;
                              if ((((1224644242 | 725520555) & 1138981654 | 24804438) ^ 1140489814) == 0) {
                                    ;
                              }

                              var6.motionY = var10 + 0.1D;
                              mc.player.fallDistance = 0.0F;
                              break;
                        case 2:
                              NetHandlerPlayClient var11 = mc.player.connection;
                              if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              CPacketPlayer var13 = new CPacketPlayer;
                              int var10003 = 0 >>> 1 >>> 1 >> 2 ^ 1;
                              if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var13.<init>((boolean)var10003);
                              if ((1857407080 << 1 << 3 ^ -1707473427) != 0) {
                                    ;
                              }

                              var11.sendPacket(var13);
                        }
                  }

            }
      }

      public SetBack() {
            super("SetBack", "Automatically setbacks you after falling a certain distance.", 1653697731 >> 1 << 3 >>> 1 ^ 1159911812, Module.Category.WORLD);
            if (!"stop skidding".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            ModeSetting var10001 = new ModeSetting;
            if (((1957905719 << 1 | 1778016620 | 47066140) << 4 ^ -1074858016) == 0) {
                  ;
            }

            if (!"you're dogshit".equals("idiot")) {
                  ;
            }

            String[] var10006 = new String[(1 & 0) >>> 4 ^ 3];
            var10006[((2031198537 | 1680025024) ^ 1530191529) >> 1 ^ 319012272] = "FlyFlag";
            var10006[0 << 1 >>> 3 ^ 1] = "TeleportBack";
            if (((1764961880 << 1 | 205848580) >> 3 ^ -70456426) == 0) {
                  ;
            }

            var10006[(1 >>> 4 ^ 1409315319 | 257863820) ^ 1600057853] = "OnGroundSpoof";
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10001.<init>("Mode", this, "FlyFlag", var10006);
            this.modeValue = var10001;
            NumberSetting var1 = new NumberSetting;
            if ((((1399687464 | 1193226672) >>> 2 >>> 2 | 45850571) ^ 1410109120) != 0) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                  ;
            }

            var1.<init>("FallDist", this, 2.0D, 1.0D, 5.0D, 0.1D);
            this.fallDistanceValue = var1;
            Setting[] var2 = new Setting[(1 & 0) << 2 << 1 ^ 715523988 ^ 715523990];
            var2[2041384037 >>> 2 >>> 1 ^ 255173004] = this.modeValue;
            var2[(0 | 1955813084) << 4 << 1 ^ -1838490751] = this.fallDistanceValue;
            this.addSettings(var2);
      }
}
