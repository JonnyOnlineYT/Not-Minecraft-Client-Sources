//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.ghost;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Panic extends Module {
      public static Setting setting;
      public static ModuleManager moduleManager;
      public static Hud hud;

      public Panic() {
            super("Panic", "disable all active modules.(you will can't enable them after using this before restart)", ((327780269 >>> 4 ^ 15136589) & 21166849 | 16893034) ^ 21229419, Module.Category.GHOST);
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player != null) {
                  if ((773486769 >>> 4 >> 3 >> 1 ^ 3021432) == 0) {
                        ;
                  }

                  Module var3;
                  for(Iterator var2 = ModuleManager.getModuleList().iterator(); var2.hasNext(); var3.setKey(94 >> 4 ^ 5)) {
                        if ((3146560 << 1 ^ 6293120) == 0) {
                              ;
                        }

                        var3 = (Module)var2.next();
                        if (var3.isToggled()) {
                              var3.toggle();
                        }
                  }

                  mc.player.jump();
                  mc.displayGuiScreen((GuiScreen)null);
            }
      }
}
