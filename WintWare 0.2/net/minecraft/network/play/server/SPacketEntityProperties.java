package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient> {
   private int entityId;
   private final List<SPacketEntityProperties.Snapshot> snapshots = Lists.newArrayList();

   public SPacketEntityProperties() {
   }

   public SPacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> instances) {
      this.entityId = entityIdIn;
      Iterator var3 = instances.iterator();

      while(var3.hasNext()) {
         IAttributeInstance iattributeinstance = (IAttributeInstance)var3.next();
         this.snapshots.add(new SPacketEntityProperties.Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.getModifiers()));
      }

   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.entityId = buf.readVarIntFromBuffer();
      int i = buf.readInt();

      for(int j = 0; j < i; ++j) {
         String s = buf.readStringFromBuffer(64);
         double d0 = buf.readDouble();
         List<AttributeModifier> list = Lists.newArrayList();
         int k = buf.readVarIntFromBuffer();

         for(int l = 0; l < k; ++l) {
            UUID uuid = buf.readUuid();
            list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
         }

         this.snapshots.add(new SPacketEntityProperties.Snapshot(s, d0, list));
      }

   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarIntToBuffer(this.entityId);
      buf.writeInt(this.snapshots.size());
      Iterator var2 = this.snapshots.iterator();

      while(var2.hasNext()) {
         SPacketEntityProperties.Snapshot spacketentityproperties$snapshot = (SPacketEntityProperties.Snapshot)var2.next();
         buf.writeString(spacketentityproperties$snapshot.getName());
         buf.writeDouble(spacketentityproperties$snapshot.getBaseValue());
         buf.writeVarIntToBuffer(spacketentityproperties$snapshot.getModifiers().size());
         Iterator var4 = spacketentityproperties$snapshot.getModifiers().iterator();

         while(var4.hasNext()) {
            AttributeModifier attributemodifier = (AttributeModifier)var4.next();
            buf.writeUuid(attributemodifier.getID());
            buf.writeDouble(attributemodifier.getAmount());
            buf.writeByte(attributemodifier.getOperation());
         }
      }

   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleEntityProperties(this);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public List<SPacketEntityProperties.Snapshot> getSnapshots() {
      return this.snapshots;
   }

   public class Snapshot {
      private final String name;
      private final double baseValue;
      private final Collection<AttributeModifier> modifiers;

      public Snapshot(String nameIn, double baseValueIn, Collection<AttributeModifier> modifiersIn) {
         this.name = nameIn;
         this.baseValue = baseValueIn;
         this.modifiers = modifiersIn;
      }

      public String getName() {
         return this.name;
      }

      public double getBaseValue() {
         return this.baseValue;
      }

      public Collection<AttributeModifier> getModifiers() {
         return this.modifiers;
      }
   }
}
