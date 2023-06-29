package ru.rockstar.client.ui.altmanager;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.ui.altmanager.alt.Alt;
import ru.rockstar.client.ui.altmanager.alt.AltLoginThread;

import org.lwjgl.input.Keyboard;

public final class GuiAltLogin extends GuiScreen {
   private final GuiScreen previousScreen;
   private PasswordField password;
   private AltLoginThread thread;
   private GuiTextField username;

   public GuiAltLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   protected void actionPerformed(GuiButton button) {
      try {
         switch(button.id) {
         case 0:
            (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
            break;
         case 1:
            this.mc.displayGuiScreen(this.previousScreen);
            break;
         case 2:
            String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            if (data.contains(":")) {
               String[] credentials = data.split(":");
               this.username.setText(credentials[0]);
               this.password.setText(credentials[1]);
            }
         }

      } catch (Throwable var4) {
         throw new RuntimeException();
      }
   }

   public void drawScreen(int x, int y, float z) {
      DrawHelper.drawBorderedRect(0.0D, 0.0D, (double)this.width, (double)this.height, 0.5D, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      this.username.drawTextBox();
      this.password.drawTextBox();
      this.mc.neverlose500_18.drawStringWithShadow("Alt Login", (double)((float)this.width / 2.0F), 20.0D, -1);
      this.mc.neverlose500_18.drawStringWithShadow(this.thread == null ? TextFormatting.GRAY + "Alts..." : this.thread.getStatus(), (double)((float)this.width / 2.0F), 29.0D, -1);
      if (this.username.getText().isEmpty() && !this.username.isFocused()) {
         this.mc.neverlose500_18.drawStringWithShadow("Username / E-Mail", (double)(this.width / 2 - 96), 66.0D, -7829368);
      }

      if (this.password.getText().isEmpty() && !this.password.isFocused()) {
         this.mc.neverlose500_18.drawStringWithShadow("Password", (double)(this.width / 2 - 96), 106.0D, -7829368);
      }

      super.drawScreen(x, y, z);
   }

   public void initGui() {
      int height1 = this.height / 4 + 24;
      this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, height1 + 72 + 12, "Login"));
      this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
      this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
      this.username = new GuiTextField(height1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      if (character == '\t') {
         if (!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(this.username.isFocused());
         }
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x, y, button);
      this.password.mouseClicked(x, y, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
   }
}
