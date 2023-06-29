package net.minecraft.client.gui;

import java.awt.Color;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiButton extends Gui {
   protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
   protected int width;
   public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
   protected int height;
   public int xPosition;
   public int yPosition;
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected boolean hovered;
   private float hoverAnimation;

   public GuiButton(int buttonId, int x, int y, String buttonText) {
      this(buttonId, x, y, 200, 20, buttonText);
   }

   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      this.hoverAnimation = 0.0F;
      this.width = 200;
      this.height = 20;
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.width = widthIn;
      this.height = heightIn;
      this.displayString = buttonText;
   }

   protected int getHoverState(boolean mouseOver) {
      int i = 1;
      if (!this.enabled) {
         i = 0;
      } else if (mouseOver) {
         i = 2;
      }

      return i;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
      if (this.visible) {
         mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
         boolean hover = isHovered((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height));
         RenderUtil.drawSmoothRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), hover ? (new Color(57, 57, 57)).getRGB() : (new Color(28, 27, 27)).getRGB());
         this.mouseDragged(mc, mouseX, mouseY);
         float var10002 = (float)(this.xPosition + this.width / 2);
         Minecraft.getMinecraft().fontRenderer.drawCenteredString(this.displayString, var10002, (float)(this.yPosition + this.height / 2 - Minecraft.getMinecraft().fontRenderer.getStringHeight(this.displayString) / 2), -1);
      }

   }

   public static boolean isHovered(float x, float y, float width, float height) {
      return (float)getMouseX() > x && (float)getMouseX() < width && (float)getMouseY() > y && (float)getMouseY() < height;
   }

   public static int getMouseX() {
      return Mouse.getX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
   }

   public static int getMouseY() {
      return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
   }

   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
   }

   public void mouseReleased(int mouseX, int mouseY) {
   }

   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   public void drawButtonForegroundLayer(int mouseX, int mouseY) {
   }

   public void playPressSound(SoundHandler soundHandlerIn) {
      soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }
}
