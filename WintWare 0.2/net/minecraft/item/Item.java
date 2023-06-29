package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class Item {
   public static final RegistryNamespaced<ResourceLocation, Item> REGISTRY = new RegistryNamespaced();
   private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.newHashMap();
   private static final IItemPropertyGetter DAMAGED_GETTER = new IItemPropertyGetter() {
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
         return stack.isItemDamaged() ? 1.0F : 0.0F;
      }
   };
   private static final IItemPropertyGetter DAMAGE_GETTER = new IItemPropertyGetter() {
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
         return MathHelper.clamp((float)stack.getItemDamage() / (float)stack.getMaxDamage(), 0.0F, 1.0F);
      }
   };
   private static final IItemPropertyGetter LEFTHANDED_GETTER = new IItemPropertyGetter() {
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
         return entityIn != null && entityIn.getPrimaryHand() != EnumHandSide.RIGHT ? 1.0F : 0.0F;
      }
   };
   private static final IItemPropertyGetter COOLDOWN_GETTER = new IItemPropertyGetter() {
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
         return entityIn instanceof EntityPlayer ? ((EntityPlayer)entityIn).getCooldownTracker().getCooldown(stack.getItem(), 0.0F) : 0.0F;
      }
   };
   private final IRegistry<ResourceLocation, IItemPropertyGetter> properties = new RegistrySimple();
   protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
   protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
   private CreativeTabs tabToDisplayOn;
   protected static Random itemRand = new Random();
   protected int maxStackSize = 64;
   private int maxDamage;
   protected boolean bFull3D;
   protected boolean hasSubtypes;
   private Item containerItem;
   private String unlocalizedName;

   public static int getIdFromItem(Item itemIn) {
      return itemIn == null ? 0 : REGISTRY.getIDForObject(itemIn);
   }

   public static Item getItemById(int id) {
      return (Item)REGISTRY.getObjectById(id);
   }

   public static Item getItemFromBlock(Block blockIn) {
      Item item = (Item)BLOCK_TO_ITEM.get(blockIn);
      return item == null ? Items.field_190931_a : item;
   }

   @Nullable
   public static Item getByNameOrId(String id) {
      Item item = (Item)REGISTRY.getObject(new ResourceLocation(id));
      if (item == null) {
         try {
            return getItemById(Integer.parseInt(id));
         } catch (NumberFormatException var3) {
         }
      }

      return item;
   }

   public final void addPropertyOverride(ResourceLocation key, IItemPropertyGetter getter) {
      this.properties.putObject(key, getter);
   }

   @Nullable
   public IItemPropertyGetter getPropertyGetter(ResourceLocation key) {
      return (IItemPropertyGetter)this.properties.getObject(key);
   }

   public boolean hasCustomProperties() {
      return !this.properties.getKeys().isEmpty();
   }

   public boolean updateItemStackNBT(NBTTagCompound nbt) {
      return false;
   }

   public Item() {
      this.addPropertyOverride(new ResourceLocation("lefthanded"), LEFTHANDED_GETTER);
      this.addPropertyOverride(new ResourceLocation("cooldown"), COOLDOWN_GETTER);
   }

   public Item setMaxStackSize(int maxStackSize) {
      this.maxStackSize = maxStackSize;
      return this;
   }

   public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
      return EnumActionResult.PASS;
   }

   public float getStrVsBlock(ItemStack stack, IBlockState state) {
      return 1.0F;
   }

   public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
      return new ActionResult(EnumActionResult.PASS, worldIn.getHeldItem(playerIn));
   }

   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
      return stack;
   }

   public int getItemStackLimit() {
      return this.maxStackSize;
   }

   public int getMetadata(int damage) {
      return 0;
   }

   public boolean getHasSubtypes() {
      return this.hasSubtypes;
   }

   protected Item setHasSubtypes(boolean hasSubtypes) {
      this.hasSubtypes = hasSubtypes;
      return this;
   }

   public int getMaxDamage() {
      return this.maxDamage;
   }

   protected Item setMaxDamage(int maxDamageIn) {
      this.maxDamage = maxDamageIn;
      if (maxDamageIn > 0) {
         this.addPropertyOverride(new ResourceLocation("damaged"), DAMAGED_GETTER);
         this.addPropertyOverride(new ResourceLocation("damage"), DAMAGE_GETTER);
      }

      return this;
   }

   public boolean isDamageable() {
      return this.maxDamage > 0 && (!this.hasSubtypes || this.maxStackSize == 1);
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
      return false;
   }

   public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
      return false;
   }

   public boolean canHarvestBlock(IBlockState blockIn) {
      return false;
   }

   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
      return false;
   }

   public Item setFull3D() {
      this.bFull3D = true;
      return this;
   }

   public boolean isFull3D() {
      return this.bFull3D;
   }

   public boolean shouldRotateAroundWhenRendering() {
      return false;
   }

   public Item setUnlocalizedName(String unlocalizedName) {
      this.unlocalizedName = unlocalizedName;
      return this;
   }

   public String getUnlocalizedNameInefficiently(ItemStack stack) {
      return I18n.translateToLocal(this.getUnlocalizedName(stack));
   }

   public String getUnlocalizedName() {
      return "item." + this.unlocalizedName;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return "item." + this.unlocalizedName;
   }

   public Item setContainerItem(Item containerItem) {
      this.containerItem = containerItem;
      return this;
   }

   public boolean getShareTag() {
      return true;
   }

   @Nullable
   public Item getContainerItem() {
      return this.containerItem;
   }

   public boolean hasContainerItem() {
      return this.containerItem != null;
   }

   public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
   }

   public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
   }

   public boolean isMap() {
      return false;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.NONE;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 0;
   }

   public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
   }

   public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
   }

   public String getItemStackDisplayName(ItemStack stack) {
      return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
   }

   public boolean hasEffect(ItemStack stack) {
      return stack.isItemEnchanted();
   }

   public EnumRarity getRarity(ItemStack stack) {
      return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
   }

   public boolean isItemTool(ItemStack stack) {
      return this.getItemStackLimit() == 1 && this.isDamageable();
   }

   protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
      float f = playerIn.rotationPitch;
      float f1 = playerIn.rotationYaw;
      double d0 = playerIn.posX;
      double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
      double d2 = playerIn.posZ;
      Vec3d vec3d = new Vec3d(d0, d1, d2);
      float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
      float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
      float f4 = -MathHelper.cos(-f * 0.017453292F);
      float f5 = MathHelper.sin(-f * 0.017453292F);
      float f6 = f3 * f4;
      float f7 = f2 * f4;
      double d3 = 5.0D;
      Vec3d vec3d1 = vec3d.addVector((double)f6 * 5.0D, (double)f5 * 5.0D, (double)f7 * 5.0D);
      return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
   }

   public int getItemEnchantability() {
      return 0;
   }

   public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      if (this.func_194125_a(itemIn)) {
         tab.add(new ItemStack(this));
      }

   }

   protected boolean func_194125_a(CreativeTabs p_194125_1_) {
      CreativeTabs creativetabs = this.getCreativeTab();
      return creativetabs != null && (p_194125_1_ == CreativeTabs.SEARCH || p_194125_1_ == creativetabs);
   }

   @Nullable
   public CreativeTabs getCreativeTab() {
      return this.tabToDisplayOn;
   }

   public Item setCreativeTab(CreativeTabs tab) {
      this.tabToDisplayOn = tab;
      return this;
   }

   public boolean canItemEditBlocks() {
      return false;
   }

   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
      return false;
   }

   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
      return HashMultimap.create();
   }

   public static void registerItems() {
      registerItemBlock(Blocks.AIR, new ItemAir(Blocks.AIR));
      registerItemBlock(Blocks.STONE, (new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("stone"));
      registerItemBlock(Blocks.GRASS, new ItemColored(Blocks.GRASS, false));
      registerItemBlock(Blocks.DIRT, (new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockDirt.DirtType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("dirt"));
      registerItemBlock(Blocks.COBBLESTONE);
      registerItemBlock(Blocks.PLANKS, (new ItemMultiTexture(Blocks.PLANKS, Blocks.PLANKS, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("wood"));
      registerItemBlock(Blocks.SAPLING, (new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("sapling"));
      registerItemBlock(Blocks.BEDROCK);
      registerItemBlock(Blocks.SAND, (new ItemMultiTexture(Blocks.SAND, Blocks.SAND, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockSand.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("sand"));
      registerItemBlock(Blocks.GRAVEL);
      registerItemBlock(Blocks.GOLD_ORE);
      registerItemBlock(Blocks.IRON_ORE);
      registerItemBlock(Blocks.COAL_ORE);
      registerItemBlock(Blocks.LOG, (new ItemMultiTexture(Blocks.LOG, Blocks.LOG, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("log"));
      registerItemBlock(Blocks.LOG2, (new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata() + 4).getUnlocalizedName();
         }
      })).setUnlocalizedName("log"));
      registerItemBlock(Blocks.LEAVES, (new ItemLeaves(Blocks.LEAVES)).setUnlocalizedName("leaves"));
      registerItemBlock(Blocks.LEAVES2, (new ItemLeaves(Blocks.LEAVES2)).setUnlocalizedName("leaves"));
      registerItemBlock(Blocks.SPONGE, (new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return (p_apply_1_.getMetadata() & 1) == 1 ? "wet" : "dry";
         }
      })).setUnlocalizedName("sponge"));
      registerItemBlock(Blocks.GLASS);
      registerItemBlock(Blocks.LAPIS_ORE);
      registerItemBlock(Blocks.LAPIS_BLOCK);
      registerItemBlock(Blocks.DISPENSER);
      registerItemBlock(Blocks.SANDSTONE, (new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockSandStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("sandStone"));
      registerItemBlock(Blocks.NOTEBLOCK);
      registerItemBlock(Blocks.GOLDEN_RAIL);
      registerItemBlock(Blocks.DETECTOR_RAIL);
      registerItemBlock(Blocks.STICKY_PISTON, new ItemPiston(Blocks.STICKY_PISTON));
      registerItemBlock(Blocks.WEB);
      registerItemBlock(Blocks.TALLGRASS, (new ItemColored(Blocks.TALLGRASS, true)).setSubtypeNames(new String[]{"shrub", "grass", "fern"}));
      registerItemBlock(Blocks.DEADBUSH);
      registerItemBlock(Blocks.PISTON, new ItemPiston(Blocks.PISTON));
      registerItemBlock(Blocks.WOOL, (new ItemCloth(Blocks.WOOL)).setUnlocalizedName("cloth"));
      registerItemBlock(Blocks.YELLOW_FLOWER, (new ItemMultiTexture(Blocks.YELLOW_FLOWER, Blocks.YELLOW_FLOWER, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("flower"));
      registerItemBlock(Blocks.RED_FLOWER, (new ItemMultiTexture(Blocks.RED_FLOWER, Blocks.RED_FLOWER, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("rose"));
      registerItemBlock(Blocks.BROWN_MUSHROOM);
      registerItemBlock(Blocks.RED_MUSHROOM);
      registerItemBlock(Blocks.GOLD_BLOCK);
      registerItemBlock(Blocks.IRON_BLOCK);
      registerItemBlock(Blocks.STONE_SLAB, (new ItemSlab(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.DOUBLE_STONE_SLAB)).setUnlocalizedName("stoneSlab"));
      registerItemBlock(Blocks.BRICK_BLOCK);
      registerItemBlock(Blocks.TNT);
      registerItemBlock(Blocks.BOOKSHELF);
      registerItemBlock(Blocks.MOSSY_COBBLESTONE);
      registerItemBlock(Blocks.OBSIDIAN);
      registerItemBlock(Blocks.TORCH);
      registerItemBlock(Blocks.END_ROD);
      registerItemBlock(Blocks.CHORUS_PLANT);
      registerItemBlock(Blocks.CHORUS_FLOWER);
      registerItemBlock(Blocks.PURPUR_BLOCK);
      registerItemBlock(Blocks.PURPUR_PILLAR);
      registerItemBlock(Blocks.PURPUR_STAIRS);
      registerItemBlock(Blocks.PURPUR_SLAB, (new ItemSlab(Blocks.PURPUR_SLAB, Blocks.PURPUR_SLAB, Blocks.PURPUR_DOUBLE_SLAB)).setUnlocalizedName("purpurSlab"));
      registerItemBlock(Blocks.MOB_SPAWNER);
      registerItemBlock(Blocks.OAK_STAIRS);
      registerItemBlock(Blocks.CHEST);
      registerItemBlock(Blocks.DIAMOND_ORE);
      registerItemBlock(Blocks.DIAMOND_BLOCK);
      registerItemBlock(Blocks.CRAFTING_TABLE);
      registerItemBlock(Blocks.FARMLAND);
      registerItemBlock(Blocks.FURNACE);
      registerItemBlock(Blocks.LADDER);
      registerItemBlock(Blocks.RAIL);
      registerItemBlock(Blocks.STONE_STAIRS);
      registerItemBlock(Blocks.LEVER);
      registerItemBlock(Blocks.STONE_PRESSURE_PLATE);
      registerItemBlock(Blocks.WOODEN_PRESSURE_PLATE);
      registerItemBlock(Blocks.REDSTONE_ORE);
      registerItemBlock(Blocks.REDSTONE_TORCH);
      registerItemBlock(Blocks.STONE_BUTTON);
      registerItemBlock(Blocks.SNOW_LAYER, new ItemSnow(Blocks.SNOW_LAYER));
      registerItemBlock(Blocks.ICE);
      registerItemBlock(Blocks.SNOW);
      registerItemBlock(Blocks.CACTUS);
      registerItemBlock(Blocks.CLAY);
      registerItemBlock(Blocks.JUKEBOX);
      registerItemBlock(Blocks.OAK_FENCE);
      registerItemBlock(Blocks.SPRUCE_FENCE);
      registerItemBlock(Blocks.BIRCH_FENCE);
      registerItemBlock(Blocks.JUNGLE_FENCE);
      registerItemBlock(Blocks.DARK_OAK_FENCE);
      registerItemBlock(Blocks.ACACIA_FENCE);
      registerItemBlock(Blocks.PUMPKIN);
      registerItemBlock(Blocks.NETHERRACK);
      registerItemBlock(Blocks.SOUL_SAND);
      registerItemBlock(Blocks.GLOWSTONE);
      registerItemBlock(Blocks.LIT_PUMPKIN);
      registerItemBlock(Blocks.TRAPDOOR);
      registerItemBlock(Blocks.MONSTER_EGG, (new ItemMultiTexture(Blocks.MONSTER_EGG, Blocks.MONSTER_EGG, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockSilverfish.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("monsterStoneEgg"));
      registerItemBlock(Blocks.STONEBRICK, (new ItemMultiTexture(Blocks.STONEBRICK, Blocks.STONEBRICK, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockStoneBrick.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("stonebricksmooth"));
      registerItemBlock(Blocks.BROWN_MUSHROOM_BLOCK);
      registerItemBlock(Blocks.RED_MUSHROOM_BLOCK);
      registerItemBlock(Blocks.IRON_BARS);
      registerItemBlock(Blocks.GLASS_PANE);
      registerItemBlock(Blocks.MELON_BLOCK);
      registerItemBlock(Blocks.VINE, new ItemColored(Blocks.VINE, false));
      registerItemBlock(Blocks.OAK_FENCE_GATE);
      registerItemBlock(Blocks.SPRUCE_FENCE_GATE);
      registerItemBlock(Blocks.BIRCH_FENCE_GATE);
      registerItemBlock(Blocks.JUNGLE_FENCE_GATE);
      registerItemBlock(Blocks.DARK_OAK_FENCE_GATE);
      registerItemBlock(Blocks.ACACIA_FENCE_GATE);
      registerItemBlock(Blocks.BRICK_STAIRS);
      registerItemBlock(Blocks.STONE_BRICK_STAIRS);
      registerItemBlock(Blocks.MYCELIUM);
      registerItemBlock(Blocks.WATERLILY, new ItemLilyPad(Blocks.WATERLILY));
      registerItemBlock(Blocks.NETHER_BRICK);
      registerItemBlock(Blocks.NETHER_BRICK_FENCE);
      registerItemBlock(Blocks.NETHER_BRICK_STAIRS);
      registerItemBlock(Blocks.ENCHANTING_TABLE);
      registerItemBlock(Blocks.END_PORTAL_FRAME);
      registerItemBlock(Blocks.END_STONE);
      registerItemBlock(Blocks.END_BRICKS);
      registerItemBlock(Blocks.DRAGON_EGG);
      registerItemBlock(Blocks.REDSTONE_LAMP);
      registerItemBlock(Blocks.WOODEN_SLAB, (new ItemSlab(Blocks.WOODEN_SLAB, Blocks.WOODEN_SLAB, Blocks.DOUBLE_WOODEN_SLAB)).setUnlocalizedName("woodSlab"));
      registerItemBlock(Blocks.SANDSTONE_STAIRS);
      registerItemBlock(Blocks.EMERALD_ORE);
      registerItemBlock(Blocks.ENDER_CHEST);
      registerItemBlock(Blocks.TRIPWIRE_HOOK);
      registerItemBlock(Blocks.EMERALD_BLOCK);
      registerItemBlock(Blocks.SPRUCE_STAIRS);
      registerItemBlock(Blocks.BIRCH_STAIRS);
      registerItemBlock(Blocks.JUNGLE_STAIRS);
      registerItemBlock(Blocks.COMMAND_BLOCK);
      registerItemBlock(Blocks.BEACON);
      registerItemBlock(Blocks.COBBLESTONE_WALL, (new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockWall.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("cobbleWall"));
      registerItemBlock(Blocks.WOODEN_BUTTON);
      registerItemBlock(Blocks.ANVIL, (new ItemAnvilBlock(Blocks.ANVIL)).setUnlocalizedName("anvil"));
      registerItemBlock(Blocks.TRAPPED_CHEST);
      registerItemBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
      registerItemBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      registerItemBlock(Blocks.DAYLIGHT_DETECTOR);
      registerItemBlock(Blocks.REDSTONE_BLOCK);
      registerItemBlock(Blocks.QUARTZ_ORE);
      registerItemBlock(Blocks.HOPPER);
      registerItemBlock(Blocks.QUARTZ_BLOCK, (new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[]{"default", "chiseled", "lines"})).setUnlocalizedName("quartzBlock"));
      registerItemBlock(Blocks.QUARTZ_STAIRS);
      registerItemBlock(Blocks.ACTIVATOR_RAIL);
      registerItemBlock(Blocks.DROPPER);
      registerItemBlock(Blocks.STAINED_HARDENED_CLAY, (new ItemCloth(Blocks.STAINED_HARDENED_CLAY)).setUnlocalizedName("clayHardenedStained"));
      registerItemBlock(Blocks.BARRIER);
      registerItemBlock(Blocks.IRON_TRAPDOOR);
      registerItemBlock(Blocks.HAY_BLOCK);
      registerItemBlock(Blocks.CARPET, (new ItemCloth(Blocks.CARPET)).setUnlocalizedName("woolCarpet"));
      registerItemBlock(Blocks.HARDENED_CLAY);
      registerItemBlock(Blocks.COAL_BLOCK);
      registerItemBlock(Blocks.PACKED_ICE);
      registerItemBlock(Blocks.ACACIA_STAIRS);
      registerItemBlock(Blocks.DARK_OAK_STAIRS);
      registerItemBlock(Blocks.SLIME_BLOCK);
      registerItemBlock(Blocks.GRASS_PATH);
      registerItemBlock(Blocks.DOUBLE_PLANT, (new ItemMultiTexture(Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockDoublePlant.EnumPlantType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("doublePlant"));
      registerItemBlock(Blocks.STAINED_GLASS, (new ItemCloth(Blocks.STAINED_GLASS)).setUnlocalizedName("stainedGlass"));
      registerItemBlock(Blocks.STAINED_GLASS_PANE, (new ItemCloth(Blocks.STAINED_GLASS_PANE)).setUnlocalizedName("stainedGlassPane"));
      registerItemBlock(Blocks.PRISMARINE, (new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockPrismarine.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("prismarine"));
      registerItemBlock(Blocks.SEA_LANTERN);
      registerItemBlock(Blocks.RED_SANDSTONE, (new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new ItemMultiTexture.Mapper() {
         public String apply(ItemStack p_apply_1_) {
            return BlockRedSandstone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
         }
      })).setUnlocalizedName("redSandStone"));
      registerItemBlock(Blocks.RED_SANDSTONE_STAIRS);
      registerItemBlock(Blocks.STONE_SLAB2, (new ItemSlab(Blocks.STONE_SLAB2, Blocks.STONE_SLAB2, Blocks.DOUBLE_STONE_SLAB2)).setUnlocalizedName("stoneSlab2"));
      registerItemBlock(Blocks.REPEATING_COMMAND_BLOCK);
      registerItemBlock(Blocks.CHAIN_COMMAND_BLOCK);
      registerItemBlock(Blocks.MAGMA);
      registerItemBlock(Blocks.NETHER_WART_BLOCK);
      registerItemBlock(Blocks.RED_NETHER_BRICK);
      registerItemBlock(Blocks.BONE_BLOCK);
      registerItemBlock(Blocks.STRUCTURE_VOID);
      registerItemBlock(Blocks.field_190976_dk);
      registerItemBlock(Blocks.field_190977_dl, new ItemShulkerBox(Blocks.field_190977_dl));
      registerItemBlock(Blocks.field_190978_dm, new ItemShulkerBox(Blocks.field_190978_dm));
      registerItemBlock(Blocks.field_190979_dn, new ItemShulkerBox(Blocks.field_190979_dn));
      registerItemBlock(Blocks.field_190980_do, new ItemShulkerBox(Blocks.field_190980_do));
      registerItemBlock(Blocks.field_190981_dp, new ItemShulkerBox(Blocks.field_190981_dp));
      registerItemBlock(Blocks.field_190982_dq, new ItemShulkerBox(Blocks.field_190982_dq));
      registerItemBlock(Blocks.field_190983_dr, new ItemShulkerBox(Blocks.field_190983_dr));
      registerItemBlock(Blocks.field_190984_ds, new ItemShulkerBox(Blocks.field_190984_ds));
      registerItemBlock(Blocks.field_190985_dt, new ItemShulkerBox(Blocks.field_190985_dt));
      registerItemBlock(Blocks.field_190986_du, new ItemShulkerBox(Blocks.field_190986_du));
      registerItemBlock(Blocks.field_190987_dv, new ItemShulkerBox(Blocks.field_190987_dv));
      registerItemBlock(Blocks.field_190988_dw, new ItemShulkerBox(Blocks.field_190988_dw));
      registerItemBlock(Blocks.field_190989_dx, new ItemShulkerBox(Blocks.field_190989_dx));
      registerItemBlock(Blocks.field_190990_dy, new ItemShulkerBox(Blocks.field_190990_dy));
      registerItemBlock(Blocks.field_190991_dz, new ItemShulkerBox(Blocks.field_190991_dz));
      registerItemBlock(Blocks.field_190975_dA, new ItemShulkerBox(Blocks.field_190975_dA));
      registerItemBlock(Blocks.field_192427_dB);
      registerItemBlock(Blocks.field_192428_dC);
      registerItemBlock(Blocks.field_192429_dD);
      registerItemBlock(Blocks.field_192430_dE);
      registerItemBlock(Blocks.field_192431_dF);
      registerItemBlock(Blocks.field_192432_dG);
      registerItemBlock(Blocks.field_192433_dH);
      registerItemBlock(Blocks.field_192434_dI);
      registerItemBlock(Blocks.field_192435_dJ);
      registerItemBlock(Blocks.field_192436_dK);
      registerItemBlock(Blocks.field_192437_dL);
      registerItemBlock(Blocks.field_192438_dM);
      registerItemBlock(Blocks.field_192439_dN);
      registerItemBlock(Blocks.field_192440_dO);
      registerItemBlock(Blocks.field_192441_dP);
      registerItemBlock(Blocks.field_192442_dQ);
      registerItemBlock(Blocks.field_192443_dR, (new ItemCloth(Blocks.field_192443_dR)).setUnlocalizedName("concrete"));
      registerItemBlock(Blocks.field_192444_dS, (new ItemCloth(Blocks.field_192444_dS)).setUnlocalizedName("concrete_powder"));
      registerItemBlock(Blocks.STRUCTURE_BLOCK);
      registerItem(256, (String)"iron_shovel", (new ItemSpade(Item.ToolMaterial.IRON)).setUnlocalizedName("shovelIron"));
      registerItem(257, (String)"iron_pickaxe", (new ItemPickaxe(Item.ToolMaterial.IRON)).setUnlocalizedName("pickaxeIron"));
      registerItem(258, (String)"iron_axe", (new ItemAxe(Item.ToolMaterial.IRON)).setUnlocalizedName("hatchetIron"));
      registerItem(259, (String)"flint_and_steel", (new ItemFlintAndSteel()).setUnlocalizedName("flintAndSteel"));
      registerItem(260, (String)"apple", (new ItemFood(4, 0.3F, false)).setUnlocalizedName("apple"));
      registerItem(261, (String)"bow", (new ItemBow()).setUnlocalizedName("bow"));
      registerItem(262, (String)"arrow", (new ItemArrow()).setUnlocalizedName("arrow"));
      registerItem(263, (String)"coal", (new ItemCoal()).setUnlocalizedName("coal"));
      registerItem(264, (String)"diamond", (new Item()).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(265, (String)"iron_ingot", (new Item()).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(266, (String)"gold_ingot", (new Item()).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(267, (String)"iron_sword", (new ItemSword(Item.ToolMaterial.IRON)).setUnlocalizedName("swordIron"));
      registerItem(268, (String)"wooden_sword", (new ItemSword(Item.ToolMaterial.WOOD)).setUnlocalizedName("swordWood"));
      registerItem(269, (String)"wooden_shovel", (new ItemSpade(Item.ToolMaterial.WOOD)).setUnlocalizedName("shovelWood"));
      registerItem(270, (String)"wooden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood"));
      registerItem(271, (String)"wooden_axe", (new ItemAxe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hatchetWood"));
      registerItem(272, (String)"stone_sword", (new ItemSword(Item.ToolMaterial.STONE)).setUnlocalizedName("swordStone"));
      registerItem(273, (String)"stone_shovel", (new ItemSpade(Item.ToolMaterial.STONE)).setUnlocalizedName("shovelStone"));
      registerItem(274, (String)"stone_pickaxe", (new ItemPickaxe(Item.ToolMaterial.STONE)).setUnlocalizedName("pickaxeStone"));
      registerItem(275, (String)"stone_axe", (new ItemAxe(Item.ToolMaterial.STONE)).setUnlocalizedName("hatchetStone"));
      registerItem(276, (String)"diamond_sword", (new ItemSword(Item.ToolMaterial.DIAMOND)).setUnlocalizedName("swordDiamond"));
      registerItem(277, (String)"diamond_shovel", (new ItemSpade(Item.ToolMaterial.DIAMOND)).setUnlocalizedName("shovelDiamond"));
      registerItem(278, (String)"diamond_pickaxe", (new ItemPickaxe(Item.ToolMaterial.DIAMOND)).setUnlocalizedName("pickaxeDiamond"));
      registerItem(279, (String)"diamond_axe", (new ItemAxe(Item.ToolMaterial.DIAMOND)).setUnlocalizedName("hatchetDiamond"));
      registerItem(280, (String)"stick", (new Item()).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(281, (String)"bowl", (new Item()).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(282, (String)"mushroom_stew", (new ItemSoup(6)).setUnlocalizedName("mushroomStew"));
      registerItem(283, (String)"golden_sword", (new ItemSword(Item.ToolMaterial.GOLD)).setUnlocalizedName("swordGold"));
      registerItem(284, (String)"golden_shovel", (new ItemSpade(Item.ToolMaterial.GOLD)).setUnlocalizedName("shovelGold"));
      registerItem(285, (String)"golden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold"));
      registerItem(286, (String)"golden_axe", (new ItemAxe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hatchetGold"));
      registerItem(287, (String)"string", (new ItemBlockSpecial(Blocks.TRIPWIRE)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(288, (String)"feather", (new Item()).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(289, (String)"gunpowder", (new Item()).setUnlocalizedName("sulphur").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(290, (String)"wooden_hoe", (new ItemHoe(Item.ToolMaterial.WOOD)).setUnlocalizedName("hoeWood"));
      registerItem(291, (String)"stone_hoe", (new ItemHoe(Item.ToolMaterial.STONE)).setUnlocalizedName("hoeStone"));
      registerItem(292, (String)"iron_hoe", (new ItemHoe(Item.ToolMaterial.IRON)).setUnlocalizedName("hoeIron"));
      registerItem(293, (String)"diamond_hoe", (new ItemHoe(Item.ToolMaterial.DIAMOND)).setUnlocalizedName("hoeDiamond"));
      registerItem(294, (String)"golden_hoe", (new ItemHoe(Item.ToolMaterial.GOLD)).setUnlocalizedName("hoeGold"));
      registerItem(295, (String)"wheat_seeds", (new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND)).setUnlocalizedName("seeds"));
      registerItem(296, (String)"wheat", (new Item()).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(297, (String)"bread", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("bread"));
      registerItem(298, (String)"leather_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD)).setUnlocalizedName("helmetCloth"));
      registerItem(299, (String)"leather_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST)).setUnlocalizedName("chestplateCloth"));
      registerItem(300, (String)"leather_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.LEGS)).setUnlocalizedName("leggingsCloth"));
      registerItem(301, (String)"leather_boots", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET)).setUnlocalizedName("bootsCloth"));
      registerItem(302, (String)"chainmail_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.HEAD)).setUnlocalizedName("helmetChain"));
      registerItem(303, (String)"chainmail_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.CHEST)).setUnlocalizedName("chestplateChain"));
      registerItem(304, (String)"chainmail_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.LEGS)).setUnlocalizedName("leggingsChain"));
      registerItem(305, (String)"chainmail_boots", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.FEET)).setUnlocalizedName("bootsChain"));
      registerItem(306, (String)"iron_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD)).setUnlocalizedName("helmetIron"));
      registerItem(307, (String)"iron_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.CHEST)).setUnlocalizedName("chestplateIron"));
      registerItem(308, (String)"iron_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.LEGS)).setUnlocalizedName("leggingsIron"));
      registerItem(309, (String)"iron_boots", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.FEET)).setUnlocalizedName("bootsIron"));
      registerItem(310, (String)"diamond_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD)).setUnlocalizedName("helmetDiamond"));
      registerItem(311, (String)"diamond_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.CHEST)).setUnlocalizedName("chestplateDiamond"));
      registerItem(312, (String)"diamond_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.LEGS)).setUnlocalizedName("leggingsDiamond"));
      registerItem(313, (String)"diamond_boots", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.FEET)).setUnlocalizedName("bootsDiamond"));
      registerItem(314, (String)"golden_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.HEAD)).setUnlocalizedName("helmetGold"));
      registerItem(315, (String)"golden_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.CHEST)).setUnlocalizedName("chestplateGold"));
      registerItem(316, (String)"golden_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.LEGS)).setUnlocalizedName("leggingsGold"));
      registerItem(317, (String)"golden_boots", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.FEET)).setUnlocalizedName("bootsGold"));
      registerItem(318, (String)"flint", (new Item()).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(319, (String)"porkchop", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("porkchopRaw"));
      registerItem(320, (String)"cooked_porkchop", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("porkchopCooked"));
      registerItem(321, (String)"painting", (new ItemHangingEntity(EntityPainting.class)).setUnlocalizedName("painting"));
      registerItem(322, (String)"golden_apple", (new ItemAppleGold(4, 1.2F, false)).setAlwaysEdible().setUnlocalizedName("appleGold"));
      registerItem(323, (String)"sign", (new ItemSign()).setUnlocalizedName("sign"));
      registerItem(324, (String)"wooden_door", (new ItemDoor(Blocks.OAK_DOOR)).setUnlocalizedName("doorOak"));
      Item item = (new ItemBucket(Blocks.AIR)).setUnlocalizedName("bucket").setMaxStackSize(16);
      registerItem(325, (String)"bucket", item);
      registerItem(326, (String)"water_bucket", (new ItemBucket(Blocks.FLOWING_WATER)).setUnlocalizedName("bucketWater").setContainerItem(item));
      registerItem(327, (String)"lava_bucket", (new ItemBucket(Blocks.FLOWING_LAVA)).setUnlocalizedName("bucketLava").setContainerItem(item));
      registerItem(328, (String)"minecart", (new ItemMinecart(EntityMinecart.Type.RIDEABLE)).setUnlocalizedName("minecart"));
      registerItem(329, (String)"saddle", (new ItemSaddle()).setUnlocalizedName("saddle"));
      registerItem(330, (String)"iron_door", (new ItemDoor(Blocks.IRON_DOOR)).setUnlocalizedName("doorIron"));
      registerItem(331, (String)"redstone", (new ItemRedstone()).setUnlocalizedName("redstone"));
      registerItem(332, (String)"snowball", (new ItemSnowball()).setUnlocalizedName("snowball"));
      registerItem(333, (String)"boat", new ItemBoat(EntityBoat.Type.OAK));
      registerItem(334, (String)"leather", (new Item()).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(335, (String)"milk_bucket", (new ItemBucketMilk()).setUnlocalizedName("milk").setContainerItem(item));
      registerItem(336, (String)"brick", (new Item()).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(337, (String)"clay_ball", (new Item()).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(338, (String)"reeds", (new ItemBlockSpecial(Blocks.REEDS)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(339, (String)"paper", (new Item()).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.MISC));
      registerItem(340, (String)"book", (new ItemBook()).setUnlocalizedName("book").setCreativeTab(CreativeTabs.MISC));
      registerItem(341, (String)"slime_ball", (new Item()).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.MISC));
      registerItem(342, (String)"chest_minecart", (new ItemMinecart(EntityMinecart.Type.CHEST)).setUnlocalizedName("minecartChest"));
      registerItem(343, (String)"furnace_minecart", (new ItemMinecart(EntityMinecart.Type.FURNACE)).setUnlocalizedName("minecartFurnace"));
      registerItem(344, (String)"egg", (new ItemEgg()).setUnlocalizedName("egg"));
      registerItem(345, (String)"compass", (new ItemCompass()).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.TOOLS));
      registerItem(346, (String)"fishing_rod", (new ItemFishingRod()).setUnlocalizedName("fishingRod"));
      registerItem(347, (String)"clock", (new ItemClock()).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.TOOLS));
      registerItem(348, (String)"glowstone_dust", (new Item()).setUnlocalizedName("yellowDust").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(349, (String)"fish", (new ItemFishFood(false)).setUnlocalizedName("fish").setHasSubtypes(true));
      registerItem(350, (String)"cooked_fish", (new ItemFishFood(true)).setUnlocalizedName("fish").setHasSubtypes(true));
      registerItem(351, (String)"dye", (new ItemDye()).setUnlocalizedName("dyePowder"));
      registerItem(352, (String)"bone", (new Item()).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.MISC));
      registerItem(353, (String)"sugar", (new Item()).setUnlocalizedName("sugar").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(354, (String)"cake", (new ItemBlockSpecial(Blocks.CAKE)).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.FOOD));
      registerItem(355, (String)"bed", (new ItemBed()).setMaxStackSize(1).setUnlocalizedName("bed"));
      registerItem(356, (String)"repeater", (new ItemBlockSpecial(Blocks.UNPOWERED_REPEATER)).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.REDSTONE));
      registerItem(357, (String)"cookie", (new ItemFood(2, 0.1F, false)).setUnlocalizedName("cookie"));
      registerItem(358, (String)"filled_map", (new ItemMap()).setUnlocalizedName("map"));
      registerItem(359, (String)"shears", (new ItemShears()).setUnlocalizedName("shears"));
      registerItem(360, (String)"melon", (new ItemFood(2, 0.3F, false)).setUnlocalizedName("melon"));
      registerItem(361, (String)"pumpkin_seeds", (new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND)).setUnlocalizedName("seeds_pumpkin"));
      registerItem(362, (String)"melon_seeds", (new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND)).setUnlocalizedName("seeds_melon"));
      registerItem(363, (String)"beef", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("beefRaw"));
      registerItem(364, (String)"cooked_beef", (new ItemFood(8, 0.8F, true)).setUnlocalizedName("beefCooked"));
      registerItem(365, (String)"chicken", (new ItemFood(2, 0.3F, true)).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setUnlocalizedName("chickenRaw"));
      registerItem(366, (String)"cooked_chicken", (new ItemFood(6, 0.6F, true)).setUnlocalizedName("chickenCooked"));
      registerItem(367, (String)"rotten_flesh", (new ItemFood(4, 0.1F, true)).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.8F).setUnlocalizedName("rottenFlesh"));
      registerItem(368, (String)"ender_pearl", (new ItemEnderPearl()).setUnlocalizedName("enderPearl"));
      registerItem(369, (String)"blaze_rod", (new Item()).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.MATERIALS).setFull3D());
      registerItem(370, (String)"ghast_tear", (new Item()).setUnlocalizedName("ghastTear").setCreativeTab(CreativeTabs.BREWING));
      registerItem(371, (String)"gold_nugget", (new Item()).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(372, (String)"nether_wart", (new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND)).setUnlocalizedName("netherStalkSeeds"));
      registerItem(373, (String)"potion", (new ItemPotion()).setUnlocalizedName("potion"));
      Item item1 = (new ItemGlassBottle()).setUnlocalizedName("glassBottle");
      registerItem(374, (String)"glass_bottle", item1);
      registerItem(375, (String)"spider_eye", (new ItemFood(2, 0.8F, false)).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F).setUnlocalizedName("spiderEye"));
      registerItem(376, (String)"fermented_spider_eye", (new Item()).setUnlocalizedName("fermentedSpiderEye").setCreativeTab(CreativeTabs.BREWING));
      registerItem(377, (String)"blaze_powder", (new Item()).setUnlocalizedName("blazePowder").setCreativeTab(CreativeTabs.BREWING));
      registerItem(378, (String)"magma_cream", (new Item()).setUnlocalizedName("magmaCream").setCreativeTab(CreativeTabs.BREWING));
      registerItem(379, (String)"brewing_stand", (new ItemBlockSpecial(Blocks.BREWING_STAND)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.BREWING));
      registerItem(380, (String)"cauldron", (new ItemBlockSpecial(Blocks.CAULDRON)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.BREWING));
      registerItem(381, (String)"ender_eye", (new ItemEnderEye()).setUnlocalizedName("eyeOfEnder"));
      registerItem(382, (String)"speckled_melon", (new Item()).setUnlocalizedName("speckledMelon").setCreativeTab(CreativeTabs.BREWING));
      registerItem(383, (String)"spawn_egg", (new ItemMonsterPlacer()).setUnlocalizedName("monsterPlacer"));
      registerItem(384, (String)"experience_bottle", (new ItemExpBottle()).setUnlocalizedName("expBottle"));
      registerItem(385, (String)"fire_charge", (new ItemFireball()).setUnlocalizedName("fireball"));
      registerItem(386, (String)"writable_book", (new ItemWritableBook()).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.MISC));
      registerItem(387, (String)"written_book", (new ItemWrittenBook()).setUnlocalizedName("writtenBook").setMaxStackSize(16));
      registerItem(388, (String)"emerald", (new Item()).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(389, (String)"item_frame", (new ItemHangingEntity(EntityItemFrame.class)).setUnlocalizedName("frame"));
      registerItem(390, (String)"flower_pot", (new ItemBlockSpecial(Blocks.FLOWER_POT)).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.DECORATIONS));
      registerItem(391, (String)"carrot", (new ItemSeedFood(3, 0.6F, Blocks.CARROTS, Blocks.FARMLAND)).setUnlocalizedName("carrots"));
      registerItem(392, (String)"potato", (new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.FARMLAND)).setUnlocalizedName("potato"));
      registerItem(393, (String)"baked_potato", (new ItemFood(5, 0.6F, false)).setUnlocalizedName("potatoBaked"));
      registerItem(394, (String)"poisonous_potato", (new ItemFood(2, 0.3F, false)).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 0.6F).setUnlocalizedName("potatoPoisonous"));
      registerItem(395, (String)"map", (new ItemEmptyMap()).setUnlocalizedName("emptyMap"));
      registerItem(396, (String)"golden_carrot", (new ItemFood(6, 1.2F, false)).setUnlocalizedName("carrotGolden").setCreativeTab(CreativeTabs.BREWING));
      registerItem(397, (String)"skull", (new ItemSkull()).setUnlocalizedName("skull"));
      registerItem(398, (String)"carrot_on_a_stick", (new ItemCarrotOnAStick()).setUnlocalizedName("carrotOnAStick"));
      registerItem(399, (String)"nether_star", (new ItemSimpleFoiled()).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(400, (String)"pumpkin_pie", (new ItemFood(8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.FOOD));
      registerItem(401, (String)"fireworks", (new ItemFirework()).setUnlocalizedName("fireworks"));
      registerItem(402, (String)"firework_charge", (new ItemFireworkCharge()).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.MISC));
      registerItem(403, (String)"enchanted_book", (new ItemEnchantedBook()).setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
      registerItem(404, (String)"comparator", (new ItemBlockSpecial(Blocks.UNPOWERED_COMPARATOR)).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.REDSTONE));
      registerItem(405, (String)"netherbrick", (new Item()).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(406, (String)"quartz", (new Item()).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(407, (String)"tnt_minecart", (new ItemMinecart(EntityMinecart.Type.TNT)).setUnlocalizedName("minecartTnt"));
      registerItem(408, (String)"hopper_minecart", (new ItemMinecart(EntityMinecart.Type.HOPPER)).setUnlocalizedName("minecartHopper"));
      registerItem(409, (String)"prismarine_shard", (new Item()).setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(410, (String)"prismarine_crystals", (new Item()).setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(411, (String)"rabbit", (new ItemFood(3, 0.3F, true)).setUnlocalizedName("rabbitRaw"));
      registerItem(412, (String)"cooked_rabbit", (new ItemFood(5, 0.6F, true)).setUnlocalizedName("rabbitCooked"));
      registerItem(413, (String)"rabbit_stew", (new ItemSoup(10)).setUnlocalizedName("rabbitStew"));
      registerItem(414, (String)"rabbit_foot", (new Item()).setUnlocalizedName("rabbitFoot").setCreativeTab(CreativeTabs.BREWING));
      registerItem(415, (String)"rabbit_hide", (new Item()).setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(416, (String)"armor_stand", (new ItemArmorStand()).setUnlocalizedName("armorStand").setMaxStackSize(16));
      registerItem(417, (String)"iron_horse_armor", (new Item()).setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
      registerItem(418, (String)"golden_horse_armor", (new Item()).setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
      registerItem(419, (String)"diamond_horse_armor", (new Item()).setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
      registerItem(420, (String)"lead", (new ItemLead()).setUnlocalizedName("leash"));
      registerItem(421, (String)"name_tag", (new ItemNameTag()).setUnlocalizedName("nameTag"));
      registerItem(422, (String)"command_block_minecart", (new ItemMinecart(EntityMinecart.Type.COMMAND_BLOCK)).setUnlocalizedName("minecartCommandBlock").setCreativeTab((CreativeTabs)null));
      registerItem(423, (String)"mutton", (new ItemFood(2, 0.3F, true)).setUnlocalizedName("muttonRaw"));
      registerItem(424, (String)"cooked_mutton", (new ItemFood(6, 0.8F, true)).setUnlocalizedName("muttonCooked"));
      registerItem(425, (String)"banner", (new ItemBanner()).setUnlocalizedName("banner"));
      registerItem(426, (String)"end_crystal", new ItemEndCrystal());
      registerItem(427, (String)"spruce_door", (new ItemDoor(Blocks.SPRUCE_DOOR)).setUnlocalizedName("doorSpruce"));
      registerItem(428, (String)"birch_door", (new ItemDoor(Blocks.BIRCH_DOOR)).setUnlocalizedName("doorBirch"));
      registerItem(429, (String)"jungle_door", (new ItemDoor(Blocks.JUNGLE_DOOR)).setUnlocalizedName("doorJungle"));
      registerItem(430, (String)"acacia_door", (new ItemDoor(Blocks.ACACIA_DOOR)).setUnlocalizedName("doorAcacia"));
      registerItem(431, (String)"dark_oak_door", (new ItemDoor(Blocks.DARK_OAK_DOOR)).setUnlocalizedName("doorDarkOak"));
      registerItem(432, (String)"chorus_fruit", (new ItemChorusFruit(4, 0.3F)).setAlwaysEdible().setUnlocalizedName("chorusFruit").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(433, (String)"chorus_fruit_popped", (new Item()).setUnlocalizedName("chorusFruitPopped").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(434, (String)"beetroot", (new ItemFood(1, 0.6F, false)).setUnlocalizedName("beetroot"));
      registerItem(435, (String)"beetroot_seeds", (new ItemSeeds(Blocks.BEETROOTS, Blocks.FARMLAND)).setUnlocalizedName("beetroot_seeds"));
      registerItem(436, (String)"beetroot_soup", (new ItemSoup(6)).setUnlocalizedName("beetroot_soup"));
      registerItem(437, (String)"dragon_breath", (new Item()).setCreativeTab(CreativeTabs.BREWING).setUnlocalizedName("dragon_breath").setContainerItem(item1));
      registerItem(438, (String)"splash_potion", (new ItemSplashPotion()).setUnlocalizedName("splash_potion"));
      registerItem(439, (String)"spectral_arrow", (new ItemSpectralArrow()).setUnlocalizedName("spectral_arrow"));
      registerItem(440, (String)"tipped_arrow", (new ItemTippedArrow()).setUnlocalizedName("tipped_arrow"));
      registerItem(441, (String)"lingering_potion", (new ItemLingeringPotion()).setUnlocalizedName("lingering_potion"));
      registerItem(442, (String)"shield", (new ItemShield()).setUnlocalizedName("shield"));
      registerItem(443, (String)"elytra", (new ItemElytra()).setUnlocalizedName("elytra"));
      registerItem(444, (String)"spruce_boat", new ItemBoat(EntityBoat.Type.SPRUCE));
      registerItem(445, (String)"birch_boat", new ItemBoat(EntityBoat.Type.BIRCH));
      registerItem(446, (String)"jungle_boat", new ItemBoat(EntityBoat.Type.JUNGLE));
      registerItem(447, (String)"acacia_boat", new ItemBoat(EntityBoat.Type.ACACIA));
      registerItem(448, (String)"dark_oak_boat", new ItemBoat(EntityBoat.Type.DARK_OAK));
      registerItem(449, (String)"totem_of_undying", (new Item()).setUnlocalizedName("totem").setMaxStackSize(1).setCreativeTab(CreativeTabs.COMBAT));
      registerItem(450, (String)"shulker_shell", (new Item()).setUnlocalizedName("shulkerShell").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(452, (String)"iron_nugget", (new Item()).setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.MATERIALS));
      registerItem(453, (String)"knowledge_book", (new ItemKnowledgeBook()).setUnlocalizedName("knowledgeBook"));
      registerItem(2256, (String)"record_13", (new ItemRecord("13", SoundEvents.RECORD_13)).setUnlocalizedName("record"));
      registerItem(2257, (String)"record_cat", (new ItemRecord("cat", SoundEvents.RECORD_CAT)).setUnlocalizedName("record"));
      registerItem(2258, (String)"record_blocks", (new ItemRecord("blocks", SoundEvents.RECORD_BLOCKS)).setUnlocalizedName("record"));
      registerItem(2259, (String)"record_chirp", (new ItemRecord("chirp", SoundEvents.RECORD_CHIRP)).setUnlocalizedName("record"));
      registerItem(2260, (String)"record_far", (new ItemRecord("far", SoundEvents.RECORD_FAR)).setUnlocalizedName("record"));
      registerItem(2261, (String)"record_mall", (new ItemRecord("mall", SoundEvents.RECORD_MALL)).setUnlocalizedName("record"));
      registerItem(2262, (String)"record_mellohi", (new ItemRecord("mellohi", SoundEvents.RECORD_MELLOHI)).setUnlocalizedName("record"));
      registerItem(2263, (String)"record_stal", (new ItemRecord("stal", SoundEvents.RECORD_STAL)).setUnlocalizedName("record"));
      registerItem(2264, (String)"record_strad", (new ItemRecord("strad", SoundEvents.RECORD_STRAD)).setUnlocalizedName("record"));
      registerItem(2265, (String)"record_ward", (new ItemRecord("ward", SoundEvents.RECORD_WARD)).setUnlocalizedName("record"));
      registerItem(2266, (String)"record_11", (new ItemRecord("11", SoundEvents.RECORD_11)).setUnlocalizedName("record"));
      registerItem(2267, (String)"record_wait", (new ItemRecord("wait", SoundEvents.RECORD_WAIT)).setUnlocalizedName("record"));
   }

   private static void registerItemBlock(Block blockIn) {
      registerItemBlock(blockIn, new ItemBlock(blockIn));
   }

   protected static void registerItemBlock(Block blockIn, Item itemIn) {
      registerItem(Block.getIdFromBlock(blockIn), (ResourceLocation)Block.REGISTRY.getNameForObject(blockIn), itemIn);
      BLOCK_TO_ITEM.put(blockIn, itemIn);
   }

   private static void registerItem(int id, String textualID, Item itemIn) {
      registerItem(id, new ResourceLocation(textualID), itemIn);
   }

   private static void registerItem(int id, ResourceLocation textualID, Item itemIn) {
      REGISTRY.register(id, textualID, itemIn);
   }

   public ItemStack func_190903_i() {
      return new ItemStack(this);
   }

   public static enum ToolMaterial {
      WOOD(0, 59, 2.0F, 0.0F, 15),
      STONE(1, 131, 4.0F, 1.0F, 5),
      IRON(2, 250, 6.0F, 2.0F, 14),
      DIAMOND(3, 1561, 8.0F, 3.0F, 10),
      GOLD(0, 32, 12.0F, 0.0F, 22);

      private final int harvestLevel;
      private final int maxUses;
      private final float efficiencyOnProperMaterial;
      private final float damageVsEntity;
      private final int enchantability;

      private ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability) {
         this.harvestLevel = harvestLevel;
         this.maxUses = maxUses;
         this.efficiencyOnProperMaterial = efficiency;
         this.damageVsEntity = damageVsEntity;
         this.enchantability = enchantability;
      }

      public int getMaxUses() {
         return this.maxUses;
      }

      public float getEfficiencyOnProperMaterial() {
         return this.efficiencyOnProperMaterial;
      }

      public float getDamageVsEntity() {
         return this.damageVsEntity;
      }

      public int getHarvestLevel() {
         return this.harvestLevel;
      }

      public int getEnchantability() {
         return this.enchantability;
      }

      public Item getRepairItem() {
         if (this == WOOD) {
            return Item.getItemFromBlock(Blocks.PLANKS);
         } else if (this == STONE) {
            return Item.getItemFromBlock(Blocks.COBBLESTONE);
         } else if (this == GOLD) {
            return Items.GOLD_INGOT;
         } else if (this == IRON) {
            return Items.IRON_INGOT;
         } else {
            return this == DIAMOND ? Items.DIAMOND : null;
         }
      }
   }
}
