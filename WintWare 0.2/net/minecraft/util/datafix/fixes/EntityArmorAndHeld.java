package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData {
   public int getFixVersion() {
      return 100;
   }

   public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
      NBTTagList nbttaglist = compound.getTagList("Equipment", 10);
      NBTTagList nbttaglist4;
      if (!nbttaglist.hasNoTags() && !compound.hasKey("HandItems", 10)) {
         nbttaglist4 = new NBTTagList();
         nbttaglist4.appendTag(nbttaglist.get(0));
         nbttaglist4.appendTag(new NBTTagCompound());
         compound.setTag("HandItems", nbttaglist4);
      }

      if (nbttaglist.tagCount() > 1 && !compound.hasKey("ArmorItem", 10)) {
         nbttaglist4 = new NBTTagList();
         nbttaglist4.appendTag(nbttaglist.getCompoundTagAt(1));
         nbttaglist4.appendTag(nbttaglist.getCompoundTagAt(2));
         nbttaglist4.appendTag(nbttaglist.getCompoundTagAt(3));
         nbttaglist4.appendTag(nbttaglist.getCompoundTagAt(4));
         compound.setTag("ArmorItems", nbttaglist4);
      }

      compound.removeTag("Equipment");
      if (compound.hasKey("DropChances", 9)) {
         nbttaglist4 = compound.getTagList("DropChances", 5);
         NBTTagList nbttaglist5;
         if (!compound.hasKey("HandDropChances", 10)) {
            nbttaglist5 = new NBTTagList();
            nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(0)));
            nbttaglist5.appendTag(new NBTTagFloat(0.0F));
            compound.setTag("HandDropChances", nbttaglist5);
         }

         if (!compound.hasKey("ArmorDropChances", 10)) {
            nbttaglist5 = new NBTTagList();
            nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(1)));
            nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(2)));
            nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(3)));
            nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(4)));
            compound.setTag("ArmorDropChances", nbttaglist5);
         }

         compound.removeTag("DropChances");
      }

      return compound;
   }
}
