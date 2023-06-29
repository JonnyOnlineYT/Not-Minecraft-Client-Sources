package me.wintware.client.module.visual;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventTransformSideFirstPerson;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
   public static Setting x;
   public static Setting y;
   public static Setting z;

   public ViewModel() {
      super("ViewModel", Category.Visuals);
      Main.instance.setmgr.rSetting(x = new Setting("X", this, 0.0D, -2.0D, 2.0D, false));
      Main.instance.setmgr.rSetting(y = new Setting("Y", this, 0.2D, -2.0D, 2.0D, false));
      Main.instance.setmgr.rSetting(z = new Setting("Z", this, 0.2D, -2.0D, 2.0D, false));
   }

   @EventTarget
   public void onSidePerson(EventTransformSideFirstPerson event) {
      if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
         GlStateManager.translate(x.getValDouble(), y.getValDouble(), z.getValDouble());
      }

   }
}
