package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class WallHack extends Module {
      @SubscribeEvent
      public void onRenderEntityPost(Post var1) {
            GL11.glDisable(11580 >>> 3 << 4 >>> 2 ^ '隫');
            GL11.glPolygonOffset(1.0F, 1100000.0F);
            GL11.glEnable((1604 >> 4 >>> 2 ^ 10) >>> 3 ^ 2898);
      }

      @SubscribeEvent
      public void onRenderEntityPre(Pre var1) {
            if (((142210067 << 3 << 4 | 1013688945) ^ -411789621) != 0) {
                  ;
            }

            GL11.glEnable((31644 | 6900) >>> 4 ^ '螈');
            GL11.glPolygonOffset(1.0F, -1100000.0F);
            GL11.glDisable((138 & 74) << 1 ^ 2884);
      }

      public WallHack() {
            super("WallHack", "wall hack", ((84181156 ^ 67441664) >> 1 | 7898026) ^ 16305146, Module.Category.RENDER);
      }
}
