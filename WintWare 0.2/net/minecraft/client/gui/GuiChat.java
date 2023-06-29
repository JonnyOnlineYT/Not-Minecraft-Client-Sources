package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen implements ITabCompleter {
   private static final Logger LOGGER = LogManager.getLogger();
   private String historyBuffer = "";
   private int sentHistoryCursor = -1;
   private TabCompleter tabCompleter;
   protected GuiTextField inputField;
   private long startTime;
   private String defaultInputFieldText = "";

   public GuiChat() {
   }

   public GuiChat(String defaultText) {
      this.defaultInputFieldText = defaultText;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new GuiTextField(0, this.fontRendererObj, 4, height - 12, width - 4, 12);
      this.inputField.setMaxStringLength(256);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setFocused(true);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setCanLoseFocus(false);
      this.tabCompleter = new GuiChat.ChatTabCompleter(this.inputField);
      this.startTime = System.currentTimeMillis();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
      this.startTime = System.currentTimeMillis();
   }

   public void updateScreen() {
      this.inputField.updateCursorCounter();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.tabCompleter.resetRequested();
      if (keyCode == 15) {
         this.tabCompleter.complete();
      } else {
         this.tabCompleter.resetDidComplete();
      }

      if (keyCode == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
      } else if (keyCode != 28 && keyCode != 156) {
         if (keyCode == 200) {
            this.getSentHistory(-1);
         } else if (keyCode == 208) {
            this.getSentHistory(1);
         } else if (keyCode == 201) {
            this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
         } else if (keyCode == 209) {
            this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
         } else {
            this.inputField.textboxKeyTyped(typedChar, keyCode);
         }
      } else {
         String s = this.inputField.getText().trim();
         if (!s.isEmpty()) {
            this.sendChatMessage(s);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int i = Mouse.getEventDWheel();
      if (i != 0) {
         if (i > 1) {
            i = 1;
         }

         if (i < -1) {
            i = -1;
         }

         if (!isShiftKeyDown()) {
            i *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(i);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0) {
         ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
         if (itextcomponent != null && this.handleComponentClick(itextcomponent)) {
            return;
         }
      }

      this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void setText(String newChatText, boolean shouldOverwrite) {
      if (shouldOverwrite) {
         this.inputField.setText(newChatText);
      } else {
         this.inputField.writeText(newChatText);
      }

   }

   public void getSentHistory(int msgPos) {
      int i = this.sentHistoryCursor + msgPos;
      int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      i = MathHelper.clamp(i, 0, j);
      if (i != this.sentHistoryCursor) {
         if (i == j) {
            this.sentHistoryCursor = j;
            this.inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == j) {
               this.historyBuffer = this.inputField.getText();
            }

            this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
            this.sentHistoryCursor = i;
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      double percent = (double)value(this.startTime);
      GlStateManager.translate(0.0D, 14.0D - 14.0D * percent, 0.0D);
      drawRect(2.0D, (double)(height - 14), (double)(width - 2), (double)(height - 2), Integer.MIN_VALUE);
      this.inputField.drawTextBox();
      this.inputField.drawTextBox();
      ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
         this.handleComponentHover(itextcomponent, mouseX, mouseY);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public static float value(long startTime) {
      return Math.min(1.0F, (float)Math.pow((double)(System.currentTimeMillis() - startTime) / 10.0D, 1.4D) / 80.0F);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void setCompletions(String... newCompletions) {
      this.tabCompleter.setCompletions(newCompletions);
   }

   public static class ChatTabCompleter extends TabCompleter {
      private final Minecraft clientInstance = Minecraft.getMinecraft();

      public ChatTabCompleter(GuiTextField p_i46749_1_) {
         super(p_i46749_1_, false);
      }

      public void complete() {
         super.complete();
         if (this.completions.size() > 1) {
            StringBuilder stringbuilder = new StringBuilder();

            String s;
            for(Iterator var2 = this.completions.iterator(); var2.hasNext(); stringbuilder.append(s)) {
               s = (String)var2.next();
               if (stringbuilder.length() > 0) {
                  stringbuilder.append(", ");
               }
            }

            this.clientInstance.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
         }

      }

      @Nullable
      public BlockPos getTargetBlockPos() {
         BlockPos blockpos = null;
         if (this.clientInstance.objectMouseOver != null && this.clientInstance.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            blockpos = this.clientInstance.objectMouseOver.getBlockPos();
         }

         return blockpos;
      }
   }
}
