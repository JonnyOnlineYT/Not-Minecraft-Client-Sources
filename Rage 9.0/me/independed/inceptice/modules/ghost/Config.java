package me.independed.inceptice.modules.ghost;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Config extends Module {
      @SubscribeEvent
      public void onClientTick(ClientTickEvent var1) {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

      }

      public Config() {
            super("Config", "save your modules settings and keyBinds", 1265654467 << 3 & 1240425445 & 516115611 ^ 142753792, Module.Category.GHOST);
            this.toggle();
      }
}
