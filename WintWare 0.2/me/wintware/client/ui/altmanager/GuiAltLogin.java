package me.wintware.client.ui.altmanager;

import java.awt.Color;
import java.io.IOException;
import java.security.SecureRandom;
import me.wintware.client.ui.login.GLSLSandboxShader;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public final class GuiAltLogin extends GuiScreen {
   private PasswordField password;
   private final GuiScreen previousScreen;
   private AltLoginThread thread;
   private GuiTextField username;
   private GLSLSandboxShader backgroundShader;
   private long initTime = System.currentTimeMillis();
   private static String alphabet = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
   private static final SecureRandom secureRandom = new SecureRandom();

   public GuiAltLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   public static String randomString(int strLength) {
      StringBuilder stringBuilder = new StringBuilder(strLength);

      for(int i = 0; i < strLength; ++i) {
         stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
      }

      return stringBuilder.toString();
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
         this.thread.start();
         break;
      case 2:
         this.thread = new AltLoginThread("Wint" + randomString(5), "");
         this.thread.start();
      }

   }

   public void drawScreen(int x2, int y2, float z2) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      RenderUtil.drawGradientSideways(0.0D, 0.0D, (double)sr.getScaledWidth(), (double)sr.getScaledHeight(), (new Color(52, 52, 52)).getRGB(), (new Color(52, 52, 52)).getRGB());
      int lol = height / 4 + 24;
      RenderUtil.drawSmoothRect((float)(width / 2 - 68), (float)(lol + 110), (float)(width / 2 - -60), (float)(lol + -20), (new Color(40, 40, 40, 255)).getRGB());
      this.username.drawTextBox();
      this.password.drawTextBox();
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("WintWare.wtf", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("WintWare.wtf") - 2), (float)(sr.getScaledHeight() - Minecraft.getMinecraft().arraylist.getStringHeight("WintWare.wtf") - 2), -1);
      float var10002 = (float)(width / 2 + -6);
      Minecraft.getMinecraft().arraylist.drawCenteredString("Alt Login", var10002, (float)lol, -1);
      Minecraft.getMinecraft().clickguismall.drawCenteredString(this.thread == null ? TextFormatting.GRAY + "" : this.thread.getStatus(), (float)(width / 2), (float)(lol + 100), -1);
      if (this.username.getText().isEmpty()) {
         var10002 = (float)(width / 2 - 53);
         Minecraft.getMinecraft().clickguismall.drawStringWithShadow("Email", var10002, (float)(lol + 23), -7829368);
      }

      if (this.password.getText().isEmpty()) {
         var10002 = (float)(width / 2 - 53);
         Minecraft.getMinecraft().clickguismall.drawStringWithShadow("Password", var10002, (float)(lol + 42), -7829368);
      }

      super.drawScreen(x2, y2, z2);
   }

   public void initGui() {
      int lol = height / 4 + 24;
      this.buttonList.add(new GuiButton(0, width / 2 - 50, lol + 60, 90, 13, "Login"));
      this.buttonList.add(new GuiButton(2, width / 2 - 50, lol + 63 + 12, 90, 13, "RandomName"));
      this.username = new GuiTextField(lol, this.mc.fontRendererObj, width / 2 - 55, lol + 20, 100, 13);
      this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 55, lol + 40, 100, 13);
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
      }

      if (character == '\t') {
         if (!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(!this.username.isFocused());
         }
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x2, int y2, int button) {
      try {
         super.mouseClicked(x2, y2, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x2, y2, button);
      this.password.mouseClicked(x2, y2, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
   }

   static {
      alphabet = alphabet + alphabet.toLowerCase();
   }
}
