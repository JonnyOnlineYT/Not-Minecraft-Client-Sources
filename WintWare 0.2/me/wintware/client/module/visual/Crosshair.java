package me.wintware.client.module.visual;

import java.awt.Color;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.util.MovementInput;

public class Crosshair extends Module {
   private final Setting dynamic;
   private final Setting colorRed;
   private final Setting colorGreen;
   private final Setting colorBlue;
   private final Setting width;
   private final Setting gap;
   private final Setting length;
   private final Setting dynamicGap;

   public Crosshair() {
      super("Crosshair", Category.Visuals);
      Main.instance.setmgr.rSetting(this.colorRed = new Setting("Red", this, 249.0D, 0.0D, 255.0D, true));
      Main.instance.setmgr.rSetting(this.colorGreen = new Setting("Green", this, 255.0D, 0.0D, 255.0D, true));
      Main.instance.setmgr.rSetting(this.colorBlue = new Setting("Blue", this, 0.0D, 0.0D, 255.0D, true));
      Main.instance.setmgr.rSetting(this.width = new Setting("Width", this, 1.0D, 0.5D, 8.0D, true));
      Main.instance.setmgr.rSetting(this.gap = new Setting("Gap", this, 2.0D, 0.5D, 10.0D, true));
      Main.instance.setmgr.rSetting(this.length = new Setting("Length", this, 3.0D, 0.5D, 30.0D, true));
      Main.instance.setmgr.rSetting(this.dynamic = new Setting("Dynamic", this, false));
      Main.instance.setmgr.rSetting(this.dynamicGap = new Setting("Dynamic Gap", this, 3.0D, 1.0D, 20.0D, true));
   }

   @EventTarget
   public void on2D(Event2D event) {
      int color = (new Color(this.colorRed.getValFloat() / 255.0F, this.colorGreen.getValFloat() / 255.0F, this.colorBlue.getValFloat() / 255.0F)).getRGB();
      int screenWidth = (int)event.getWidth();
      int screenHeight = (int)event.getHeight();
      int wMiddle = screenWidth / 2;
      int hMiddle = screenHeight / 2;
      boolean dyn = this.dynamic.getValue();
      double dyngap = this.dynamicGap.getValDouble();
      double wid = this.width.getValDouble();
      double len = this.length.getValDouble();
      boolean wider = dyn && this.isMoving();
      double gaps = wider ? dyngap : this.gap.getValDouble();
      RenderUtil.drawBorderedRect((double)wMiddle - gaps - len, (double)hMiddle - wid / 2.0D, (double)wMiddle - gaps, (double)hMiddle + wid / 2.0D, 0.5D, Color.black.getRGB(), color, false);
      RenderUtil.drawBorderedRect((double)wMiddle + gaps, (double)hMiddle - wid / 2.0D, (double)wMiddle + gaps + len, (double)hMiddle + wid / 2.0D, 0.5D, Color.black.getRGB(), color, false);
      RenderUtil.drawBorderedRect((double)wMiddle - wid / 2.0D, (double)hMiddle - gaps - len, (double)wMiddle + wid / 2.0D, (double)hMiddle - gaps, 0.5D, Color.black.getRGB(), color, false);
      RenderUtil.drawBorderedRect((double)wMiddle - wid / 2.0D, (double)hMiddle + gaps, (double)wMiddle + wid / 2.0D, (double)hMiddle + gaps + len, 0.5D, Color.black.getRGB(), color, false);
   }

   public boolean isMoving() {
      return MovementInput.moveForward != 0.0F || mc.player.moveStrafing != 0.0F;
   }
}
