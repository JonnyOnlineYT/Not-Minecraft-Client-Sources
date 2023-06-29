//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.player;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Nuker extends Module {
      public NumberSetting Nukerradius;

      public Nuker() {
            super("Nuker", "breaks blocks in radius", 2514969 << 3 ^ 20119752, Module.Category.PLAYER);
            NumberSetting var10001 = new NumberSetting;
            if ((((767085000 | 501092413) << 3 | 1227858908) << 4 ^ -65600) == 0) {
                  ;
            }

            if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                  ;
            }

            var10001.<init>("Radius", this, 3.0D, 1.0D, 20.0D, 1.0D);
            this.Nukerradius = var10001;
            if (((1230951545 >>> 1 ^ 64756130 | 432719579) ^ 1073477599) == 0) {
                  ;
            }

            Setting[] var1 = new Setting[(0 ^ 702614775) >>> 2 & 125410874 ^ 41419321];
            var1[((68030598 ^ 29053423) >>> 1 << 3 | 216387563) ^ 519426027] = this.Nukerradius;
            this.addSettings(var1);
            if (((775006214 | 590470613) << 2 ^ -17758894) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if (!"you probably spell youre as your".equals("you're dogshit")) {
                  ;
            }

            if (var10000.player != null) {
                  for(int var2 = (int)(-this.Nukerradius.getValue()); var2 < (int)this.Nukerradius.getValue(); ++var2) {
                        for(int var3 = (int)this.Nukerradius.getValue() + ((0 | 1299427088) >> 3 ^ 162428387); var3 > (int)(-this.Nukerradius.getValue()) + ((0 >> 3 | 1349840249) >>> 1 ^ 674920125); --var3) {
                              if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              double var13 = this.Nukerradius.getValue();
                              if ((1270273832 >> 3 ^ 109816730 ^ 236173734 ^ -318317153) != 0) {
                                    ;
                              }

                              for(int var4 = (int)(-var13); var4 < (int)this.Nukerradius.getValue(); ++var4) {
                                    double var5 = mc.player.posX + (double)var2;
                                    double var7 = mc.player.posY + (double)var3;
                                    var13 = mc.player.posZ;
                                    if (!"yo mama name maurice".equals("minecraft")) {
                                          ;
                                    }

                                    double var9 = var13 + (double)var4;
                                    BlockPos var11 = new BlockPos(var5, var7, var9);
                                    Block var14 = mc.world.getBlockState(var11).getBlock();
                                    if ((1506712036 >>> 3 << 3 << 3 ^ 1292455397) != 0) {
                                          ;
                                    }

                                    Block var12 = var14;
                                    if (var12 != Block.getBlockFromName("Air")) {
                                          mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, var11, EnumFacing.NORTH));
                                          if ((6480780 << 4 ^ 103692480) == 0) {
                                                ;
                                          }

                                          mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, var11, EnumFacing.NORTH));
                                    }
                              }
                        }
                  }

            }
      }
}
