package ru.rockstar.client.features.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.other.SoundHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class DeathSounds extends Feature
{
    public ListSetting deathSoundMode;
    public NumberSetting volume;
    
    public DeathSounds() {
        super("DeathSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a\u0438 \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u043a\u0430\u043a\u043e\u0433\u043e \u043b\u0438\u0431\u043e \u0438\u0433\u0440\u043e\u043a\u0430", 0, Category.MISC);
        this.deathSoundMode = new ListSetting("Death Sound Mode", "Tyan", () -> true, new String[] { "Tyan", "Bruh", "Wolf", "Villager", "Ghast", "Blaze", "Guardian", "Iron Golem", "Skeleton", "Zombie", "Chicken", "Cow", "Pig", "Enderman", "Polar Bear", "Ender Dragon" });
        this.volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);
        this.addSettings(this.deathSoundMode, this.volume);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(this.deathSoundMode.currentMode, true);
        for (final Entity entity : DeathSounds.mc.world.loadedEntityList) {
            if (entity == null) {
                continue;
            }
            if (!(entity instanceof EntityPlayer) || ((EntityLivingBase)entity).deathTime >= 1 || ((EntityPlayer)entity).getHealth() > 0.0f || DeathSounds.mc.player.getDistanceToEntity(entity) >= 10.0f || entity.ticksExisted <= 5) {
                continue;
            }
            final float volume = this.volume.getNumberValue() / 10.0f;
            final String currentMode = this.deathSoundMode.currentMode;
            switch (currentMode) {
                case "Bruh": {
                    SoundHelper.playSound("bruh.wav", -30.0f + volume * 3.0f, false);
                    continue;
                }
                case "Tyan": {
                    SoundHelper.playSound("yametekudasai.wav", -30.0f + volume * 3.0f, false);
                    continue;
                }
                case "Wolf": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_WOLF_DEATH, volume, 1.0f);
                    continue;
                }
                case "Villager": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, volume, 1.0f);
                    continue;
                }
                case "Blaze": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_BLAZE_DEATH, volume, 1.0f);
                    continue;
                }
                case "Chicken": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, volume, 1.0f);
                    continue;
                }
                case "Enderman": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ENDERMEN_DEATH, volume, 1.0f);
                    continue;
                }
                case "Ender Dragon": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ENDERDRAGON_DEATH, volume, 1.0f);
                    continue;
                }
                case "Cow": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_COW_DEATH, volume, 1.0f);
                    continue;
                }
                case "Pig": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_PIG_DEATH, volume, 1.0f);
                    continue;
                }
                case "Skeleton": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_SKELETON_DEATH, volume, 1.0f);
                    continue;
                }
                case "Ghast": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_GHAST_DEATH, volume, 1.0f);
                    continue;
                }
                case "Zombie": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ZOMBIE_DEATH, volume, 1.0f);
                    continue;
                }
                case "Polar Bear": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_POLAR_BEAR_DEATH, volume, 1.0f);
                    continue;
                }
                case "Guardian": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_GUARDIAN_DEATH, volume, 1.0f);
                    continue;
                }
                case "Iron Golem": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_IRONGOLEM_DEATH, volume, 1.0f);
                    continue;
                }
            }
        }
    }
}