package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.entity.Entity;

public class HitBox extends Module {
   public static Setting expand;

   public HitBox() {
      super("HitBox", Category.Combat);
      Main.instance.setmgr.rSetting(expand = new Setting("Expand", this, 0.0D, 0.0D, 9.0D, false));
   }

   public static float expand(Entity entity) {
      return !entity.equals(mc.player) && Main.instance.moduleManager.getModuleByClass(HitBox.class).getState() ? (float)expand.getValDouble() : 0.0F;
   }
}
