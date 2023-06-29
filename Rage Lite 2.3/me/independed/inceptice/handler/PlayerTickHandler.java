//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.handler;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PlayerTickHandler {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (var1.phase == Phase.START) {
                  EntityPlayer var2 = var1.player;
                  if (var2 == Minecraft.getMinecraft().player) {
                        if ((541106184 >> 4 >> 3 & 602730 ^ 64) == 0) {
                              ;
                        }

                        Iterator var3 = ModuleManager.getModuleList().iterator();

                        while(var3.hasNext()) {
                              Module var4 = (Module)var3.next();
                              if (var4.isToggled()) {
                                    var4.onLocalPlayerUpdate();
                              }
                        }
                  }

            }
      }
}
