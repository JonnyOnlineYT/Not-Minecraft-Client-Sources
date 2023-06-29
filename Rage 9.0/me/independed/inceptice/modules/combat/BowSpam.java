//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class BowSpam extends Module {
      public NumberSetting speedSpam;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive()) {
                        EntityPlayerSP var10000 = mc.player;
                        if ((633888746 << 2 >> 3 ^ -219926539) == 0) {
                              ;
                        }

                        double var2 = (double)var10000.getItemInUseMaxCount();
                        if (((734642944 >> 4 >> 4 & 2509374 | 1063228) >> 1 ^ 1768744516) != 0) {
                              ;
                        }

                        if (var2 >= this.speedSpam.getValue()) {
                              NetHandlerPlayClient var3 = mc.player.connection;
                              CPacketPlayerDigging var10001 = new CPacketPlayerDigging;
                              if ((358169083 << 2 << 3 ^ 975655509 ^ -1862243019) == 0) {
                                    ;
                              }

                              var10001.<init>(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing());
                              var3.sendPacket(var10001);
                              Minecraft var4 = mc;
                              if ((1103716517 >> 1 >>> 1 ^ 1338272943) != 0) {
                                    ;
                              }

                              var3 = var4.player.connection;
                              CPacketPlayerTryUseItem var5 = new CPacketPlayerTryUseItem;
                              if ((336159148 << 1 >>> 4 >>> 1 ^ 21009946) == 0) {
                                    ;
                              }

                              var5.<init>(mc.player.getActiveHand());
                              var3.sendPacket(var5);
                              mc.player.stopActiveHand();
                        }
                  }

                  if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

            }
      }

      public BowSpam() {
            super("BowSpam", "spamming arrows", 1187972 >> 2 ^ 296993, Module.Category.COMBAT);
            if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                  ;
            }

            NumberSetting var10001 = new NumberSetting;
            if (((59103315 << 2 & 80020931) >>> 4 ^ -697285341) != 0) {
                  ;
            }

            var10001.<init>("UseCount", this, 2.4D, 1.5D, 5.0D, 0.1D);
            this.speedSpam = var10001;
            if (((640853019 >>> 3 | 18598564) << 2 ^ 394230428) == 0) {
                  ;
            }

            Setting[] var1 = new Setting[(0 & 1679779631 | 960730008) >> 1 ^ 480365005];
            if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var1[882765170 << 2 >>> 2 >>> 2 ^ 220691292] = this.speedSpam;
            this.addSettings(var1);
      }
}
