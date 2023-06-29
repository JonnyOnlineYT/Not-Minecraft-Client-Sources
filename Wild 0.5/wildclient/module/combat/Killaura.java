//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.util.EnumHand;
import black.nigger.wildclient.module.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.settings.Setting;
import black.nigger.wildclient.Wild;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Counter;
import java.util.Random;
import black.nigger.wildclient.module.Module;

public class Killaura extends Module
{
    Random rand;
    public static float yaw;
    public static float pitch;
    public Random random;
    public Counter counter;
    
    public Killaura() {
        super("Killaura", "Killaura", Category.COMBAT);
        this.rand = new Random();
        this.random = new Random();
        this.counter = new Counter();
        Wild.instance.settingsManager.rSetting(new Setting("AuraRange", this, 3.2, 0.1, 10.0, false));
        Wild.instance.settingsManager.rSetting(new Setting("CriticalsFalling", this, true));
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final boolean criticals = Wild.instance.settingsManager.getSettingByName(this, "CriticalsFalling").getValBoolean();
        final float aurarange = (float)Wild.instance.settingsManager.getSettingByName(this, "AuraRange").getValDouble();
        if (Killaura.mc.player == null) {
            return;
        }
        for (final EntityPlayer targetez : Killaura.mc.world.playerEntities) {
            if (targetez != Killaura.mc.player) {
                if (targetez.isInvisible()) {
                    continue;
                }
                if (Killaura.mc.player.getCooledAttackStrength(0.0f) == 1.0f && Killaura.mc.player.getDistance((Entity)targetez) <= aurarange && Killaura.mc.player.fallDistance >= 0.1 && criticals) {
                    Killaura.mc.player.rotationYaw = RotationUtils.getNeededRotations((Entity)targetez)[0];
                    Killaura.mc.player.rotationPitch = RotationUtils.getNeededRotations((Entity)targetez)[1] + this.random.nextFloat() * 35.0f - 5.0f;
                    Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.player, (Entity)targetez);
                    Killaura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.counter.reset();
                }
                else {
                    if (Killaura.mc.player.getCooledAttackStrength(0.0f) != 1.0f || Killaura.mc.player.getDistance((Entity)targetez) > aurarange || criticals) {
                        continue;
                    }
                    Killaura.mc.player.rotationYaw = RotationUtils.getNeededRotations((Entity)targetez)[0];
                    Killaura.mc.player.rotationPitch = RotationUtils.getNeededRotations((Entity)targetez)[1] + this.random.nextFloat() * 35.0f - 5.0f;
                    Killaura.mc.playerController.attackEntity((EntityPlayer)Killaura.mc.player, (Entity)targetez);
                    Killaura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.counter.reset();
                }
            }
        }
    }
}
