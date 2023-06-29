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

      public BowSpam() {
            super("BowSpam", "spamming arrows", (20645074 ^ 10959331) >>> 3 ^ 3377062, Module.Category.COMBAT);
            NumberSetting var10001 = new NumberSetting;
            if (((218047156 << 3 << 3 >> 4 | 39640987) ^ 359194793) != 0) {
                  ;
            }

            var10001.<init>("UseCount", this, 2.4D, 1.5D, 5.0D, 0.1D);
            this.speedSpam = var10001;
            Setting[] var1 = new Setting[(0 & 932402422) << 1 << 3 & 503594394 ^ 1];
            if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                  ;
            }

            if (((418653085 >> 3 >> 2 ^ 11281949) >>> 4 ^ 1537479601) != 0) {
                  ;
            }

            var1[(812742414 ^ 576953649 ^ 278946349) >>> 1 ^ 22627337] = this.speedSpam;
            this.addSettings(var1);
            if ((((1419782952 | 90176200) ^ 704594189) << 3 ^ -536488152) == 0) {
                  ;
            }

            if ((1616000630 << 1 ^ 1317212980 ^ 1555414560) != 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
                        if (((519909028 >> 1 >>> 2 ^ 43035674 | 8488574) ^ 767121300) != 0) {
                              ;
                        }

                        Minecraft var10000 = mc;
                        if (((709604238 ^ 14525830) >> 1 ^ 357239556) == 0) {
                              ;
                        }

                        EntityPlayerSP var2 = var10000.player;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        if (var2.isHandActive() && (double)mc.player.getItemInUseMaxCount() >= this.speedSpam.getValue()) {
                              NetHandlerPlayClient var3 = mc.player.connection;
                              CPacketPlayerDigging var10001 = new CPacketPlayerDigging;
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              Action var10003 = Action.RELEASE_USE_ITEM;
                              BlockPos var10004 = BlockPos.ORIGIN;
                              EntityPlayerSP var10005 = mc.player;
                              if ((385035987 >> 3 << 1 << 4 ^ 1540143936) == 0) {
                                    ;
                              }

                              var10001.<init>(var10003, var10004, var10005.getHorizontalFacing());
                              var3.sendPacket(var10001);
                              mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                              mc.player.stopActiveHand();
                        }
                  }

                  if (!"idiot".equals("shitted on you harder than archybot")) {
                        ;
                  }

            }
      }
}
