package net.minecraft.item.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CraftingManager {
   private static final Logger field_192422_a = LogManager.getLogger();
   private static int field_193381_c;
   public static final RegistryNamespaced<ResourceLocation, IRecipe> field_193380_a = new RegistryNamespaced();

   public static boolean func_193377_a() {
      try {
         func_193379_a("armordye", new RecipesArmorDyes());
         func_193379_a("bookcloning", new RecipeBookCloning());
         func_193379_a("mapcloning", new RecipesMapCloning());
         func_193379_a("mapextending", new RecipesMapExtending());
         func_193379_a("fireworks", new RecipeFireworks());
         func_193379_a("repairitem", new RecipeRepairItem());
         func_193379_a("tippedarrow", new RecipeTippedArrow());
         func_193379_a("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
         func_193379_a("banneraddpattern", new RecipesBanners.RecipeAddPattern());
         func_193379_a("shielddecoration", new ShieldRecipes.Decoration());
         func_193379_a("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
         return func_192420_c();
      } catch (Throwable var1) {
         return false;
      }
   }

   private static boolean func_192420_c() {
      FileSystem filesystem = null;
      Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

      try {
         boolean flag1;
         try {
            URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");
            if (url == null) {
               field_192422_a.error("Couldn't find .mcassetsroot");
               flag1 = false;
               return flag1;
            } else {
               URI uri = url.toURI();
               boolean var34;
               Path path;
               if ("file".equals(uri.getScheme())) {
                  path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
               } else {
                  if (!"jar".equals(uri.getScheme())) {
                     field_192422_a.error("Unsupported scheme " + uri + " trying to list all recipes");
                     boolean flag2 = false;
                     var34 = flag2;
                     return var34;
                  }

                  filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                  path = filesystem.getPath("/assets/minecraft/recipes");
               }

               Iterator iterator = Files.walk(path).iterator();

               while(iterator.hasNext()) {
                  Path path1 = (Path)iterator.next();
                  if ("json".equals(FilenameUtils.getExtension(path1.toString()))) {
                     Path path2 = path.relativize(path1);
                     String s = FilenameUtils.removeExtension(path2.toString()).replaceAll("\\\\", "/");
                     ResourceLocation resourcelocation = new ResourceLocation(s);
                     BufferedReader bufferedreader = null;

                     try {
                        boolean flag;
                        boolean var14;
                        try {
                           bufferedreader = Files.newBufferedReader(path1);
                           func_193379_a(s, func_193376_a((JsonObject)JsonUtils.func_193839_a(gson, bufferedreader, JsonObject.class)));
                        } catch (JsonParseException var27) {
                           field_192422_a.error("Parsing error loading recipe " + resourcelocation, var27);
                           flag = false;
                           var14 = flag;
                           return var14;
                        } catch (IOException var28) {
                           field_192422_a.error("Couldn't read recipe " + resourcelocation + " from " + path1, var28);
                           flag = false;
                           var14 = flag;
                           return var14;
                        }
                     } finally {
                        IOUtils.closeQuietly(bufferedreader);
                     }
                  }
               }

               var34 = true;
               return var34;
            }
         } catch (URISyntaxException | IOException var30) {
            field_192422_a.error("Couldn't get a list of all recipe files", var30);
            flag1 = false;
            boolean var4 = flag1;
            return var4;
         }
      } finally {
         IOUtils.closeQuietly(filesystem);
      }
   }

   private static IRecipe func_193376_a(JsonObject p_193376_0_) {
      String s = JsonUtils.getString(p_193376_0_, "type");
      if ("crafting_shaped".equals(s)) {
         return ShapedRecipes.func_193362_a(p_193376_0_);
      } else if ("crafting_shapeless".equals(s)) {
         return ShapelessRecipes.func_193363_a(p_193376_0_);
      } else {
         throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
      }
   }

   public static void func_193379_a(String p_193379_0_, IRecipe p_193379_1_) {
      func_193372_a(new ResourceLocation(p_193379_0_), p_193379_1_);
   }

   public static void func_193372_a(ResourceLocation p_193372_0_, IRecipe p_193372_1_) {
      if (field_193380_a.containsKey(p_193372_0_)) {
         throw new IllegalStateException("Duplicate recipe ignored with ID " + p_193372_0_);
      } else {
         field_193380_a.register(field_193381_c++, p_193372_0_, p_193372_1_);
      }
   }

   public static ItemStack findMatchingRecipe(InventoryCrafting p_82787_0_, World craftMatrix) {
      Iterator var2 = field_193380_a.iterator();

      IRecipe irecipe;
      do {
         if (!var2.hasNext()) {
            return ItemStack.field_190927_a;
         }

         irecipe = (IRecipe)var2.next();
      } while(!irecipe.matches(p_82787_0_, craftMatrix));

      return irecipe.getCraftingResult(p_82787_0_);
   }

   @Nullable
   public static IRecipe func_192413_b(InventoryCrafting p_192413_0_, World p_192413_1_) {
      Iterator var2 = field_193380_a.iterator();

      IRecipe irecipe;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         irecipe = (IRecipe)var2.next();
      } while(!irecipe.matches(p_192413_0_, p_192413_1_));

      return irecipe;
   }

   public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting p_180303_0_, World craftMatrix) {
      Iterator var2 = field_193380_a.iterator();

      while(var2.hasNext()) {
         IRecipe irecipe = (IRecipe)var2.next();
         if (irecipe.matches(p_180303_0_, craftMatrix)) {
            return irecipe.getRemainingItems(p_180303_0_);
         }
      }

      NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(p_180303_0_.getSizeInventory(), ItemStack.field_190927_a);

      for(int i = 0; i < nonnulllist.size(); ++i) {
         nonnulllist.set(i, p_180303_0_.getStackInSlot(i));
      }

      return nonnulllist;
   }

   @Nullable
   public static IRecipe func_193373_a(ResourceLocation p_193373_0_) {
      return (IRecipe)field_193380_a.getObject(p_193373_0_);
   }

   public static int func_193375_a(IRecipe p_193375_0_) {
      return field_193380_a.getIDForObject(p_193375_0_);
   }

   @Nullable
   public static IRecipe func_193374_a(int p_193374_0_) {
      return (IRecipe)field_193380_a.getObjectById(p_193374_0_);
   }
}
