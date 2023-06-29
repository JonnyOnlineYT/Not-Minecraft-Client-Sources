package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHorse extends AbstractHorse {
   private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
   private static final DataParameter<Integer> HORSE_VARIANT;
   private static final DataParameter<Integer> HORSE_ARMOR;
   private static final String[] HORSE_TEXTURES;
   private static final String[] HORSE_TEXTURES_ABBR;
   private static final String[] HORSE_MARKING_TEXTURES;
   private static final String[] HORSE_MARKING_TEXTURES_ABBR;
   private String texturePrefix;
   private final String[] horseTexturesArray = new String[3];

   public EntityHorse(World worldIn) {
      super(worldIn);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(HORSE_VARIANT, 0);
      this.dataManager.register(HORSE_ARMOR, HorseArmorType.NONE.getOrdinal());
   }

   public static void registerFixesHorse(DataFixer fixer) {
      AbstractHorse.func_190683_c(fixer, EntityHorse.class);
      fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityHorse.class, new String[]{"ArmorItem"}));
   }

   public void writeEntityToNBT(NBTTagCompound compound) {
      super.writeEntityToNBT(compound);
      compound.setInteger("Variant", this.getHorseVariant());
      if (!this.horseChest.getStackInSlot(1).func_190926_b()) {
         compound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
      }

   }

   public void readEntityFromNBT(NBTTagCompound compound) {
      super.readEntityFromNBT(compound);
      this.setHorseVariant(compound.getInteger("Variant"));
      if (compound.hasKey("ArmorItem", 10)) {
         ItemStack itemstack = new ItemStack(compound.getCompoundTag("ArmorItem"));
         if (!itemstack.func_190926_b() && HorseArmorType.isHorseArmor(itemstack.getItem())) {
            this.horseChest.setInventorySlotContents(1, itemstack);
         }
      }

      this.updateHorseSlots();
   }

   public void setHorseVariant(int variant) {
      this.dataManager.set(HORSE_VARIANT, variant);
      this.resetTexturePrefix();
   }

   public int getHorseVariant() {
      return (Integer)this.dataManager.get(HORSE_VARIANT);
   }

   private void resetTexturePrefix() {
      this.texturePrefix = null;
   }

   private void setHorseTexturePaths() {
      int i = this.getHorseVariant();
      int j = (i & 255) % 7;
      int k = ((i & '\uff00') >> 8) % 5;
      HorseArmorType horsearmortype = this.getHorseArmorType();
      this.horseTexturesArray[0] = HORSE_TEXTURES[j];
      this.horseTexturesArray[1] = HORSE_MARKING_TEXTURES[k];
      this.horseTexturesArray[2] = horsearmortype.getTextureName();
      this.texturePrefix = "horse/" + HORSE_TEXTURES_ABBR[j] + HORSE_MARKING_TEXTURES_ABBR[k] + horsearmortype.getHash();
   }

   public String getHorseTexture() {
      if (this.texturePrefix == null) {
         this.setHorseTexturePaths();
      }

      return this.texturePrefix;
   }

   public String[] getVariantTexturePaths() {
      if (this.texturePrefix == null) {
         this.setHorseTexturePaths();
      }

      return this.horseTexturesArray;
   }

   protected void updateHorseSlots() {
      super.updateHorseSlots();
      this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
   }

   public void setHorseArmorStack(ItemStack itemStackIn) {
      HorseArmorType horsearmortype = HorseArmorType.getByItemStack(itemStackIn);
      this.dataManager.set(HORSE_ARMOR, horsearmortype.getOrdinal());
      this.resetTexturePrefix();
      if (!this.world.isRemote) {
         this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
         int i = horsearmortype.getProtection();
         if (i != 0) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, 0)).setSaved(false));
         }
      }

   }

   public HorseArmorType getHorseArmorType() {
      return HorseArmorType.getByOrdinal((Integer)this.dataManager.get(HORSE_ARMOR));
   }

   public void onInventoryChanged(IInventory invBasic) {
      HorseArmorType horsearmortype = this.getHorseArmorType();
      super.onInventoryChanged(invBasic);
      HorseArmorType horsearmortype1 = this.getHorseArmorType();
      if (this.ticksExisted > 20 && horsearmortype != horsearmortype1 && horsearmortype1 != HorseArmorType.NONE) {
         this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
      }

   }

   protected void func_190680_a(SoundType p_190680_1_) {
      super.func_190680_a(p_190680_1_);
      if (this.rand.nextInt(10) == 0) {
         this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, p_190680_1_.getVolume() * 0.6F, p_190680_1_.getPitch());
      }

   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)this.getModifiedMaxHealth());
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
      this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote && this.dataManager.isDirty()) {
         this.dataManager.setClean();
         this.resetTexturePrefix();
      }

   }

   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.ENTITY_HORSE_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.ENTITY_HORSE_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      super.getHurtSound(p_184601_1_);
      return SoundEvents.ENTITY_HORSE_HURT;
   }

   protected SoundEvent getAngrySound() {
      super.getAngrySound();
      return SoundEvents.ENTITY_HORSE_ANGRY;
   }

   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_HORSE;
   }

   public boolean processInteract(EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.getHeldItem(hand);
      boolean flag = !itemstack.func_190926_b();
      if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
         return super.processInteract(player, hand);
      } else {
         if (!this.isChild()) {
            if (this.isTame() && player.isSneaking()) {
               this.openGUI(player);
               return true;
            }

            if (this.isBeingRidden()) {
               return super.processInteract(player, hand);
            }
         }

         if (flag) {
            if (this.func_190678_b(player, itemstack)) {
               if (!player.capabilities.isCreativeMode) {
                  itemstack.func_190918_g(1);
               }

               return true;
            }

            if (itemstack.interactWithEntity(player, this, hand)) {
               return true;
            }

            if (!this.isTame()) {
               this.func_190687_dF();
               return true;
            }

            boolean flag1 = HorseArmorType.getByItemStack(itemstack) != HorseArmorType.NONE;
            boolean flag2 = !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;
            if (flag1 || flag2) {
               this.openGUI(player);
               return true;
            }
         }

         if (this.isChild()) {
            return super.processInteract(player, hand);
         } else {
            this.mountTo(player);
            return true;
         }
      }
   }

   public boolean canMateWith(EntityAnimal otherAnimal) {
      if (otherAnimal == this) {
         return false;
      } else if (!(otherAnimal instanceof EntityDonkey) && !(otherAnimal instanceof EntityHorse)) {
         return false;
      } else {
         return this.canMate() && ((AbstractHorse)otherAnimal).canMate();
      }
   }

   public EntityAgeable createChild(EntityAgeable ageable) {
      Object abstracthorse;
      if (ageable instanceof EntityDonkey) {
         abstracthorse = new EntityMule(this.world);
      } else {
         EntityHorse entityhorse = (EntityHorse)ageable;
         abstracthorse = new EntityHorse(this.world);
         int j = this.rand.nextInt(9);
         int i;
         if (j < 4) {
            i = this.getHorseVariant() & 255;
         } else if (j < 8) {
            i = entityhorse.getHorseVariant() & 255;
         } else {
            i = this.rand.nextInt(7);
         }

         int k = this.rand.nextInt(5);
         if (k < 2) {
            i |= this.getHorseVariant() & '\uff00';
         } else if (k < 4) {
            i |= entityhorse.getHorseVariant() & '\uff00';
         } else {
            i |= this.rand.nextInt(5) << 8 & '\uff00';
         }

         ((EntityHorse)abstracthorse).setHorseVariant(i);
      }

      this.func_190681_a(ageable, (AbstractHorse)abstracthorse);
      return (EntityAgeable)abstracthorse;
   }

   public boolean func_190677_dK() {
      return true;
   }

   public boolean func_190682_f(ItemStack p_190682_1_) {
      return HorseArmorType.isHorseArmor(p_190682_1_.getItem());
   }

   @Nullable
   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
      IEntityLivingData livingdata = super.onInitialSpawn(difficulty, livingdata);
      int i;
      if (livingdata instanceof EntityHorse.GroupData) {
         i = ((EntityHorse.GroupData)livingdata).field_190885_a;
      } else {
         i = this.rand.nextInt(7);
         livingdata = new EntityHorse.GroupData(i);
      }

      this.setHorseVariant(i | this.rand.nextInt(5) << 8);
      return (IEntityLivingData)livingdata;
   }

   static {
      HORSE_VARIANT = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
      HORSE_ARMOR = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
      HORSE_TEXTURES = new String[]{"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
      HORSE_TEXTURES_ABBR = new String[]{"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
      HORSE_MARKING_TEXTURES = new String[]{null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
      HORSE_MARKING_TEXTURES_ABBR = new String[]{"", "wo_", "wmo", "wdo", "bdo"};
   }

   public static class GroupData implements IEntityLivingData {
      public int field_190885_a;

      public GroupData(int p_i47337_1_) {
         this.field_190885_a = p_i47337_1_;
      }
   }
}
