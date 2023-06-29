//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class WaterSpeed extends Module {
      public int ticks;
      public ModeSetting modesWaterSpeed;
      public NumberSetting speedSetting = new NumberSetting("Speed", this, 1.0D, 0.1D, 5.0D, 0.1D);

      public static void setMoveSpeed(double var0) {
            double var10000 = (double)mc.player.movementInput.moveForward;
            if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            double var2 = var10000;
            MovementInput var7 = mc.player.movementInput;
            if (((399630686 >> 3 ^ 13246618) >>> 1 ^ 3016734 ^ 20317766) == 0) {
                  ;
            }

            double var4 = (double)var7.moveStrafe;
            float var6 = mc.player.rotationYaw;
            if (var2 == 0.0D && var4 == 0.0D) {
                  if (((980233622 | 318138041 | 843268192) ^ 989818879) == 0) {
                        ;
                  }

                  if ((76448970 >> 3 << 1 ^ 1216300152) != 0) {
                        ;
                  }

                  mc.player.motionX = 0.0D;
                  if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  mc.player.motionZ = 0.0D;
            } else {
                  if (((91546511 ^ 77656318) >>> 1 >>> 3 << 3 ^ 185172910) != 0) {
                        ;
                  }

                  if (var2 != 0.0D) {
                        if (var4 > 0.0D) {
                              var6 += (float)(var2 > 0.0D ? ((38904718 | 19852838) << 1 & 57690220) << 3 ^ -327303757 : (3 >>> 3 >>> 1 | 1249027535) ^ 1249027554);
                        } else if (var4 < 0.0D) {
                              var6 += (float)(var2 > 0.0D ? 17 >>> 2 ^ 1 ^ 40 : (271338370 | 10072614) << 2 ^ 105462256 ^ 155793295 ^ -1308090572);
                        }

                        var4 = 0.0D;
                        double var10;
                        int var8 = (var10 = var2 - 0.0D) == 0.0D ? 0 : (var10 < 0.0D ? -1 : 1);
                        if ((((640141152 ^ 398381842 | 195521214) << 1 & 1748082048) >>> 2 ^ 403449696) == 0) {
                              ;
                        }

                        if (var8 > 0) {
                              var2 = 1.0D;
                        } else if (var2 < 0.0D) {
                              var2 = -1.0D;
                        }
                  }

                  EntityPlayerSP var9 = mc.player;
                  double var10001 = var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  var9.motionX = var10001 + var4 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F)));
                  mc.player.motionZ = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))) - var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.isInWater()) {
                        String var10000 = this.modesWaterSpeed.activeMode;
                        if (!"please dont crack my plugin".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        if (var10000 == "Simple") {
                              this.ticks += ((0 | 518172284) >> 2 | 95796210) ^ 129874942;
                              if (this.ticks == ((0 >> 1 ^ 833110781) >> 4 ^ 52069419)) {
                                    WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                              }

                              int var5 = this.ticks;
                              int var10001 = ((4 & 1) >> 2 | 1937937760) ^ 1937937765;
                              if (((1516172579 | 548713667) & 744377831 ^ 593573800 ^ 188707403) == 0) {
                                    ;
                              }

                              if (var5 >= var10001) {
                                    WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    this.ticks = ((((405202847 | 401387851) ^ 76953293) & 135256494) << 2 | 480264521) ^ 1017333065;
                              }

                              WaterSpeed.setMoveSpeed(this.speedSetting.getValue());
                        } else {
                              boolean var6 = WaterSpeed.getBlock(mc.player.getPosition()) instanceof BlockLiquid;
                              if (!"please get a girlfriend and stop cracking plugins".equals("idiot")) {
                                    ;
                              }

                              if (var6) {
                                    double var7 = this.speedSetting.getValue();
                                    if ((1553639649 << 4 & 899055564 ^ 25300480) == 0) {
                                          ;
                                    }

                                    float var2 = (float)var7;
                                    EntityPlayerSP var3 = mc.player;
                                    var3.motionX *= (double)var2;
                                    EntityPlayerSP var4 = mc.player;
                                    if ((34603712 >>> 1 ^ 17301856) == 0) {
                                          ;
                                    }

                                    double var8 = var4.motionZ * (double)var2;
                                    if (((1671003342 >>> 2 & 145970561) >> 4 >> 2 ^ -1252249774) != 0) {
                                          ;
                                    }

                                    var4.motionZ = var8;
                              }
                        }
                  }

            }
      }

      public static Block getBlock(BlockPos var0) {
            if ((345128195 >> 2 >> 3 << 3 >>> 2 ^ -2056199659) != 0) {
                  ;
            }

            WorldClient var10000 = mc.world;
            if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            return var10000.getBlockState(var0).getBlock();
      }

      public void onDisable() {
            super.onDisable();
            this.ticks = (1354005115 >> 1 << 4 | 850023006 | 1566148748) ^ -271394;
            if ((1245346536 << 1 >> 4 >> 2 ^ -1550892299) != 0) {
                  ;
            }

      }

      public WaterSpeed() {
            super("WaterSpeed", "faster in water", (22713479 << 2 >>> 4 ^ 4297520) << 3 ^ 12169352, Module.Category.MOVEMENT);
            if (!"stringer is a good obfuscator".equals("stop skidding")) {
                  ;
            }

            ModeSetting var10001 = new ModeSetting;
            if (((1575185733 | 1430019069) << 1 ^ 428403778) != 0) {
                  ;
            }

            int var10006 = (0 | 846606094) << 2 << 3 ^ 1321591234;
            if (((44438292 ^ 30211027) >> 3 ^ -2132204869) != 0) {
                  ;
            }

            String[] var2 = new String[var10006];
            if (!"please go outside".equals("please take a shower")) {
                  ;
            }

            var2[((660676099 | 124023666) & 150331641) >> 2 & 1211997 ^ 1071132] = "Fly";
            var2[(0 ^ 800749217) >> 3 ^ 100093653] = "Simple";
            var10001.<init>("Mode", this, "Simple", var2);
            this.modesWaterSpeed = var10001;
            this.ticks = 408038406 >>> 2 >>> 1 ^ 51004800;
            Setting[] var1 = new Setting[(1 ^ 0 ^ 0) >>> 2 ^ 2];
            var1[575664998 >>> 2 >> 4 ^ 8994765] = this.speedSetting;
            int var10003 = (0 >> 4 | 1064003241 | 814793138 | 42594792) ^ 1073477626;
            ModeSetting var10004 = this.modesWaterSpeed;
            if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var1[var10003] = var10004;
            this.addSettings(var1);
      }
}
