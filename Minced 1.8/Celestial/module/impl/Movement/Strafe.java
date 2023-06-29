package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.event.events.impl.player.EventMove;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.inventory.InventoryUtil;
import Celestial.utils.movement.MovementUtils;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.network.play.client.CPacketEntityAction;

public class Strafe extends Module {
    public ListSetting strafeMode = new ListSetting("Strafe Mode", "Matrix", () -> true, "Matrix");
    public final NumberSetting strafeSpeed = new NumberSetting("Strafe Speed", 0.2F, 0.01f, 0.3f, 0.01F, () -> strafeMode.currentMode.equals("Elytra"));

    public Strafe() {
        super("Strafe", "Стрейфы", ModuleCategory.Movement);
        addSettings(strafeMode, strafeSpeed);
    }

    @EventTarget
    public void onPre(EventMove eventPreMotion) {
        if (strafeMode.currentMode.equalsIgnoreCase("Elytra")) {

                    MovementUtils.strafe();
                    if (Math.abs(Helper.mc.player.movementInput.moveStrafe) < 0.1 && Helper.mc.gameSettings.keyBindForward.pressed) {
                        MovementUtils.strafe();
                    }
                    if (Helper.mc.player.onGround) {
                        MovementUtils.strafe();
                    }
                }
            }
        }
    }

    private void disabler() {

        int elytra = InventoryUtil.getItemIndex(Items.ELYTRA);

        if (Helper.mc.player.inventory.getItemStack().getItem() != Items.ELYTRA)
            Helper.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, Helper.mc.player);

        Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
        Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
        Helper.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, Helper.mc.player);

    }

    @EventTarget
    public void on(EventSendPacket event) {
        if (strafeMode.currentMode.equals("Elytra")) {
            if (Helper.mc.player.isOnLadder() || Helper.mc.player.isInLiquid() || InventoryUtil.getSlotWithElytra() == -1) {
                return;

            }
        }
    }
}
