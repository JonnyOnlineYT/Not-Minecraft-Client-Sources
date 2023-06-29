package net.minecraft.util.datafix.walkers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;

public class ItemStackData extends Filtered {
   private final String[] matchingTags;

   public ItemStackData(Class<?> p_i47311_1_, String... p_i47311_2_) {
      super(p_i47311_1_);
      this.matchingTags = p_i47311_2_;
   }

   NBTTagCompound filteredProcess(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
      String[] var4 = this.matchingTags;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String s = var4[var6];
         compound = DataFixesManager.processItemStack(fixer, compound, versionIn, s);
      }

      return compound;
   }
}
