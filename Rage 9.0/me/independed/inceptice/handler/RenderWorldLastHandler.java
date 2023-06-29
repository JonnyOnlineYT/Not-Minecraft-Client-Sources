package me.independed.inceptice.handler;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderWorldLastHandler {
      public RenderWorldLastHandler() {
            if ((241394064 >> 4 << 4 ^ -1067838043) != 0) {
                  ;
            }

            super();
      }

      @SubscribeEvent
      public void onRenderWorldLast(RenderWorldLastEvent var1) {
            Iterator var2 = ModuleManager.getModuleList().iterator();

            while(var2.hasNext()) {
                  Module var10000 = (Module)var2.next();
                  if (!"please take a shower".equals("nefariousMoment")) {
                        ;
                  }

                  Module var3 = var10000;
                  if (var3.isToggled()) {
                        if (((33698005 | 14841807 | 25937192 | 19621883 | 298545) ^ 66060287) == 0) {
                              ;
                        }

                        if (((624536959 << 3 ^ 336991875) >> 4 ^ -129414908) != 0) {
                              ;
                        }

                        var3.onRenderWorldLast(var1.getPartialTicks());
                  }
            }

      }
}
