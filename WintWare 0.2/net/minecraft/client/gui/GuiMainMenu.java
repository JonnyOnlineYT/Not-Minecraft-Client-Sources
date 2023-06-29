package net.minecraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import me.wintware.client.clickgui.util.ParticleEngine;
import me.wintware.client.ui.altmanager.GuiAltLogin;
import me.wintware.client.utils.other.ChangeLogUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiMainMenu extends GuiScreen {
   public static boolean isLogin = false;
   public static ParticleEngine engine = new ParticleEngine();
   public static float animatedMouseX;
   public static float animatedMouseY;
   public float zoom1 = 1.0F;
   public float zoom2 = 1.0F;
   public float zoom3 = 1.0F;
   public float zoom4 = 1.0F;
   public float zoom5 = 1.0F;

   public void initGui() {
      int i = true;
      int j = height / 4 + 48;
      this.buttonList.add(new GuiButton(1, 20, j + -80, 98, 20, I18n.format("menu.singleplayer")));
      this.buttonList.add(new GuiButton(2, 20, j + -55, 98, 20, I18n.format("menu.multiplayer")));
      this.buttonList.add(new GuiButton(3, 20, j + -30, 98, 20, I18n.format("AltManager")));
      this.buttonList.add(new GuiButton(4, 20, j + -5, 98, 20, I18n.format("menu.options")));
      this.buttonList.add(new GuiButton(5, 20, j + 20, 98, 20, I18n.format("menu.quit")));
   }

   private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
      this.buttonList.add(new GuiButton(1, 20, p_73969_1_, I18n.format("menu.singleplayer")));
      this.buttonList.add(new GuiButton(2, 20, p_73969_1_ + 0, I18n.format("menu.multiplayer")));
      this.buttonList.add(new GuiButton(14, 20, p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 1) {
         this.mc.displayGuiScreen(new GuiWorldSelection(this));
      }

      if (button.id == 2) {
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if (button.id == 4) {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if (button.id == 3) {
         this.mc.displayGuiScreen(new GuiAltLogin(this));
      }

      if (button.id == 5) {
         this.mc.shutdown();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int posX = 2;
      int posY = 40;
      ScaledResolution sr = new ScaledResolution(this.mc);
      RenderUtil.drawGradientSideways(0.0D, 0.0D, (double)sr.getScaledWidth(), (double)sr.getScaledHeight(), (new Color(0, 226, 255)).getRGB(), (new Color(180, 0, 255)).getRGB());
      RenderUtil.drawBorderedRect(-5.0D, 0.0D, (double)(posX + 133), (double)(posY + sr.getScaledHeight()), 0.5D, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      RenderUtil.drawBorderedRect(1.0D, 0.0D, (double)(posX + 132), (double)(posY + sr.getScaledHeight()), 0.5D, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      engine.render((float)mouseX, (float)mouseY);
      int lol = height / 4 + 24;
      ChangeLogUtils.drawChangeLog();
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("WintWare.wtf", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("WintWare.wtf") - 2), (float)(sr.getScaledHeight() - Minecraft.getMinecraft().fontRenderer.getStringHeight("WintWare.wtf") - 2), -1);
      this.mc.net.drawCenteredString("WintWare", 70.0F, 20.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
