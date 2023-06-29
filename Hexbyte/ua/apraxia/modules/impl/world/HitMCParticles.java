package ua.apraxia.modules.impl.world;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventAttackSilent;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.Utility;
import net.minecraft.util.EnumParticleTypes;

import java.util.Random;

public class HitMCParticles extends Module {
    public SliderSetting particleMultiplier = new SliderSetting("Value", 5, 1, 15, 1);
    private final Random random = new Random();

    public HitMCParticles() {
        super("HitMCParticles", Categories.World);
        addSetting(particleMultiplier);
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
            for (float i = 0; i < event.getTargetEntity().height; i += 0.2F) {
                for (int i2 = 0; i2 < particleMultiplier.value; i2++) {
                    Utility.mc.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, event.getTargetEntity().posX, event.getTargetEntity().posY + i, event.getTargetEntity().posZ, ((random.nextInt(6) - 3) / 5F), 0.1D, ((random.nextInt(6) - 3) / 5F));
                }
            }
        }
    }
