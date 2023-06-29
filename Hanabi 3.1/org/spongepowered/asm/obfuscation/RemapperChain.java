package org.spongepowered.asm.obfuscation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public class RemapperChain implements IRemapper {
   private final List remappers = new ArrayList();

   public String toString() {
      return String.format("RemapperChain[%d]", this.remappers.size());
   }

   public RemapperChain add(IRemapper remapper) {
      this.remappers.add(remapper);
      return this;
   }

   public String mapMethodName(String owner, String name, String desc) {
      Iterator var4 = this.remappers.iterator();

      while(var4.hasNext()) {
         IRemapper remapper = (IRemapper)var4.next();
         String newName = remapper.mapMethodName(owner, name, desc);
         if (newName != null && !newName.equals(name)) {
            name = newName;
         }
      }

      return name;
   }

   public String mapFieldName(String owner, String name, String desc) {
      Iterator var4 = this.remappers.iterator();

      while(var4.hasNext()) {
         IRemapper remapper = (IRemapper)var4.next();
         String newName = remapper.mapFieldName(owner, name, desc);
         if (newName != null && !newName.equals(name)) {
            name = newName;
         }
      }

      return name;
   }

   public String map(String typeName) {
      Iterator var2 = this.remappers.iterator();

      while(var2.hasNext()) {
         IRemapper remapper = (IRemapper)var2.next();
         String newName = remapper.map(typeName);
         if (newName != null && !newName.equals(typeName)) {
            typeName = newName;
         }
      }

      return typeName;
   }

   public String unmap(String typeName) {
      Iterator var2 = this.remappers.iterator();

      while(var2.hasNext()) {
         IRemapper remapper = (IRemapper)var2.next();
         String newName = remapper.unmap(typeName);
         if (newName != null && !newName.equals(typeName)) {
            typeName = newName;
         }
      }

      return typeName;
   }

   public String mapDesc(String desc) {
      Iterator var2 = this.remappers.iterator();

      while(var2.hasNext()) {
         IRemapper remapper = (IRemapper)var2.next();
         String newDesc = remapper.mapDesc(desc);
         if (newDesc != null && !newDesc.equals(desc)) {
            desc = newDesc;
         }
      }

      return desc;
   }

   public String unmapDesc(String desc) {
      Iterator var2 = this.remappers.iterator();

      while(var2.hasNext()) {
         IRemapper remapper = (IRemapper)var2.next();
         String newDesc = remapper.unmapDesc(desc);
         if (newDesc != null && !newDesc.equals(desc)) {
            desc = newDesc;
         }
      }

      return desc;
   }
}
