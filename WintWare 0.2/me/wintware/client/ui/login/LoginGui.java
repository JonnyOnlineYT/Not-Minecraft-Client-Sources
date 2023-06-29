package me.wintware.client.ui.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import me.wintware.client.Main;
import me.wintware.client.utils.other.HwidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class LoginGui extends GuiScreen {
   private GLSLSandboxShader backgroundShader;
   private long initTime = System.currentTimeMillis();
   public static GuiTextField hwid;
   String status = "";

   public LoginGui() {
      try {
         this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
      } catch (IOException var2) {
         throw new IllegalStateException("Failed to load backgound shader", var2);
      }
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      hwid = new GuiTextField(123123, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 60, 200, 20);
      hwid.setMaxStringLength(16);
      this.buttonList.add(new GuiButton(1001, width / 2 - 100, height / 2 + 10, "Authentication"));
   }

   public void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 1001:
         try {
            String[] strings = requestURLSRC("http://qwertymk0.majorservice.space/txt.txt").split("!");
            String[] var3 = strings;
            int var4 = strings.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String s = var3[var5];
               if (s.equalsIgnoreCase(HwidUtil.getHWID() + ":" + hwid.getText())) {
                  this.mc.displayGuiScreen(new GuiMainMenu());
               }
            }
         } catch (Exception var7) {
            this.mc.shutdownMinecraftApplet();
         }
      default:
      }
   }

   public static String requestURLSRC(String BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27) throws IOException {
      URL urlObject = new URL(BLviCHHy76v5Ch39PB3hpcX7W2qe45YaBPQyn285Dcg27);
      URLConnection urlConnection = urlObject.openConnection();
      urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      return AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(urlConnection.getInputStream());
   }

   private static String AP2iKAwcS2gFL8cX8z944ZiJp2zS54T68Tp39nr2rJAwh(InputStream L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG) throws IOException {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(L58C336iNBkwz86u4QV3HcDJ94i34gWv4gpzbqBC5ZCdG, "UTF-8"));
      Throwable var2 = null;

      try {
         StringBuilder stringBuilder = new StringBuilder();

         String inputLine;
         while((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
         }

         String var5 = stringBuilder.toString();
         return var5;
      } catch (Throwable var14) {
         var2 = var14;
         throw var14;
      } finally {
         if (bufferedReader != null) {
            if (var2 != null) {
               try {
                  bufferedReader.close();
               } catch (Throwable var13) {
                  var2.addSuppressed(var13);
               }
            } else {
               bufferedReader.close();
            }
         }

      }
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void drawScreen(int x2, int y2, float z2) {
      GlStateManager.disableCull();
      this.backgroundShader.useShader(width, height, (float)x2, (float)y2, (float)(System.currentTimeMillis() - this.initTime) / 1000.0F);
      GL11.glBegin(7);
      GL11.glVertex2f(-1.0F, -1.0F);
      GL11.glVertex2f(-1.0F, 1.0F);
      GL11.glVertex2f(1.0F, 1.0F);
      GL11.glVertex2f(1.0F, -1.0F);
      GL11.glEnd();
      GL20.glUseProgram(0);
      new ScaledResolution(this.mc);
      hwid.drawTextBox();
      if (hwid.getText().isEmpty()) {
         float var10002 = (float)(width / 2 - 96);
         Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("UID", var10002, (float)(height / 2 - 54), -1);
      }

      Minecraft.getMinecraft().fontRenderer.drawCenteredString(Main.name + " Auth", (float)(width / 2), (float)(height / 2 - 90), -1);
      super.drawScreen(x2, y2, z2);
   }

   public void mouseClicked(int x2, int y2, int z2) {
      try {
         super.mouseClicked(x2, y2, z2);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      hwid.mouseClicked(x2, y2, z2);
   }

   protected void keyTyped(char character, int key) {
      hwid.textboxKeyTyped(character, key);
      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      if (key == 1) {
         this.mc.displayGuiScreen(new LoginGui());
      }

   }
}
