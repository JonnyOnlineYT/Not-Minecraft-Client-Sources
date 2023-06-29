package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFood extends Item {
   public final int itemUseDuration;
   private final int healAmount;
   private final float saturationModifier;
   private final boolean isWolfsFavoriteMeat;
   private boolean alwaysEdible;
   private PotionEffect potionId;
   private float potionEffectProbability;

   public ItemFood(int amount, float saturation, boolean isWolfFood) {
      this.itemUseDuration = 32;
      this.healAmount = amount;
      this.isWolfsFavoriteMeat = isWolfFood;
      this.saturationModifier = saturation;
      this.setCreativeTab(CreativeTabs.FOOD);
   }

   public ItemFood(int amount, boolean isWolfFood) {
      this(amount, 0.6F, isWolfFood);
   }

   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
      if (entityLiving instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)entityLiving;
         entityplayer.getFoodStats().addStats(this, stack);
         worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
         this.onFoodEaten(stack, worldIn, entityplayer);
         entityplayer.addStat(StatList.getObjectUseStats(this));
         if (entityplayer instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193138_y.func_193148_a((EntityPlayerMP)entityplayer, stack);
         }
      }

      stack.func_190918_g(1);
      return stack;
   }

   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
      if (!worldIn.isRemote && this.potionId != null && worldIn.rand.nextFloat() < this.potionEffectProbability) {
         player.addPotionEffect(new PotionEffect(this.potionId));
      }

   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 32;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.EAT;
   }

   public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
      ItemStack itemstack = worldIn.getHeldItem(playerIn);
      if (worldIn.canEat(this.alwaysEdible)) {
         worldIn.setActiveHand(playerIn);
         return new ActionResult(EnumActionResult.SUCCESS, itemstack);
      } else {
         return new ActionResult(EnumActionResult.FAIL, itemstack);
      }
   }

   public int getHealAmount(ItemStack stack) {
      return this.healAmount;
   }

   public float getSaturationModifier(ItemStack stack) {
      return this.saturationModifier;
   }

   public boolean isWolfsFavoriteMeat() {
      return this.isWolfsFavoriteMeat;
   }

   public ItemFood setPotionEffect(PotionEffect p_185070_1_, float p_185070_2_) {
      this.potionId = p_185070_1_;
      this.potionEffectProbability = p_185070_2_;
      return this;
   }

   public ItemFood setAlwaysEdible() {
      this.alwaysEdible = true;
      return this;
   }
}
