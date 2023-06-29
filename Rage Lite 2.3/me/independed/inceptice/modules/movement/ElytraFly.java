//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ElytraFly extends Module {
      public ModeSetting modeFlyElytra;
      public NumberSetting speed = new NumberSetting("Speed", this, 1.0D, 0.0D, 100.0D, 0.1D);

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  String var10000 = this.modeFlyElytra.activeMode;
                  if (((175696155 ^ 131819161) >> 2 >> 4 >>> 4 ^ 223459) == 0) {
                        ;
                  }

                  Minecraft var3;
                  EntityPlayerSP var4;
                  if (var10000 == "Fly") {
                        if (mc.player.capabilities.isFlying || mc.player.isElytraFlying()) {
                              mc.player.setSprinting((boolean)(675315856 >>> 1 << 2 ^ 1350631712));
                        }

                        var3 = mc;
                        if (!"ape covered in human flesh".equals("please take a shower")) {
                              ;
                        }

                        var4 = var3.player;
                        if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        PlayerCapabilities var5;
                        if (var4.capabilities.isFlying) {
                              if ((((1434133706 ^ 771877767) >>> 1 >> 1 ^ 421584276) & 52878183 ^ 52841031) == 0) {
                                    ;
                              }

                              mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                              mc.player.setPosition(mc.player.posX, mc.player.posY - 5.0000002374872565E-5D, mc.player.posZ);
                              var5 = mc.player.capabilities;
                              float var10001 = (float)this.speed.getValue();
                              if (((143225783 ^ 27884685) >>> 3 >> 2 ^ 1321683988) != 0) {
                                    ;
                              }

                              var5.setFlySpeed(var10001);
                              mc.player.setSprinting((boolean)((1877692356 | 612346296 | 556270889) ^ 1879044093));
                        }

                        if (((713130790 >> 1 ^ 102410984) >> 4 ^ 20293303) == 0) {
                              ;
                        }

                        if (mc.player.onGround) {
                              if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                                    ;
                              }

                              var5 = mc.player.capabilities;
                              if (((1817300859 ^ 1135724240 | 143870996) ^ 804474303) == 0) {
                                    ;
                              }

                              var5.allowFlying = (boolean)(((1789406006 | 334696685) & 1271784297 | 1023299030) ^ 2147446783);
                        }

                        if (mc.player.isElytraFlying()) {
                              mc.player.capabilities.setFlySpeed(0.915F);
                              var5 = mc.player.capabilities;
                              if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                                    ;
                              }

                              var5.isFlying = (boolean)(0 ^ 580074819 ^ 409155627 ^ 942296934 ^ 47860239);
                              var4 = mc.player;
                              if (((1917410618 >>> 1 >>> 2 & 98369676) >>> 1 ^ 35946562) == 0) {
                                    ;
                              }

                              if (!var4.capabilities.isCreativeMode) {
                                    if (!"ape covered in human flesh".equals("minecraft")) {
                                          ;
                                    }

                                    var5 = mc.player.capabilities;
                                    int var6 = (0 & 592987359) >> 3 >>> 4 ^ 1;
                                    if ((831656106 << 3 << 1 & 307581435 ^ -507217913) != 0) {
                                          ;
                                    }

                                    var5.allowFlying = (boolean)var6;
                              }
                        }
                  } else {
                        if (mc.player.isInWater()) {
                              mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
                              return;
                        }

                        GameSettings var7 = mc.gameSettings;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (var7.keyBindJump.isKeyDown()) {
                              if (((95219762 ^ 85544898) << 3 ^ 95289216) == 0) {
                                    ;
                              }

                              var4 = mc.player;
                              var4.motionY += 0.08D;
                        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                              if ((33558594 << 4 << 2 ^ 1491011529) != 0) {
                                    ;
                              }

                              var4 = mc.player;
                              var4.motionY -= 0.04D;
                              if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }
                        }

                        float var2;
                        if (mc.gameSettings.keyBindForward.isKeyDown()) {
                              var2 = (float)Math.toRadians((double)mc.player.rotationYaw);
                              var4 = mc.player;
                              if ((1027255411 >>> 2 ^ 103405328 ^ -30669331) != 0) {
                                    ;
                              }

                              var4.motionX -= (double)(MathHelper.sin(var2) * 0.05F);
                              if ((1927642253 >> 3 ^ 164195603 ^ 2075469988) != 0) {
                                    ;
                              }

                              var4 = mc.player;
                              var4.motionZ += (double)(MathHelper.cos(var2) * 0.05F);
                        } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
                              var3 = mc;
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please take a shower")) {
                                    ;
                              }

                              var2 = (float)Math.toRadians((double)var3.player.rotationYaw);
                              if (((72871685 ^ 32269099 ^ 31914954) >>> 2 ^ 18308601) == 0) {
                                    ;
                              }

                              var4 = mc.player;
                              var4.motionX += (double)(MathHelper.sin(var2) * 0.05F);
                              var4 = mc.player;
                              var4.motionZ -= (double)(MathHelper.cos(var2) * 0.05F);
                        }
                  }

            }
      }

      public ElytraFly() {
            super("ElytraFly", "fly on elytra!", 827089617 << 4 & 114071940 ^ 70980455 ^ 16718439, Module.Category.MOVEMENT);
            if (((1187341040 ^ 769628819) >>> 4 ^ 1081905003) != 0) {
                  ;
            }

            String[] var10006 = new String[(1 | 0 | 0) ^ 3];
            var10006[(1326681107 << 2 ^ 489711147) >>> 4 ^ 35120486] = "Fly";
            var10006[(0 >>> 3 >>> 4 ^ 274274962) >>> 2 ^ 68568741] = "Boost";
            this.modeFlyElytra = new ModeSetting("Mode", this, "Fly", var10006);
            Setting[] var10001 = new Setting[(0 & 2007787100 ^ 181155091) << 3 ^ 1449240730];
            var10001[((1077429120 ^ 220274444) >>> 1 & 521603359) << 3 ^ 807419952] = this.speed;
            var10001[(0 ^ 1319588202 ^ 521424235 | 494440448) ^ 1576770560] = this.modeFlyElytra;
            this.addSettings(var10001);
      }

      public void onDisable() {
            if (mc.player != null && mc.world != null) {
                  super.onDisable();
                  EntityPlayerSP var10000 = mc.player;
                  if (((683182511 >> 3 >>> 3 | 2148671) ^ 5072258 ^ 1420021731) != 0) {
                        ;
                  }

                  var10000.capabilities.isFlying = (boolean)((778269744 | 308771581) >>> 2 << 1 ^ 523485054);
                  PlayerCapabilities var1 = mc.player.capabilities;
                  if (((1000249348 | 649412895) >> 2 ^ -564468713) != 0) {
                        ;
                  }

                  var1.setFlySpeed(0.05F);
                  if (!mc.player.capabilities.isCreativeMode) {
                        mc.player.capabilities.allowFlying = (boolean)(262144 >>> 1 >>> 4 << 2 ^ '耀');
                  }

            }
      }
}
