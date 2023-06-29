//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.modules.impl.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.utility.other.InventoryUtil;
import ua.apraxia.utility.other.MoveUtility;

public class ElytraHelper extends Module {
    public BooleanSetting autojump = new BooleanSetting("AutoJump", false);
    public BooleanSetting isAirBorne = new BooleanSetting("AutoElytra",  false);
    public BooleanSetting autofly = new BooleanSetting("AutoFly", false);

    public ElytraHelper() {
        super("ElytraHelper", Categories.Other);
        this.addSetting(isAirBorne, autojump, autofly);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        Minecraft var10000;
        if (this.autofly.value) {
            var10000 = mc;
            if (Minecraft.player.isElytraFlying()) {
                var10000 = mc;
                Minecraft.player.jump();
                if (this.autofly.value) {
                    var10000 = mc;
                    if (Minecraft.player.isElytraFlying()) {
                        var10000 = mc;
                        if (Minecraft.player.isSneaking()) {
                            var10000 = mc;
                          //  Minecraft.player.motionY = -0.5;
                        }
                    }
                }
            }
        }

        if (this.autojump.value) {
            var10000 = mc;
            if (Minecraft.player.onGround) {
                var10000 = mc;
                Minecraft.player.jump();
            }
        }

        if (this.isAirBorne.value) {
            var10000 = mc;
            if (Minecraft.player.isAirBorne) {
                var10000 = mc;
                if (Minecraft.player.getFoodStats().getFoodLevel() / 2 > 3) {
                    var10000 = mc;
                    Minecraft.player.setSprinting(MoveUtility.isMoving());
                    boolean elytra = false;
                    var10000 = mc;
                    Minecraft var10003 = mc;
                   Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, Action.START_FALL_FLYING));
                }
            }
        }

    }

    @EventTarget
    public void onUpdate(EventPreMotion eventPreMotion) {
        Minecraft var10000 = mc;
        if (Minecraft.player.ticksExisted % 4 == 0) {
            NetHandlerPlayClient var2 = mc.getConnection();
            Minecraft var10003 = mc;
           var2.sendPacket(new CPacketEntityAction(Minecraft.player, Action.START_FALL_FLYING));
        }

    }

    public void onDisable() {
        InventoryUtil.swapElytraToChestplate();
        Minecraft var10000 = mc;
        Minecraft.player.setSprinting(false);
        super.onDisable();
    }
}
