//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.handler;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PlayerTickHandler {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (var1.phase == Phase.START) {
                  if ((301502054 >>> 3 >>> 2 ^ -1211809814) != 0) {
                        ;
                  }

                  EntityPlayer var2 = var1.player;
                  EntityPlayerSP var10001 = Minecraft.getMinecraft().player;
                  if (((376671792 >>> 2 | 92587978) << 3 ^ 564986067 ^ 329157373) != 0) {
                        ;
                  }

                  if (var2 == var10001) {
                        Iterator var3 = ModuleManager.getModuleList().iterator();

                        while(var3.hasNext()) {
                              Object var10000 = var3.next();
                              if (((1723046485 ^ 803061508 ^ 512440394 | 69936333) >>> 2 ^ 368824183) == 0) {
                                    ;
                              }

                              Module var4 = (Module)var10000;
                              if (var4.isToggled()) {
                                    var4.onLocalPlayerUpdate();
                              }

                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                                    ;
                              }
                        }
                  }

            }
      }
}
