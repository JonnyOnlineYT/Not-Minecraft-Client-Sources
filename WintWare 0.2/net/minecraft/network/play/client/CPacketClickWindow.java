package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer> {
   private int windowId;
   private int slotId;
   private int usedButton;
   private short actionNumber;
   private ItemStack clickedItem;
   private ClickType mode;

   public CPacketClickWindow() {
      this.clickedItem = ItemStack.field_190927_a;
   }

   public CPacketClickWindow(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn) {
      this.clickedItem = ItemStack.field_190927_a;
      this.windowId = windowIdIn;
      this.slotId = slotIdIn;
      this.usedButton = usedButtonIn;
      this.clickedItem = clickedItemIn.copy();
      this.actionNumber = actionNumberIn;
      this.mode = modeIn;
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processClickWindow(this);
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.windowId = buf.readByte();
      this.slotId = buf.readShort();
      this.usedButton = buf.readByte();
      this.actionNumber = buf.readShort();
      this.mode = (ClickType)buf.readEnumValue(ClickType.class);
      this.clickedItem = buf.readItemStackFromBuffer();
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeByte(this.windowId);
      buf.writeShort(this.slotId);
      buf.writeByte(this.usedButton);
      buf.writeShort(this.actionNumber);
      buf.writeEnumValue(this.mode);
      buf.writeItemStackToBuffer(this.clickedItem);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getSlotId() {
      return this.slotId;
   }

   public int getUsedButton() {
      return this.usedButton;
   }

   public short getActionNumber() {
      return this.actionNumber;
   }

   public ItemStack getClickedItem() {
      return this.clickedItem;
   }

   public ClickType getClickType() {
      return this.mode;
   }
}
