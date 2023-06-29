package net.minecraft.creativetab;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

public abstract class CreativeTabs {
   public static final CreativeTabs[] CREATIVE_TAB_ARRAY = new CreativeTabs[12];
   public static final CreativeTabs BUILDING_BLOCKS = new CreativeTabs(0, "buildingBlocks") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK));
      }
   };
   public static final CreativeTabs DECORATIONS = new CreativeTabs(1, "decorations") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta());
      }
   };
   public static final CreativeTabs REDSTONE = new CreativeTabs(2, "redstone") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Items.REDSTONE);
      }
   };
   public static final CreativeTabs TRANSPORTATION = new CreativeTabs(3, "transportation") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Item.getItemFromBlock(Blocks.GOLDEN_RAIL));
      }
   };
   public static final CreativeTabs MISC = new CreativeTabs(6, "misc") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Items.LAVA_BUCKET);
      }
   };
   public static final CreativeTabs SEARCH = (new CreativeTabs(5, "search") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Items.COMPASS);
      }
   }).setBackgroundImageName("item_search.png");
   public static final CreativeTabs FOOD = new CreativeTabs(7, "food") {
      public ItemStack getTabIconItem() {
         return new ItemStack(Items.APPLE);
      }
   };
   public static final CreativeTabs TOOLS;
   public static final CreativeTabs COMBAT;
   public static final CreativeTabs BREWING;
   public static final CreativeTabs MATERIALS;
   public static final CreativeTabs field_192395_m;
   public static final CreativeTabs INVENTORY;
   private final int tabIndex;
   private final String tabLabel;
   private String theTexture = "items.png";
   private boolean hasScrollbar = true;
   private boolean drawTitle = true;
   private EnumEnchantmentType[] enchantmentTypes = new EnumEnchantmentType[0];
   private ItemStack iconItemStack;

   public CreativeTabs(int index, String label) {
      this.tabIndex = index;
      this.tabLabel = label;
      this.iconItemStack = ItemStack.field_190927_a;
      CREATIVE_TAB_ARRAY[index] = this;
   }

   public int getTabIndex() {
      return this.tabIndex;
   }

   public String getTabLabel() {
      return this.tabLabel;
   }

   public String getTranslatedTabLabel() {
      return "itemGroup." + this.getTabLabel();
   }

   public ItemStack getIconItemStack() {
      if (this.iconItemStack.func_190926_b()) {
         this.iconItemStack = this.getTabIconItem();
      }

      return this.iconItemStack;
   }

   public abstract ItemStack getTabIconItem();

   public String getBackgroundImageName() {
      return this.theTexture;
   }

   public CreativeTabs setBackgroundImageName(String texture) {
      this.theTexture = texture;
      return this;
   }

   public boolean drawInForegroundOfTab() {
      return this.drawTitle;
   }

   public CreativeTabs setNoTitle() {
      this.drawTitle = false;
      return this;
   }

   public boolean shouldHidePlayerInventory() {
      return this.hasScrollbar;
   }

   public CreativeTabs setNoScrollbar() {
      this.hasScrollbar = false;
      return this;
   }

   public int getTabColumn() {
      return this.tabIndex % 6;
   }

   public boolean isTabInFirstRow() {
      return this.tabIndex < 6;
   }

   public boolean func_192394_m() {
      return this.getTabColumn() == 5;
   }

   public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
      return this.enchantmentTypes;
   }

   public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types) {
      this.enchantmentTypes = types;
      return this;
   }

   public boolean hasRelevantEnchantmentType(@Nullable EnumEnchantmentType enchantmentType) {
      if (enchantmentType != null) {
         EnumEnchantmentType[] var2 = this.enchantmentTypes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EnumEnchantmentType enumenchantmenttype = var2[var4];
            if (enumenchantmenttype == enchantmentType) {
               return true;
            }
         }
      }

      return false;
   }

   public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
      Iterator var2 = Item.REGISTRY.iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         item.getSubItems(this, p_78018_1_);
      }

   }

   static {
      TOOLS = (new CreativeTabs(8, "tools") {
         public ItemStack getTabIconItem() {
            return new ItemStack(Items.IRON_AXE);
         }
      }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
      COMBAT = (new CreativeTabs(9, "combat") {
         public ItemStack getTabIconItem() {
            return new ItemStack(Items.GOLDEN_SWORD);
         }
      }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE});
      BREWING = new CreativeTabs(10, "brewing") {
         public ItemStack getTabIconItem() {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
         }
      };
      MATERIALS = MISC;
      field_192395_m = new CreativeTabs(4, "hotbar") {
         public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.BOOKSHELF);
         }

         public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
            throw new RuntimeException("Implement exception client-side.");
         }

         public boolean func_192394_m() {
            return true;
         }
      };
      INVENTORY = (new CreativeTabs(11, "inventory") {
         public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(Blocks.CHEST));
         }
      }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
   }
}
