//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Nuker extends Module {
      public NumberSetting Nukerradius;

      public Nuker() {
            super("Nuker", "breaks blocks in radius", 1680444239 >>> 1 << 4 ^ 558652016, Module.Category.PLAYER);
            NumberSetting var10001 = new NumberSetting;
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var10001.<init>("Radius", this, 3.0D, 1.0D, 20.0D, 1.0D);
            this.Nukerradius = var10001;
            Setting[] var1 = new Setting[((0 ^ 1347238809) >> 2 & 220761444) << 1 >> 1 ^ 67112037];
            var1[((1568427606 ^ 756763825) >>> 4 | 36116697) ^ 120027391] = this.Nukerradius;
            this.addSettings(var1);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (((2094287082 >>> 1 ^ 189523569) >> 2 ^ 222851201) == 0) {
                        ;
                  }

                  int var2 = (int)(-this.Nukerradius.getValue());
                  if (((1252293454 >>> 4 >> 3 >>> 1 | 1101497) ^ 5959419) == 0) {
                        ;
                  }

                  while(true) {
                        if ((399317844 >>> 4 & 1929085 ^ 667521 ^ 1470132) == 0) {
                              ;
                        }

                        if (var2 >= (int)this.Nukerradius.getValue()) {
                              return;
                        }

                        if ((1123337927 >> 3 >>> 1 ^ 9703557 ^ 79387881) == 0) {
                              ;
                        }

                        int var10000 = (int)this.Nukerradius.getValue() + ((0 >>> 1 ^ 621432572) & 292390554 ^ 6176630 ^ 22428143);
                        if (!"your mom your dad the one you never had".equals("please go outside")) {
                              ;
                        }

                        for(int var3 = var10000; var3 > (int)(-this.Nukerradius.getValue()) + ((0 & 1404683207) >>> 2 ^ 1); --var3) {
                              int var4 = (int)(-this.Nukerradius.getValue());

                              while(true) {
                                    if ((827535395 >> 4 << 2 ^ 1300223258) != 0) {
                                          ;
                                    }

                                    if (var4 >= (int)this.Nukerradius.getValue()) {
                                          break;
                                    }

                                    EntityPlayerSP var13 = mc.player;
                                    if (((((1611969482 | 932998640) ^ 956999845) & 973848328) >>> 1 ^ 83954564) == 0) {
                                          ;
                                    }

                                    double var5 = var13.posX + (double)var2;
                                    double var7 = mc.player.posY + (double)var3;
                                    double var9 = mc.player.posZ + (double)var4;
                                    if ((1138385467 >>> 1 << 2 >>> 1 << 2 ^ -441473827) != 0) {
                                          ;
                                    }

                                    BlockPos var11 = new BlockPos(var5, var7, var9);
                                    Block var12 = mc.world.getBlockState(var11).getBlock();
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if (var12 == Block.getBlockFromName("Air")) {
                                          if ((2074739750 >> 3 ^ 17016103 ^ 147903271 ^ 100307183 ^ -14857853) != 0) {
                                                ;
                                          }
                                    } else {
                                          var13 = mc.player;
                                          if ((((652606118 | 290687881) << 2 & 284256291 ^ 25457311) << 3 ^ -1968646664) == 0) {
                                                ;
                                          }

                                          var13.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, var11, EnumFacing.NORTH));
                                          mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, var11, EnumFacing.NORTH));
                                    }

                                    ++var4;
                              }
                        }

                        ++var2;
                  }
            }
      }
}
