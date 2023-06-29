package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {
   public final float attackDamage;
   private final Item.ToolMaterial material;

   public ItemSword(Item.ToolMaterial material) {
      this.material = material;
      this.maxStackSize = 1;
      this.setMaxDamage(material.getMaxUses());
      this.setCreativeTab(CreativeTabs.COMBAT);
      this.attackDamage = 3.0F + material.getDamageVsEntity();
   }

   public float getDamageVsEntity() {
      return this.material.getDamageVsEntity();
   }

   public float getStrVsBlock(ItemStack stack, IBlockState state) {
      Block block = state.getBlock();
      if (block == Blocks.WEB) {
         return 15.0F;
      } else {
         Material material = state.getMaterial();
         return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
      }
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
      stack.damageItem(1, attacker);
      return true;
   }

   public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
      if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
         stack.damageItem(2, entityLiving);
      }

      return true;
   }

   public boolean isFull3D() {
      return true;
   }

   public boolean canHarvestBlock(IBlockState blockIn) {
      return blockIn.getBlock() == Blocks.WEB;
   }

   public int getItemEnchantability() {
      return this.material.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.material.toString();
   }

   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
      return this.material.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
   }

   public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
      Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
      if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
         multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
         multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
      }

      return multimap;
   }
}
