package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;

public class GuiSleepMP extends GuiChat {
   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 40, I18n.format("multiplayer.stopSleeping")));
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.wakeFromSleep();
      } else if (keyCode != 28 && keyCode != 156) {
         super.keyTyped(typedChar, keyCode);
      } else {
         String s = this.inputField.getText().trim();
         if (!s.isEmpty()) {
            this.mc.player.sendChatMessage(s);
         }

         this.inputField.setText("");
         this.mc.ingameGUI.getChatGUI().resetScroll();
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 1) {
         this.wakeFromSleep();
      } else {
         super.actionPerformed(button);
      }

   }

   private void wakeFromSleep() {
      NetHandlerPlayClient nethandlerplayclient = this.mc.player.connection;
      nethandlerplayclient.sendPacket(new CPacketEntityAction(this.mc.player, CPacketEntityAction.Action.STOP_SLEEPING));
   }
}
