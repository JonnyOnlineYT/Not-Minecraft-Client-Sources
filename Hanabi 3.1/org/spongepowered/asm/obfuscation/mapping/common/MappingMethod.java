package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Objects;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingMethod implements IMapping {
   private final String owner;
   private final String name;
   private final String desc;

   public MappingMethod(String fullyQualifiedName, String desc) {
      this(getOwnerFromName(fullyQualifiedName), getBaseName(fullyQualifiedName), desc);
   }

   public MappingMethod(String owner, String simpleName, String desc) {
      this.owner = owner;
      this.name = simpleName;
      this.desc = desc;
   }

   public IMapping.Type getType() {
      return IMapping.Type.METHOD;
   }

   public String getName() {
      return this.name == null ? null : (this.owner != null ? this.owner + "/" : "") + this.name;
   }

   public String getSimpleName() {
      return this.name;
   }

   public String getOwner() {
      return this.owner;
   }

   public String getDesc() {
      return this.desc;
   }

   public MappingMethod getSuper() {
      return null;
   }

   public boolean isConstructor() {
      return "<init>".equals(this.name);
   }

   public MappingMethod move(String newOwner) {
      return new MappingMethod(newOwner, this.getSimpleName(), this.getDesc());
   }

   public MappingMethod remap(String newName) {
      return new MappingMethod(this.getOwner(), newName, this.getDesc());
   }

   public MappingMethod transform(String newDesc) {
      return new MappingMethod(this.getOwner(), this.getSimpleName(), newDesc);
   }

   public MappingMethod copy() {
      return new MappingMethod(this.getOwner(), this.getSimpleName(), this.getDesc());
   }

   public MappingMethod addPrefix(String prefix) {
      String simpleName = this.getSimpleName();
      return simpleName != null && !simpleName.startsWith(prefix) ? new MappingMethod(this.getOwner(), prefix + simpleName, this.getDesc()) : this;
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.getName(), this.getDesc()});
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof MappingMethod)) {
         return false;
      } else {
         return Objects.equal(this.name, ((MappingMethod)obj).name) && Objects.equal(this.desc, ((MappingMethod)obj).desc);
      }
   }

   public String serialise() {
      return this.toString();
   }

   public String toString() {
      String desc = this.getDesc();
      return String.format("%s%s%s", this.getName(), desc != null ? " " : "", desc != null ? desc : "");
   }

   private static String getBaseName(String name) {
      if (name == null) {
         return null;
      } else {
         int pos = name.lastIndexOf(47);
         return pos > -1 ? name.substring(pos + 1) : name;
      }
   }

   private static String getOwnerFromName(String name) {
      if (name == null) {
         return null;
      } else {
         int pos = name.lastIndexOf(47);
         return pos > -1 ? name.substring(0, pos) : null;
      }
   }
}
