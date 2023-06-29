package ru.rockstar.client.features.impl.visuals;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.opengl.GL11;

public class Nimb extends Feature {
   final ListSetting colorMode = new ListSetting("Trails Color", "Astolfo", () -> {
      return true;
   }, new String[]{"Astolfo", "Pulse", "Custom", "Client", "Static"});
   final ColorSetting onecolor = new ColorSetting("One Color", (new Color(255, 255, 255)).getRGB(), () -> {
      return this.colorMode.currentMode.equalsIgnoreCase("Static") || this.colorMode.currentMode.equalsIgnoreCase("Custom");
   });
   final ColorSetting twocolor = new ColorSetting("Two Color", (new Color(255, 255, 255)).getRGB(), () -> {
      return this.colorMode.currentMode.equalsIgnoreCase("Custom");
   });
   final NumberSetting saturation = new NumberSetting("Saturation", 0.7F, 0.1F, 1.0F, 0.1F, () -> {
      return this.colorMode.currentMode.equalsIgnoreCase("Astolfo");
   });

   public Nimb() {
      super("Nimb", "Создаёт нимб, ты бог бро)", 0, Category.VISUALS);
      this.addSettings(new Setting[]{this.colorMode, this.onecolor, this.twocolor, this.saturation});
   }

   @EventTarget
   public void asf(Event3D event) {
      ItemStack stack = mc.player.getEquipmentInSlot(4);
      double height = stack.getItem() instanceof ItemArmor ? (mc.player.isSneaking() ? -0.1D : 0.12D) : (mc.player.isSneaking() ? -0.22D : 0.0D);
      if (mc.gameSettings.thirdPersonView == 1 || mc.gameSettings.thirdPersonView == 2) {
         GlStateManager.pushMatrix();
         GL11.glBlendFunc(770, 771);
         GlStateManager.disableDepth();
         GlStateManager.disableTexture2D();
         DrawHelper.enableSmoothLine(2.5F);
         GL11.glShadeModel(7425);
         GL11.glDisable(2884);
         GL11.glEnable(3042);
         GL11.glEnable(2929);
         GL11.glTranslatef(0.0F, (float)((double)mc.player.height + height), 0.0F);
         GL11.glRotatef(-mc.player.rotationYaw, 0.0F, 1.0F, 0.0F);
         Color color2 = Color.WHITE;
         Color firstcolor2 = new Color(this.onecolor.getColorValue());
         String var7 = this.colorMode.currentMode;
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -1808614770:
            if (var7.equals("Static")) {
               var8 = 4;
            }
            break;
         case 77474681:
            if (var7.equals("Pulse")) {
               var8 = 2;
            }
            break;
         case 961091784:
            if (var7.equals("Astolfo")) {
               var8 = 1;
            }
            break;
         case 2021122027:
            if (var7.equals("Client")) {
               var8 = 0;
            }
            break;
         case 2029746065:
            if (var7.equals("Custom")) {
               var8 = 3;
            }
         }

         switch(var8) {
         case 0:
            color2 = ClientHelper.getClientColor(5.0F, 1.0F, 5);
            break;
         case 1:
            color2 = DrawHelper.astolfoColors45(5.0F, 5.0F, this.saturation.getNumberValue(), 10.0F);
            break;
         case 2:
            color2 = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D);
            break;
         case 3:
            color2 = DrawHelper.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D);
            break;
         case 4:
            color2 = firstcolor2;
         }

         GL11.glBegin(6);
         DrawHelper.glColor(color2, 255);
         GL11.glVertex3d(0.0D, 0.3D, 0.0D);

         Color firstcolor;
         String var10;
         byte var11;
         float i;
         Color color;
         for(i = 0.0F; (double)i < 360.5D; ++i) {
            color = Color.WHITE;
            firstcolor = new Color(this.onecolor.getColorValue());
            var10 = this.colorMode.currentMode;
            var11 = -1;
            switch(var10.hashCode()) {
            case -1808614770:
               if (var10.equals("Static")) {
                  var11 = 4;
               }
               break;
            case 77474681:
               if (var10.equals("Pulse")) {
                  var11 = 2;
               }
               break;
            case 961091784:
               if (var10.equals("Astolfo")) {
                  var11 = 1;
               }
               break;
            case 2021122027:
               if (var10.equals("Client")) {
                  var11 = 0;
               }
               break;
            case 2029746065:
               if (var10.equals("Custom")) {
                  var11 = 3;
               }
            }

            switch(var11) {
            case 0:
               color = ClientHelper.getClientColor(i / 16.0F, i, 5);
               break;
            case 1:
               color = DrawHelper.astolfoColors45(i - i + 1.0F, i, this.saturation.getNumberValue(), 10.0F);
               break;
            case 2:
               color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (double)(6.0F * (i / 16.0F) / 60.0F));
               break;
            case 3:
               color = DrawHelper.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (double)(3.0F * (i / 16.0F) / 60.0F));
               break;
            case 4:
               color = firstcolor;
            }

            DrawHelper.glColor(color, 255);
         }

         GL11.glVertex3d(0.0D, 0.3D, 0.0D);
         GL11.glEnd();
         GL11.glBegin(2);

         for(i = 0.0F; (double)i < 360.5D; ++i) {
            color = Color.WHITE;
            firstcolor = new Color(this.onecolor.getColorValue());
            var10 = this.colorMode.currentMode;
            var11 = -1;
            switch(var10.hashCode()) {
            case -1808614770:
               if (var10.equals("Static")) {
                  var11 = 4;
               }
               break;
            case 77474681:
               if (var10.equals("Pulse")) {
                  var11 = 2;
               }
               break;
            case 961091784:
               if (var10.equals("Astolfo")) {
                  var11 = 1;
               }
               break;
            case 2021122027:
               if (var10.equals("Client")) {
                  var11 = 0;
               }
               break;
            case 2029746065:
               if (var10.equals("Custom")) {
                  var11 = 3;
               }
            }

            switch(var11) {
            case 0:
               color = ClientHelper.getClientColor(5.0F, i, 5);
               break;
            case 1:
               color = DrawHelper.astolfoColors45(i - i + 1.0F, i, this.saturation.getNumberValue(), 10.0F);
               break;
            case 2:
               color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (double)(6.0F * (i / 16.0F) / 60.0F));
               break;
            case 3:
               color = DrawHelper.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + (double)(3.0F * (i / 16.0F) / 60.0F));
               break;
            case 4:
               color = firstcolor;
            }

            DrawHelper.glColor(color, 255);
            for(int a = 0; (double)a < 3; ++a) {
            	GL11.glVertex3d(Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.45D, 0.15D - 0.02 * a, Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.45D);
            	GL11.glVertex3d(Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.45D, 0.15D - 0.02 * a, Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.47D);
            }
         }

         GL11.glEnd();
         GlStateManager.enableAlpha();
         DrawHelper.disableSmoothLine();
         GL11.glShadeModel(7424);
         GL11.glEnable(2884);
         GL11.glDisable(3042);
         GlStateManager.enableTexture2D();
         GlStateManager.enableDepth();
         GlStateManager.resetColor();
         GlStateManager.popMatrix();
      }

   }
}
