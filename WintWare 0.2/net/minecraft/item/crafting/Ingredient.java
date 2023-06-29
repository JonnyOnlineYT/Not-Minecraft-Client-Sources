package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack> {
   public static final Ingredient field_193370_a = new Ingredient(new ItemStack[0]) {
      public boolean apply(@Nullable ItemStack p_apply_1_) {
         return p_apply_1_.func_190926_b();
      }
   };
   private final ItemStack[] field_193371_b;
   private IntList field_194140_c;

   private Ingredient(ItemStack... p_i47503_1_) {
      this.field_193371_b = p_i47503_1_;
   }

   public ItemStack[] func_193365_a() {
      return this.field_193371_b;
   }

   public boolean apply(@Nullable ItemStack p_apply_1_) {
      if (p_apply_1_ == null) {
         return false;
      } else {
         ItemStack[] var2 = this.field_193371_b;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack itemstack = var2[var4];
            if (itemstack.getItem() == p_apply_1_.getItem()) {
               int i = itemstack.getMetadata();
               if (i == 32767 || i == p_apply_1_.getMetadata()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public IntList func_194139_b() {
      if (this.field_194140_c == null) {
         this.field_194140_c = new IntArrayList(this.field_193371_b.length);
         ItemStack[] var1 = this.field_193371_b;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack itemstack = var1[var3];
            this.field_194140_c.add(RecipeItemHelper.func_194113_b(itemstack));
         }

         this.field_194140_c.sort(IntComparators.NATURAL_COMPARATOR);
      }

      return this.field_194140_c;
   }

   public static Ingredient func_193367_a(Item p_193367_0_) {
      return func_193369_a(new ItemStack(p_193367_0_, 1, 32767));
   }

   public static Ingredient func_193368_a(Item... p_193368_0_) {
      ItemStack[] aitemstack = new ItemStack[p_193368_0_.length];

      for(int i = 0; i < p_193368_0_.length; ++i) {
         aitemstack[i] = new ItemStack(p_193368_0_[i]);
      }

      return func_193369_a(aitemstack);
   }

   public static Ingredient func_193369_a(ItemStack... p_193369_0_) {
      if (p_193369_0_.length > 0) {
         ItemStack[] var1 = p_193369_0_;
         int var2 = p_193369_0_.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack itemstack = var1[var3];
            if (!itemstack.func_190926_b()) {
               return new Ingredient(p_193369_0_);
            }
         }
      }

      return field_193370_a;
   }

   // $FF: synthetic method
   Ingredient(ItemStack[] x0, Object x1) {
      this(x0);
   }
}
