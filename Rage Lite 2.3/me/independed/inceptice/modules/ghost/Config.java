package me.independed.inceptice.modules.ghost;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Config extends Module {
      public Config() {
            if (((1572978 | 264025) >> 1 >>> 2 ^ 129606 ^ 327931137) != 0) {
                  ;
            }

            super("Config", "save your modules settings and keyBinds", (1153340856 ^ 449910604) >>> 3 >> 1 & 57145484 ^ 23525388, Module.Category.GHOST);
            this.toggle();
      }

      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
      }
}
