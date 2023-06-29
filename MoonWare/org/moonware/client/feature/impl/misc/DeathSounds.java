package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class DeathSounds
        extends Feature {
    public ListSetting deathSoundMode = new ListSetting("Mode", "Oof", () -> true, "Oof", "Bruh", "Punch", "Wolf", "Villager", "Ghast", "Blaze", "Guardian", "Iron Golem", "Skeleton", "Zombie", "Chicken", "Cow", "Pig", "Enderman", "Polar Bear", "Ender Dragon");
    public NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public DeathSounds() {
        super("DeathSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a\u0438 \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u043a\u0430\u043a\u043e\u0433\u043e \u043b\u0438\u0431\u043e \u0438\u0433\u0440\u043e\u043a\u0430", Type.Other);
        addSettings(deathSoundMode, volume);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(deathSoundMode.currentMode);
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).getHealth() <= 0.0f) || ((EntityLivingBase)entity).deathTime >= 1 || !(Minecraft.player.getDistanceToEntity(entity) < 10.0f) || entity.ticksExisted <= 5) continue;
            float volume = this.volume.getCurrentValue() / 10.0f;
            switch (deathSoundMode.currentMode) {
                case "Oof": {
                    MWUtils.playSound("oof.wav", -30.0f + volume * 3.0f);
                    break;
                }
                case "Bruh": {
                    MWUtils.playSound("bruh.wav", -30.0f + volume * 3.0f);
                    break;
                }
                case "Punch": {
                    MWUtils.playSound("punch.wav", -30.0f + volume * 3.0F);
                    break;
                }
                case "Wolf": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_WOLF_DEATH, volume, 1.0f);
                    break;
                }
                case "Villager": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, volume, 1.0f);
                    break;
                }
                case "Blaze": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_BLAZE_DEATH, volume, 1.0f);
                    break;
                }
                case "Chicken": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, volume, 1.0f);
                    break;
                }
                case "Enderman": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_ENDERMEN_DEATH, volume, 1.0f);
                    break;
                }
                case "Ender Dragon": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_ENDERDRAGON_DEATH, volume, 1.0f);
                    break;
                }
                case "Cow": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_COW_DEATH, volume, 1.0f);
                    break;
                }
                case "Pig": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_PIG_DEATH, volume, 1.0f);
                    break;
                }
                case "Skeleton": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_SKELETON_DEATH, volume, 1.0f);
                    break;
                }
                case "Ghast": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_GHAST_DEATH, volume, 1.0f);
                    break;
                }
                case "Zombie": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_ZOMBIE_DEATH, volume, 1.0f);
                    break;
                }
                case "Polar Bear": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_POLAR_BEAR_DEATH, volume, 1.0f);
                    break;
                }
                case "Guardian": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_GUARDIAN_DEATH, volume, 1.0f);
                    break;
                }
                case "Iron Golem": {
                    Minecraft.player.playSound(SoundEvents.ENTITY_IRONGOLEM_DEATH, volume, 1.0f);
                }
            }
        }
    }
}
