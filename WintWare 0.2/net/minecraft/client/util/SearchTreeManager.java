package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;

public class SearchTreeManager implements IResourceManagerReloadListener {
   public static final SearchTreeManager.Key<ItemStack> field_194011_a = new SearchTreeManager.Key();
   public static final SearchTreeManager.Key<RecipeList> field_194012_b = new SearchTreeManager.Key();
   private final Map<SearchTreeManager.Key<?>, SearchTree<?>> field_194013_c = Maps.newHashMap();

   public void onResourceManagerReload(IResourceManager resourceManager) {
      Iterator var2 = this.field_194013_c.values().iterator();

      while(var2.hasNext()) {
         SearchTree<?> searchtree = (SearchTree)var2.next();
         searchtree.func_194040_a();
      }

   }

   public <T> void func_194009_a(SearchTreeManager.Key<T> p_194009_1_, SearchTree<T> p_194009_2_) {
      this.field_194013_c.put(p_194009_1_, p_194009_2_);
   }

   public <T> ISearchTree<T> func_194010_a(SearchTreeManager.Key<T> p_194010_1_) {
      return (ISearchTree)this.field_194013_c.get(p_194010_1_);
   }

   public static class Key<T> {
   }
}
