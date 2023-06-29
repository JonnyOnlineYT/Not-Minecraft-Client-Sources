package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.translation.I18n;

public abstract class Enchantment
{
    public static final RegistryNamespaced<Namespaced, Enchantment> REGISTRY = new RegistryNamespaced<Namespaced, Enchantment>();

    /** Where this enchantment has an effect, e.g. offhand, pants */
    private final EntityEquipmentSlot[] applicableEquipmentTypes;
    private final Enchantment.Rarity rarity;
    @Nullable

    /** The EnumEnchantmentType given to this Enchantment. */
    public EnumEnchantmentType type;

    /** Used in localisation and stats. */
    protected String name;

    @Nullable

    /**
     * Gets an Enchantment from the registry, based on a numeric ID.
     */
    public static Enchantment getEnchantmentByID(int id)
    {
        return REGISTRY.getObjectById(id);
    }

    /**
     * Gets the numeric ID for the passed enchantment.
     */
    public static int getEnchantmentID(Enchantment enchantmentIn)
    {
        return REGISTRY.getIDForObject(enchantmentIn);
    }

    @Nullable

    /**
     * Retrieves an enchantment by using its location name.
     */
    public static Enchantment getEnchantmentByLocation(String location)
    {
        return REGISTRY.getObject(new Namespaced(location));
    }

    protected Enchantment(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots)
    {
        rarity = rarityIn;
        type = typeIn;
        applicableEquipmentTypes = slots;
    }

    public List<ItemStack> getEntityEquipment(EntityLivingBase entityIn)
    {
        List<ItemStack> list = Lists.newArrayList();

        for (EntityEquipmentSlot entityequipmentslot : applicableEquipmentTypes)
        {
            ItemStack itemstack = entityIn.getItemStackFromSlot(entityequipmentslot);

            if (!itemstack.isEmpty())
            {
                list.add(itemstack);
            }
        }

        return list;
    }

    /**
     * Retrieves the weight value of an Enchantment. This weight value is used within vanilla to determine how rare an
     * enchantment is.
     */
    public Enchantment.Rarity getRarity()
    {
        return rarity;
    }

    /**
     * Returns the minimum level that the enchantment can have.
     */
    public int getMinLevel()
    {
        return 1;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 1;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + enchantmentLevel * 10;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return getMinEnchantability(enchantmentLevel) + 5;
    }

    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    public int calcModifierDamage(int level, DamageSource source)
    {
        return 0;
    }

    /**
     * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     */
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
        return 0.0F;
    }

    public final boolean func_191560_c(Enchantment p_191560_1_)
    {
        return canApplyTogether(p_191560_1_) && p_191560_1_.canApplyTogether(this);
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    protected boolean canApplyTogether(Enchantment ench)
    {
        return this != ench;
    }

    /**
     * Sets the enchantment name
     */
    public Enchantment setName(String enchName)
    {
        name = enchName;
        return this;
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment." + name;
    }

    /**
     * Returns the correct traslated name of the enchantment and the level in roman numbers.
     */
    public String getTranslatedName(int level)
    {
        String s = I18n.translateToLocal(getName());

        if (func_190936_d())
        {
            s = Formatting.RED + s;
        }

        return level == 1 && getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level);
    }

    /**
     * Determines if this enchantment can be applied to a specific ItemStack.
     */
    public boolean canApply(ItemStack stack)
    {
        return type.canEnchantItem(stack.getItem());
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     */
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
    }

    /**
     * Whenever an entity that has this enchantment on one of its associated items is damaged this method will be
     * called.
     */
    public void onUserHurt(EntityLivingBase user, Entity attacker, int level)
    {
    }

    public boolean isTreasureEnchantment()
    {
        return false;
    }

    public boolean func_190936_d()
    {
        return false;
    }

    /**
     * Registers all of the vanilla enchantments.
     */
    public static void registerEnchantments()
    {
        EntityEquipmentSlot[] aentityequipmentslot = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
        REGISTRY.register(0, new Namespaced("protection"), new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.Type.ALL, aentityequipmentslot));
        REGISTRY.register(1, new Namespaced("fire_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FIRE, aentityequipmentslot));
        REGISTRY.register(2, new Namespaced("feather_falling"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FALL, aentityequipmentslot));
        REGISTRY.register(3, new Namespaced("blast_protection"), new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.Type.EXPLOSION, aentityequipmentslot));
        REGISTRY.register(4, new Namespaced("projectile_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.PROJECTILE, aentityequipmentslot));
        REGISTRY.register(5, new Namespaced("respiration"), new EnchantmentOxygen(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(6, new Namespaced("aqua_affinity"), new EnchantmentWaterWorker(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(7, new Namespaced("thorns"), new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, aentityequipmentslot));
        REGISTRY.register(8, new Namespaced("depth_strider"), new EnchantmentWaterWalker(Enchantment.Rarity.RARE, aentityequipmentslot));
        REGISTRY.register(9, new Namespaced("frost_walker"), new EnchantmentFrostWalker(Enchantment.Rarity.RARE, EntityEquipmentSlot.FEET));
        REGISTRY.register(10, new Namespaced("binding_curse"), new EnchantmentBindingCurse(Enchantment.Rarity.VERY_RARE, aentityequipmentslot));
        REGISTRY.register(16, new Namespaced("sharpness"), new EnchantmentDamage(Enchantment.Rarity.COMMON, 0, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(17, new Namespaced("smite"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 1, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(18, new Namespaced("bane_of_arthropods"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 2, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(19, new Namespaced("knockback"), new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(20, new Namespaced("fire_aspect"), new EnchantmentFireAspect(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(21, new Namespaced("looting"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(22, new Namespaced("sweeping"), new EnchantmentSweepingEdge(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(32, new Namespaced("efficiency"), new EnchantmentDigging(Enchantment.Rarity.COMMON, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(33, new Namespaced("silk_touch"), new EnchantmentUntouching(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(34, new Namespaced("unbreaking"), new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(35, new Namespaced("fortune"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(48, new Namespaced("power"), new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(49, new Namespaced("punch"), new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(50, new Namespaced("flame"), new EnchantmentArrowFire(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(51, new Namespaced("infinity"), new EnchantmentArrowInfinite(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(61, new Namespaced("luck_of_the_sea"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(62, new Namespaced("lure"), new EnchantmentFishingSpeed(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, EntityEquipmentSlot.MAINHAND));
        REGISTRY.register(70, new Namespaced("mending"), new EnchantmentMending(Enchantment.Rarity.RARE, EntityEquipmentSlot.values()));
        REGISTRY.register(71, new Namespaced("vanishing_curse"), new EnchantmentVanishingCurse(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.values()));
    }

    public enum Rarity
    {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        Rarity(int rarityWeight)
        {
            weight = rarityWeight;
        }

        public int getWeight()
        {
            return weight;
        }
    }
}
