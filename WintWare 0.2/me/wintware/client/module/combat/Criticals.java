package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {
   public Criticals() {
      super("Criticals", Category.Combat);
      ArrayList<String> mode = new ArrayList();
      mode.add("Packet");
      Main.instance.setmgr.rSetting(new Setting("Criticals Mode", this, "Packet", mode));
   }

   @EventTarget
   public void onPreMotion(EventSendPacket event) {
      String mode = Main.instance.setmgr.getSettingByName("Criticals Mode").getValString();
      if (event.getPacket() instanceof CPacketUseEntity) {
         double x = mc.player.posX;
         double y = mc.player.posY;
         double z = mc.player.posZ;
         Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.0625D, z, true));
         Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, false));
         Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y + 1.1E-5D, z, false));
         Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, false));
      }

   }
}
