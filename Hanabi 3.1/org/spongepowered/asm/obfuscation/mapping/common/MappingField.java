package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingField implements IMapping {
   private final String owner;
   private final String name;
   private final String desc;

   public MappingField(String owner, String name) {
      this(owner, name, (String)null);
   }

   public MappingField(String owner, String name, String desc) {
      this.owner = owner;
      this.name = name;
      this.desc = desc;
   }

   public IMapping.Type getType() {
      return IMapping.Type.FIELD;
   }

   public String getName() {
      return this.name;
   }

   public final String getSimpleName() {
      return this.name;
   }

   public final String getOwner() {
      return this.owner;
   }

   public final String getDesc() {
      return this.desc;
   }

   public MappingField getSuper() {
      return null;
   }

   public MappingField move(String newOwner) {
      return new MappingField(newOwner, this.getName(), this.getDesc());
   }

   public MappingField remap(String newName) {
      return new MappingField(this.getOwner(), newName, this.getDesc());
   }

   public MappingField transform(String newDesc) {
      return new MappingField(this.getOwner(), this.getName(), newDesc);
   }

   public MappingField copy() {
      return new MappingField(this.getOwner(), this.getName(), this.getDesc());
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.toString()});
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj instanceof MappingField ? Objects.equal(this.toString(), ((MappingField)obj).toString()) : false;
      }
   }

   public String serialise() {
      return this.toString();
   }

   public String toString() {
      return String.format("L%s;%s:%s", this.getOwner(), this.getName(), Strings.nullToEmpty(this.getDesc()));
   }
}
