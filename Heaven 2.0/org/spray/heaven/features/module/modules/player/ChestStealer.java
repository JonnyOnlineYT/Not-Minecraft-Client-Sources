package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.SPacketWindowItems;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "ChestStealer", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class ChestStealer extends Module {

    public Setting maxDelay = register(new Setting("Max Delay", 160.0D, 10D, 300D));
    public Setting minDelay = register(new Setting("Min Delay", 90.0D, 10D, 300D));

    private Setting autoClose = register(new Setting("Auto Close", true));

    private Timer timer = new Timer();

    public SPacketWindowItems packet;

    public double delay;

    @Override
    public void onEnable() {
        delay = MathUtil.random(minDelay.getValue(), maxDelay.getValue());
    }

    @Override
    public void onDisable() {
        packet = null;
    }

    @Override
    public void onUpdate() {
        if (packet != null && mc.player.openContainer.windowId == packet.getWindowId()) {
            if (mc.currentScreen instanceof GuiShulkerBox || mc.currentScreen instanceof GuiChest) {
                if (!isContainerEmpty(mc.player.openContainer)) {
                    for (int i = 0; i < mc.player.openContainer.inventorySlots.size() - 36; ++i) {
                        Slot slot = mc.player.openContainer.getSlot(i);
                        if (slot.getHasStack() && slot.getStack() != null) {

                            if (timer.hasReached(delay)) {
                                quickItem(i);
                            }
                        }
                    }
                }
            }

            if (isContainerEmpty(mc.player.openContainer)) {
                if (autoClose.isToggle())
                    mc.player.closeScreen();
                if (packet != null)
                    packet = null;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(EventType.RECIEVE)) {
            if (event.getPacket() instanceof SPacketWindowItems)
                packet = (SPacketWindowItems) event.getPacket();
        }
    }

    private void quickItem(int slot) {
        mc.playerController.windowClick(mc.player.openContainer.windowId, slot, 1, ClickType.QUICK_MOVE, mc.player);

        timer.reset();
        delay = MathUtil.random(minDelay.getValue(), maxDelay.getValue());
    }

    /* GishCode */
    private boolean isContainerEmpty(Container container) {
        boolean temp = true;
        int i = 0;
        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; i++) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }

}
