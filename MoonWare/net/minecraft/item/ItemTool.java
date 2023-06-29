package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemTool extends Item
{
    private final Set<Block> effectiveBlocks;
    protected float efficiencyOnProperMaterial;

    /** Damage versus entities. */
    protected float damageVsEntity;
    protected float attackSpeed;

    /** The material this tool is made from. */
    protected Item.ToolMaterial toolMaterial;

    protected ItemTool(float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn)
    {
        efficiencyOnProperMaterial = 4.0F;
        toolMaterial = materialIn;
        effectiveBlocks = effectiveBlocksIn;
        maxStackSize = 1;
        setMaxDamage(materialIn.getMaxUses());
        efficiencyOnProperMaterial = materialIn.getEfficiencyOnProperMaterial();
        damageVsEntity = attackDamageIn + materialIn.getDamageVsEntity();
        attackSpeed = attackSpeedIn;
        setCreativeTab(CreativeTabs.TOOLS);
    }

    protected ItemTool(Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn)
    {
        this(0.0F, 0.0F, materialIn, effectiveBlocksIn);
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        return effectiveBlocks.contains(state.getBlock()) ? efficiencyOnProperMaterial : 1.0F;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(2, attacker);
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(1, entityLiving);
        }

        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return toolMaterial.getEnchantability();
    }

    /**
     * Return the name for this tool's material.
     */
    public String getToolMaterialName()
    {
        return toolMaterial.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return toolMaterial.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Tool modifier", damageVsEntity, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Tool modifier", attackSpeed, 0));
        }

        return multimap;
    }

    public float getDamageVsEntity() {
        return damageVsEntity;
    }
}
