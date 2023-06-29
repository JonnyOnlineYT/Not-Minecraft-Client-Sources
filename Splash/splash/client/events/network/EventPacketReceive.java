package splash.client.events.network;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

/**
 * Author: Ice
 * Created: 17:27, 30-May-20
 * Project: Client
 */
public class EventPacketReceive extends Cancelable {

    private Packet receivedPacket;

    public EventPacketReceive(Packet sentPacket) {
        this.receivedPacket = sentPacket;
        
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.ticksExisted > 20) {
            if (getReceivedPacket() instanceof S02PacketChat) {
            	S02PacketChat s02PacketChat = (S02PacketChat) getReceivedPacket();

            	if (!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
            		String message = s02PacketChat.getChatComponent().getUnformattedText();

            		for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            			if (entity instanceof EntityPlayer) {
            				if (message.contains(entity.getName()) && message.contains(Minecraft.getMinecraft().thePlayer.getName()) && message.contains("killed") && !entity.getName().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getName())) {
            					EntityPlayer killedPlayer = (EntityPlayer) entity;
            					GuiMainMenu.lastKilled.add(entity.getName());
            				}
            			}
            		}
            	}
            }
        }
    }

    public Packet getReceivedPacket() {
        return receivedPacket;
    }

    public void setReceivedPacket(Packet receivedPacket) {
        this.receivedPacket = receivedPacket;
    }
}
