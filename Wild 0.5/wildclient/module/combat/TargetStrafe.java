//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import black.nigger.wildclient.module.EventManager;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.entity.Entity;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import net.minecraft.client.Minecraft;
import black.nigger.wildclient.module.TimerUtils;
import black.nigger.wildclient.module.Counter;
import black.nigger.wildclient.module.Module;

public class TargetStrafe extends Module
{
    public Counter counter;
    public TimerUtils timerUtils;
    public Minecraft mc;
    public static int direction;
    
    public TargetStrafe() {
        super("TargetStrafe", "Rotates around target", Category.COMBAT);
        this.counter = new Counter();
        this.mc = Minecraft.getMinecraft();
        Wild.instance.settingsManager.rSetting(new Setting("Range", this, 3.0, 0.1, 7.0, false));
        Wild.instance.settingsManager.rSetting(new Setting("Boost", this, 2.8, 0.1, 7.0, false));
        Wild.instance.settingsManager.rSetting(new Setting("SwitchMS", this, 5000.0, 1.0, 10000.0, true));
        Wild.instance.settingsManager.rSetting(new Setting("AutoSneak", this, true));
        Wild.instance.settingsManager.rSetting(new Setting("DamageBoost", this, true));
    }
    
    public Entity getTarget() {
        if (this.mc.player == null || this.mc.player.isDead) {
            return null;
        }
        final List list = (List)this.mc.world.loadedEntityList.stream().filter(entity -> entity != this.mc.player).filter(entity -> this.mc.player.getDistance(entity) <= 35.0f).filter(entity -> !entity.isDead).filter(this::lambdagetTarget).sorted(Comparator.comparing(entity -> this.mc.player.getDistance(entity))).collect(Collectors.toList());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        super.onDisable();
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }
    
    public final void doStrafeAtSpeed(final double d) {
        final boolean autoSneak = Wild.instance.settingsManager.getSettingByName(this, "AutoSneak").getValBoolean();
        if (!this.mc.player.isDead) {
            final boolean bl = true;
            final float range = (float)Wild.instance.settingsManager.getSettingByName(this, "Range").getValDouble();
            final Entity entity = this.getTarget();
            if (entity != null) {
                if (this.mc.player.onGround) {
                    this.mc.player.jump();
                }
                final float[] arrf = RotationUtils.getNeededRotations(entity);
                if (Minecraft.getMinecraft().player.getDistance(entity) <= (double)range) {
                    this.mc.player.renderYawOffset = arrf[0];
                    this.mc.player.rotationYawHead = arrf[0];
                    this.setSpeed(d - 0.05, arrf[0], TargetStrafe.direction, 0.0);
                }
                else {
                    this.setSpeed(d - 0.05, arrf[0], TargetStrafe.direction, 1.0);
                    this.mc.player.renderYawOffset = arrf[0];
                    this.mc.player.rotationYawHead = arrf[0];
                }
                if (this.isToggled() && Minecraft.getMinecraft().player.getDistance(entity) <= 7.0 && autoSneak) {
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
                else {
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final float switchMS = (float)Wild.instance.settingsManager.getSettingByName(this, "SwitchMS").getValDouble();
        if (this.counter.hasReached(switchMS)) {
            this.invertStrafe();
            this.counter.reset();
        }
        if (this.mc.player.collidedHorizontally) {
            this.invertStrafe();
        }
        this.mc.player.movementInput.moveForward = 0.0f;
        double d = this.getMovementSpeed();
        final float boost = (float)Wild.instance.settingsManager.getSettingByName(this, "Boost").getValDouble();
        if (this.mc.player.hurtTime > 0) {
            this.doStrafeAtSpeed(d *= boost);
        }
        else {
            this.doStrafeAtSpeed(d);
        }
    }
    
    public double getMovementSpeed() {
        final boolean damageBoost = Wild.instance.settingsManager.getSettingByName(this, "DamageBoost").getValBoolean();
        final boolean boosted = false;
        double d = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(1))) && !boosted) {
            final int n = Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            d *= 1.0 + 0.2 * (n + 1);
        }
        return d;
    }
    
    private boolean lambdagetTarget(final Entity entity) {
        return this.attackCheck(entity);
    }
    
    private void invertStrafe() {
        TargetStrafe.direction = -TargetStrafe.direction;
    }
    
    public void setSpeed(final double d, final float f, final double d2, final double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;
        if (d4 == 0.0 && d5 == 0.0) {
            this.mc.player.motionZ = 0.0;
            this.mc.player.motionX = 0.0;
        }
        else {
            if (d4 != 0.0) {
                if (d5 > 0.0) {
                    f2 += ((d4 > 0.0) ? -45 : 45);
                }
                else if (d5 < 0.0) {
                    f2 += ((d4 > 0.0) ? 45 : -45);
                }
                d5 = 0.0;
                if (d4 > 0.0) {
                    d4 = 1.0;
                }
                else if (d4 < 0.0) {
                    d4 = -1.0;
                }
            }
            final double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
            final double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
            this.mc.player.motionX = d4 * d * d6 + d5 * d * d7;
            this.mc.player.motionZ = d4 * d * d7 - d5 * d * d6;
        }
    }
    
    public boolean attackCheck(final Entity entity) {
        return !entity.isInvisible() && entity instanceof EntityPlayer;
    }
    
    static {
        TargetStrafe.direction = -1;
    }
}
