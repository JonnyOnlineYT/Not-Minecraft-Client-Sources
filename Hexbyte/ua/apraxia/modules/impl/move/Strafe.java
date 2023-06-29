package ua.apraxia.modules.impl.move;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import ua.apraxia.Hexbyte;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.utility.other.MoveUtility;

public class Strafe extends Module {
    public static BooleanSetting smart = new BooleanSetting("Smart", false);

    public Strafe() {
        super("Strafe", Categories.Movement);
        addSetting(smart);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!Hexbyte.getInstance().moduleManagment.getModule(Strafe.class).isModuleState()) {
            return;
        }
        if (MoveUtility.isMoving()) {
            if (MoveUtility.getSpeed() < 0.2177f) {
                MoveUtility.strafe();
                if (Math.abs(mc.player.movementInput.moveStrafe) < 0.1 && mc.gameSettings.keyBindForward.pressed) {
                    MoveUtility.strafe();
                }
                if (mc.player.onGround) {
                    MoveUtility.strafe();
                }
            }
        }
    }
}
