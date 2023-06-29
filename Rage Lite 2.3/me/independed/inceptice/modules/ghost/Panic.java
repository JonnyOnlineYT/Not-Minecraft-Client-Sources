//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.ghost;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Panic extends Module {
      public static Hud hud;
      public static ModuleManager moduleManager;
      public static Setting setting;

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            Minecraft var10000 = mc;
            if (((1912406502 >> 1 & 307735596) << 1 >> 3 ^ -1904201385) != 0) {
                  ;
            }

            if (var10000.player != null) {
                  Iterator var2 = ModuleManager.getModuleList().iterator();
                  if (!"ape covered in human flesh".equals("idiot")) {
                        ;
                  }

                  Module var3;
                  for(; var2.hasNext(); var3.setKey((557817303 ^ 201057531 | 499996366) ^ 782539515 ^ 292152597)) {
                        var3 = (Module)var2.next();
                        if ((454886193 << 4 << 4 << 4 ^ 1777172959) != 0) {
                              ;
                        }

                        if (var3.isToggled()) {
                              var3.toggle();
                        }
                  }

                  mc.player.jump();
                  mc.displayGuiScreen((GuiScreen)null);
            }
      }

      public Panic() {
            super("Panic", "disable all active modules.(you will can't enable them after using this before restart)", 66588754 ^ 48358214 ^ 15422648 ^ 32679852, Module.Category.GHOST);
      }
}
