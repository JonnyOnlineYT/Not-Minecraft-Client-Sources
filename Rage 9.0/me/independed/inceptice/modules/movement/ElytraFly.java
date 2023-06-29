//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ElytraFly extends Module {
      public ModeSetting modeFlyElytra;
      public NumberSetting speed = new NumberSetting("Speed", this, 1.0D, 0.0D, 100.0D, 0.1D);

      public void onDisable() {
            if (mc.player != null && mc.world != null) {
                  super.onDisable();
                  if (((2205029 ^ 365884) >>> 2 ^ 698268903) != 0) {
                        ;
                  }

                  mc.player.capabilities.isFlying = (boolean)(((570296033 | 298811970 | 809440339) & 214251706) >>> 2 ^ 3228716);
                  if ((280645521 << 2 ^ 128675467 ^ 962475598 ^ 2082236033) == 0) {
                        ;
                  }

                  mc.player.capabilities.setFlySpeed(0.05F);
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("ape covered in human flesh")) {
                        ;
                  }

                  Minecraft var10000 = mc;
                  if ((((1276312570 << 2 | 457554815) & 660597017 | 298748504) ^ -1445022168) != 0) {
                        ;
                  }

                  if (!var10000.player.capabilities.isCreativeMode) {
                        PlayerCapabilities var1 = mc.player.capabilities;
                        int var10001 = ((1492511647 ^ 16594307) >> 2 | 183566277) ^ 519241671;
                        if (((1340088124 << 4 ^ 181425311) << 1 >> 3 ^ -46896681) == 0) {
                              ;
                        }

                        var1.allowFlying = (boolean)var10001;
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                        ;
                  }

                  if ((1040912505 >>> 1 << 4 ^ 1456902327 ^ -1500638345) == 0) {
                        ;
                  }

            } else {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                        ;
                  }

            }
      }

      public ElytraFly() {
            super("ElytraFly", "fly on elytra!", (((670935231 | 9437368) ^ 93234696) << 3 | 136272492) ^ 463437820, Module.Category.MOVEMENT);
            ModeSetting var10001 = new ModeSetting;
            String[] var10006 = new String[(1 >> 4 & 205719129) << 3 ^ 2];
            if (((237548596 >>> 2 | 23050113) ^ 51336412 ^ 4905675 ^ 10105242) == 0) {
                  ;
            }

            var10006[1536573518 >> 3 >> 2 << 2 ^ 192071688] = "Fly";
            var10006[(0 ^ 450648043 ^ 59351575) >>> 4 ^ 26566494] = "Boost";
            var10001.<init>("Mode", this, "Fly", var10006);
            this.modeFlyElytra = var10001;
            Setting[] var1 = new Setting[1 >>> 4 >>> 3 ^ 732914330 ^ 732914328];
            var1[1646354961 << 2 << 1 & 140129487 ^ 659592] = this.speed;
            var1[(0 | 1396139292) << 3 ^ -1715787551] = this.modeFlyElytra;
            this.addSettings(var1);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null && mc.world != null) {
                  double var10001;
                  EntityPlayerSP var3;
                  if (this.modeFlyElytra.activeMode == "Fly") {
                        boolean var10000 = mc.player.capabilities.isFlying;
                        if ((709198771 << 4 & 1654943401 ^ 536881696) == 0) {
                              ;
                        }

                        if (var10000 || mc.player.isElytraFlying()) {
                              mc.player.setSprinting((boolean)(634911008 << 1 >>> 1 << 3 ^ 784320768));
                        }

                        if (mc.player.capabilities.isFlying) {
                              mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                              var3 = mc.player;
                              var10001 = mc.player.posX;
                              if ((250784698 >>> 4 >>> 1 >> 1 >> 2 ^ 2101993762) != 0) {
                                    ;
                              }

                              EntityPlayerSP var10002 = mc.player;
                              if ((1250477998 >> 4 << 1 & 67140281 ^ -1207405727) != 0) {
                                    ;
                              }

                              var3.setPosition(var10001, var10002.posY - 5.0000002374872565E-5D, mc.player.posZ);
                              mc.player.capabilities.setFlySpeed((float)this.speed.getValue());
                              mc.player.setSprinting((boolean)(1059858391 << 4 >> 3 ^ 310949936 ^ 543363223 ^ -860625143));
                        }

                        if (((293689656 ^ 212772290) << 1 << 1 >>> 2 ^ 1606164598) != 0) {
                              ;
                        }

                        if (mc.player.onGround) {
                              mc.player.capabilities.allowFlying = (boolean)(((1691164939 ^ 1551461582) & 893110144) << 4 ^ 50616320);
                        }

                        if (mc.player.isElytraFlying()) {
                              mc.player.capabilities.setFlySpeed(0.915F);
                              PlayerCapabilities var4 = mc.player.capabilities;
                              if (((((212466541 ^ 102851448) & 13312938) >> 2 | 703579) ^ 621596 ^ 2342983) == 0) {
                                    ;
                              }

                              var4.isFlying = (boolean)(0 >> 1 ^ 399294117 ^ 399294116);
                              if (!mc.player.capabilities.isCreativeMode) {
                                    mc.player.capabilities.allowFlying = (boolean)(0 & 1713353947 ^ 123002503 ^ 3481670 ^ 123862720);
                              }
                        }
                  } else {
                        if ((1753551228 << 4 >> 2 >> 1 ^ 19125541) != 0) {
                              ;
                        }

                        Minecraft var5 = mc;
                        if (!"you're dogshit".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        if (var5.player.isInWater()) {
                              mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
                              return;
                        }

                        if (mc.gameSettings.keyBindJump.isKeyDown()) {
                              var3 = mc.player;
                              if (((295735512 ^ 140836419) >> 3 ^ 699151138) != 0) {
                                    ;
                              }

                              var3.motionY += 0.08D;
                        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                              if (!"idiot".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              var5 = mc;
                              if ((65600 ^ 65600) == 0) {
                                    ;
                              }

                              var3 = var5.player;
                              var3.motionY -= 0.04D;
                        }

                        float var2;
                        if (mc.gameSettings.keyBindForward.isKeyDown()) {
                              var2 = (float)Math.toRadians((double)mc.player.rotationYaw);
                              var5 = mc;
                              if (!"i hope you catch fire ngl".equals("stop skidding")) {
                                    ;
                              }

                              var3 = var5.player;
                              var3.motionX -= (double)(MathHelper.sin(var2) * 0.05F);
                              if ((((815819441 ^ 692986195) & 136843433) >> 2 & 21139615 ^ 131080) == 0) {
                                    ;
                              }

                              var3 = mc.player;
                              var10001 = var3.motionZ;
                              float var6 = MathHelper.cos(var2) * 0.05F;
                              if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var3.motionZ = var10001 + (double)var6;
                        } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
                              var2 = (float)Math.toRadians((double)mc.player.rotationYaw);
                              var3 = mc.player;
                              var3.motionX += (double)(MathHelper.sin(var2) * 0.05F);
                              var3 = mc.player;
                              if (!"stop skidding".equals("intentMoment")) {
                                    ;
                              }

                              var10001 = var3.motionZ;
                              if (((42064377 >> 4 | 15193) << 3 ^ 496436740) != 0) {
                                    ;
                              }

                              var3.motionZ = var10001 - (double)(MathHelper.cos(var2) * 0.05F);
                        }
                  }

            }
      }
}
