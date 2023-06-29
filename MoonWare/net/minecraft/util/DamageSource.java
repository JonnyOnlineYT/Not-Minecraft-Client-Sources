package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;

import javax.annotation.Nullable;

public class DamageSource
{
    public static final DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
    public static final DamageSource lightningBolt = new DamageSource("lightningBolt");
    public static final DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static final DamageSource lava = (new DamageSource("lava")).setFireDamage();
    public static final DamageSource hotFloor = (new DamageSource("hotFloor")).setFireDamage();
    public static final DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static final DamageSource field_191291_g = (new DamageSource("cramming")).setDamageBypassesArmor();
    public static final DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
    public static final DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource cactus = new DamageSource("cactus");
    public static final DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
    public static final DamageSource flyIntoWall = (new DamageSource("flyIntoWall")).setDamageBypassesArmor();
    public static final DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
    public static final DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static final DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
    public static final DamageSource anvil = new DamageSource("anvil");
    public static final DamageSource fallingBlock = new DamageSource("fallingBlock");
    public static final DamageSource dragonBreath = (new DamageSource("dragonBreath")).setDamageBypassesArmor();
    public static final DamageSource field_191552_t = (new DamageSource("fireworks")).setExplosion();

    /** This kind of damage can be blocked or not. */
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;

    /**
     * Whether or not the damage ignores modification by potion effects or enchantments.
     */
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.1F;

    /** This kind of damage is based on fire or not. */
    private boolean fireDamage;

    /** This kind of damage is based on a projectile or not. */
    private boolean projectile;

    /**
     * Whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    private boolean difficultyScaled;

    /** Whether the damage is magic based. */
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;

    public static DamageSource causeMobDamage(EntityLivingBase mob)
    {
        return new EntityDamageSource("mob", mob);
    }

    public static DamageSource causeIndirectDamage(Entity source, EntityLivingBase indirectEntityIn)
    {
        return new EntityDamageSourceIndirect("mob", source, indirectEntityIn);
    }

    /**
     * returns an EntityDamageSource of type player
     */
    public static DamageSource causePlayerDamage(EntityPlayer player)
    {
        return new EntityDamageSource("player", player);
    }

    /**
     * returns EntityDamageSourceIndirect of an arrow
     */
    public static DamageSource causeArrowDamage(EntityArrow arrow, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn)).setProjectile();
    }

    /**
     * returns EntityDamageSourceIndirect of a fireball
     */
    public static DamageSource causeFireballDamage(EntityFireball fireball, @Nullable Entity indirectEntityIn)
    {
        return indirectEntityIn == null ? (new EntityDamageSourceIndirect("onFire", fireball, fireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", fireball, indirectEntityIn)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("thrown", source, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn)).setDamageBypassesArmor().setMagicDamage();
    }

    /**
     * Returns the EntityDamageSource of the Thorns enchantment
     */
    public static DamageSource causeThornsDamage(Entity source)
    {
        return (new EntityDamageSource("thorns", source)).setIsThornsDamage().setMagicDamage();
    }

    public static DamageSource causeExplosionDamage(@Nullable Explosion explosionIn)
    {
        return explosionIn != null && explosionIn.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public static DamageSource causeExplosionDamage(@Nullable EntityLivingBase entityLivingBaseIn)
    {
        return entityLivingBaseIn != null ? (new EntityDamageSource("explosion.player", entityLivingBaseIn)).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    /**
     * Returns true if the damage is projectile based.
     */
    public boolean isProjectile()
    {
        return projectile;
    }

    /**
     * Define the damage type as projectile based.
     */
    public DamageSource setProjectile()
    {
        projectile = true;
        return this;
    }

    public boolean isExplosion()
    {
        return explosion;
    }

    public DamageSource setExplosion()
    {
        explosion = true;
        return this;
    }

    public boolean isUnblockable()
    {
        return isUnblockable;
    }

    /**
     * How much satiate(food) is consumed by this DamageSource
     */
    public float getHungerDamage()
    {
        return hungerDamage;
    }

    public boolean canHarmInCreative()
    {
        return isDamageAllowedInCreativeMode;
    }

    /**
     * Whether or not the damage ignores modification by potion effects or enchantments.
     */
    public boolean isDamageAbsolute()
    {
        return damageIsAbsolute;
    }

    protected DamageSource(String damageTypeIn)
    {
        damageType = damageTypeIn;
    }

    @Nullable
    public Entity getSourceOfDamage()
    {
        return getEntity();
    }

    @Nullable
    public Entity getEntity()
    {
        return null;
    }

    protected DamageSource setDamageBypassesArmor()
    {
        isUnblockable = true;
        hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setDamageAllowedInCreativeMode()
    {
        isDamageAllowedInCreativeMode = true;
        return this;
    }

    /**
     * Sets a value indicating whether the damage is absolute (ignores modification by potion effects or enchantments),
     * and also clears out hunger damage.
     */
    protected DamageSource setDamageIsAbsolute()
    {
        damageIsAbsolute = true;
        hungerDamage = 0.0F;
        return this;
    }

    /**
     * Define the damage type as fire based.
     */
    protected DamageSource setFireDamage()
    {
        fireDamage = true;
        return this;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public Component getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + damageType;
        String s1 = s + ".player";
        return entitylivingbase != null && I18n.canTranslate(s1) ? new TranslatableComponent(s1, entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName());
    }

    /**
     * Returns true if the damage is fire based.
     */
    public boolean isFireDamage()
    {
        return fireDamage;
    }

    /**
     * Return the name of damage type.
     */
    public String getDamageType()
    {
        return damageType;
    }

    /**
     * Set whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public DamageSource setDifficultyScaled()
    {
        difficultyScaled = true;
        return this;
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled()
    {
        return difficultyScaled;
    }

    /**
     * Returns true if the damage is magic based.
     */
    public boolean isMagicDamage()
    {
        return magicDamage;
    }

    /**
     * Define the damage type as magic based.
     */
    public DamageSource setMagicDamage()
    {
        magicDamage = true;
        return this;
    }

    public boolean isCreativePlayer()
    {
        Entity entity = getEntity();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }

    @Nullable

    /**
     * Gets the location from which the damage originates.
     */
    public Vec3d getDamageLocation()
    {
        return null;
    }
}
