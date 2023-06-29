package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;


/*
    Do not question the power of the ratware.
 */

public class AntiPlate extends Cheats {
    private int airTicks;

    public AntiPlate() {
        super("AntiPlate", "The greatest module to ever walk the planet Earth.", Selection.MOVEMENT);
    }

    @Override
    public void onEnable() {
        airTicks = 0;
    }

    @EventTarget
    private void onPreUpdate(EventUpdate event) {
        if (event.isPre()) {
            if (Minecraft.thePlayer.isMoving()) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                    airTicks = 0;
                } else {
                    if (airTicks < 9) {
                        airTicks++;
                    } else {
                        Minecraft.thePlayer.motionY = 0;
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1E-8,
                                Minecraft.thePlayer.posZ);
                    }
                }
            }
        }
    }
}