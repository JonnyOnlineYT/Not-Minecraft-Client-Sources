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
            if (((2144149611 | 334630221 | 2094894673) >> 4 ^ -1972996755) != 0) {
                  ;
            }

            if (mc.player == null) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                        ;
                  }

            } else {
                  if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer && mc.gameSettings.keyBindPickBlock.isKeyDown()) {
                        TimerUtil var10000 = this.timerUtil;
                        if (((96872018 | 93366215 | 24166711) ^ -588363192) != 0) {
                              ;
                        }

                        if (var10000.hasReached(100.0D)) {
                              String var2 = Minecraft.getMinecraft().objectMouseOver.entityHit.getName();
                              if (!FriendManager.isFriend(var2)) {
                                    if (((530091805 << 2 | 948128526) << 3 ^ 1252031558) != 0) {
                                          ;
                                    }

                                    FriendManager.addFriend(var2, var2);
                                    StringBuilder var3 = (new StringBuilder()).append("Added ").append(var2).append(" as a friend.");
                                    if (((68462738 | 61115963) >> 4 >>> 4 ^ 1380993366) != 0) {
                                          ;
                                    }

                                    ChatUtil.sendChatMessage(var3.toString());
                                    this.timerUtil.reset();
                                    return;
                              }

                              if (this.timerUtil.hasReached(100.0D)) {
                                    FriendManager.removeFriend(var2);
                                    ChatUtil.sendChatMessage((new StringBuilder()).append("Removed ").append(var2).append(".").toString());
                                    this.timerUtil.reset();
                              }
                        }
                  }

            }
      }

      public MCF() {
            if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                  ;
            }

            int var10003 = (1302224179 << 2 ^ 816042087 | 22463271) >>> 4 ^ 8253050;
            if ((((803375982 ^ 200547145) & 524164978) << 1 >> 1 & 19945493 ^ 1052672) == 0) {
                  ;
            }

            super("MCF", "middle click friends", var10003, Module.Category.WORLD);
            TimerUtil var10001 = new TimerUtil;
            if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                  ;
            }

            var10001.<init>();
            this.timerUtil = var10001;
      }
}
