//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.misc;

import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.ChatUtil;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class MCF extends Module {
      TimerUtil timerUtil;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer && mc.gameSettings.keyBindPickBlock.isKeyDown()) {
                        if (((2082423194 << 1 ^ 1807174133) << 2 ^ -1878722143) != 0) {
                              ;
                        }

                        if (this.timerUtil.hasReached(100.0D)) {
                              String var2 = Minecraft.getMinecraft().objectMouseOver.entityHit.getName();
                              if (!FriendManager.isFriend(var2)) {
                                    FriendManager.addFriend(var2, var2);
                                    StringBuilder var3 = (new StringBuilder()).append("Added ");
                                    if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var3 = var3.append(var2);
                                    if (((2144751 | 1657982) >>> 3 ^ 38216194) != 0) {
                                          ;
                                    }

                                    ChatUtil.sendChatMessage(var3.append(" as a friend.").toString());
                                    this.timerUtil.reset();
                                    if (((450915708 << 2 & 950553729) >>> 2 >>> 3 >> 1 ^ 10616850) == 0) {
                                          ;
                                    }

                                    return;
                              }

                              if ((1025937396 >>> 2 >> 3 ^ 32060543) == 0) {
                                    ;
                              }

                              TimerUtil var10000 = this.timerUtil;
                              if (('肁' ^ '肁') == 0) {
                                    ;
                              }

                              if (var10000.hasReached(100.0D)) {
                                    FriendManager.removeFriend(var2);
                                    ChatUtil.sendChatMessage((new StringBuilder()).append("Removed ").append(var2).append(".").toString());
                                    this.timerUtil.reset();
                              }
                        }
                  }

            }
      }

      public MCF() {
            int var10003 = (327512050 >>> 2 ^ 53504078) >>> 1 ^ 65575385;
            Module.Category var10004 = Module.Category.WORLD;
            if (((1125000208 >>> 1 & 150834358) >>> 4 ^ 544768) == 0) {
                  ;
            }

            super("MCF", "middle click friends", var10003, var10004);
            this.timerUtil = new TimerUtil();
      }
}
