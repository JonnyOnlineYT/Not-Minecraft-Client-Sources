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
      private double prevZ;
      public ModeSetting modeValue;
      private double prevY;
      private double prevX;
      public NumberSetting fallDistanceValue;

      public void onDisable() {
            this.prevX = 0.0D;
            if (((183517457 | 131311625) << 4 ^ 1596196993 ^ -534723649) != 0) {
                  ;
            }

            this.prevY = 0.0D;
            this.prevZ = 0.0D;
            if (((892966461 ^ 181560717 | 960543174) ^ -1638768769) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((571329346 ^ 76622938) >>> 2 & 107651635 ^ 2236418) == 0) {
                        ;
                  }

                  if (mc.world != null) {
                        if (mc.player.onGround) {
                              if (((109068426 | 40334303) >> 4 << 2 ^ -1108209595) != 0) {
                                    ;
                              }

                              this.prevX = mc.player.posX;
                              double var10001 = mc.player.posY;
                              if (((1412913262 ^ 1377146700) << 2 << 4 ^ 822576339) != 0) {
                                    ;
                              }

                              this.prevY = var10001;
                              this.prevZ = mc.player.posZ;
                        }

                        if ((double)mc.player.fallDistance > this.fallDistanceValue.getValue()) {
                              String var2 = this.modeValue.activeMode;
                              if ((70254848 >> 4 ^ 4390928) == 0) {
                                    ;
                              }

                              String var3 = var2.toLowerCase();
                              if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              if (((129793233 >> 2 | 5591172) ^ 171566352) != 0) {
                                    ;
                              }

                              int var5 = (671522689 >> 1 << 2 >> 2 | 292730369) ^ -359923650;
                              switch(var3.hashCode()) {
                              case -1718744413:
                                    if (var3.equals("ongroundspoof")) {
                                          int var10000 = (0 >>> 2 ^ 492086910) >>> 4 >>> 1 ^ 15377713;
                                          if (!"idiot".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var5 = var10000;
                                    }
                                    break;
                              case -757065121:
                                    if (var3.equals("flyflag")) {
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var5 = ((0 & 1533593951) >> 4 ^ 1470507098 | 1379785673) ^ 1472199642;
                                    }
                                    break;
                              case -198873454:
                                    if (var3.equals("teleportback")) {
                                          var5 = 1843256783 >> 2 >>> 1 ^ 230407097;
                                    }
                              }

                              switch(var5) {
                              case 0:
                                    Minecraft var9 = mc;
                                    if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    var9.player.setPositionAndUpdate((double)((int)this.prevX), (double)((int)this.prevY), (double)((int)this.prevZ));
                                    mc.player.fallDistance = 0.0F;
                                    mc.player.motionY = 0.0D;
                                    break;
                              case 1:
                                    EntityPlayerSP var6 = mc.player;
                                    var6.motionY += 0.1D;
                                    EntityPlayerSP var8 = mc.player;
                                    if (((1659621957 >> 3 << 4 | 2073881558) ^ -1675811171) != 0) {
                                          ;
                                    }

                                    if (((1090718234 ^ 71467066) >>> 3 ^ 145240388) == 0) {
                                          ;
                                    }

                                    var8.fallDistance = 0.0F;
                                    break;
                              case 2:
                                    NetHandlerPlayClient var7 = mc.player.connection;
                                    CPacketPlayer var10 = new CPacketPlayer;
                                    int var10003 = ((0 >> 4 ^ 786906109) >>> 1 | 232029192) ^ 536346111;
                                    if (!"nefariousMoment".equals("you're dogshit")) {
                                          ;
                                    }

                                    var10.<init>((boolean)var10003);
                                    var7.sendPacket(var10);
                              }
                        }

                        return;
                  }
            }

      }

      public SetBack() {
            super("SetBack", "Automatically setbacks you after falling a certain distance.", (2009849409 | 1622078269 | 891149548) & 826674117 ^ 826674117, Module.Category.WORLD);
            ModeSetting var10001 = new ModeSetting;
            int var10006 = (2 << 3 >> 3 ^ 1) << 4 ^ 51;
            if (((28125404 << 3 ^ 97760279 ^ 67866468) >> 3 & 26315514 ^ 732578421) != 0) {
                  ;
            }

            String[] var3 = new String[var10006];
            int var10008 = ((401694340 ^ 280093125) >>> 2 | 4093897) ^ 33456089;
            if (((((418716074 | 312622333) & 114927248) << 2 | 116746036) << 3 ^ 2142223264) == 0) {
                  ;
            }

            var3[var10008] = "FlyFlag";
            var3[(0 << 2 | 178348299) ^ 178348298] = "TeleportBack";
            var3[0 << 2 >> 4 >>> 4 << 3 ^ 2] = "OnGroundSpoof";
            var10001.<init>("Mode", this, "FlyFlag", var3);
            this.modeValue = var10001;
            NumberSetting var1 = new NumberSetting;
            if (!"shitted on you harder than archybot".equals("intentMoment")) {
                  ;
            }

            var1.<init>("FallDist", this, 2.0D, 1.0D, 5.0D, 0.1D);
            this.fallDistanceValue = var1;
            Setting[] var2 = new Setting[(1 >> 3 | 536141541 | 183947964) ^ 536272639];
            var2[2136772801 >> 1 >> 3 << 4 ^ 2136772800] = this.modeValue;
            if (!"shitted on you harder than archybot".equals("ape covered in human flesh")) {
                  ;
            }

            int var10003 = (0 >> 3 ^ 822640760) & 710033277 ^ 536871033;
            if (((278529042 ^ 245866932) << 4 ^ 1818747043) != 0) {
                  ;
            }

            var2[var10003] = this.fallDistanceValue;
            this.addSettings(var2);
      }
}
