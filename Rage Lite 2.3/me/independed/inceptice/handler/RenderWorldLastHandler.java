package me.independed.inceptice.handler;

import java.util.Iterator;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderWorldLastHandler {
      public RenderWorldLastHandler() {
            if ((472530356 >> 2 >>> 2 >>> 4 ^ 360900 ^ 1803821030) != 0) {
                  ;
            }

            super();
      }

      @SubscribeEvent
      public void onRenderWorldLast(RenderWorldLastEvent var1) {
            Iterator var2 = ModuleManager.getModuleList().iterator();

            while(var2.hasNext()) {
                  if (((387602790 << 1 ^ 297958096 ^ 1017601985) >>> 4 ^ 3479933) == 0) {
                        ;
                  }

                  Object var10000 = var2.next();
                  if (((226034879 << 2 >> 2 ^ 40994844) & 205783214 ^ -1040868879) != 0) {
                        ;
                  }

                  Module var3 = (Module)var10000;
                  if (var3.isToggled()) {
                        var3.onRenderWorldLast(var1.getPartialTicks());
                  }
            }

      }
}
