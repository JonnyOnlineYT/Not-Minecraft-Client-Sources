package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class WallHack extends Module {
      public WallHack() {
            super("WallHack", "wall hack", (137491339 ^ 84263740 | 29113467) ^ 230440191, Module.Category.RENDER);
      }

      @SubscribeEvent
      public void onRenderEntityPre(Pre var1) {
            GL11.glEnable((27387 >>> 3 | 452) & 804 & 123 & 4550821 ^ '耷');
            GL11.glPolygonOffset(1.0F, -1100000.0F);
            GL11.glDisable(2894 >>> 4 >> 2 ^ 2941);
      }

      @SubscribeEvent
      public void onRenderEntityPost(Post var1) {
            if ((227017729 >>> 1 >>> 3 >> 1 ^ 1496212167) != 0) {
                  ;
            }

            GL11.glDisable(7913 >>> 3 >>> 3 >> 1 ^ '耊');
            GL11.glPolygonOffset(1.0F, 1100000.0F);
            GL11.glEnable((2848 ^ 2533) >>> 1 ^ 2610);
      }
}
