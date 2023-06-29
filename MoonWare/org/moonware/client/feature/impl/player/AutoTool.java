package org.moonware.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTool extends Feature {

    private static BooleanSetting saveItem= new BooleanSetting("Save Item",true);
    private  static BooleanSetting swapBack = new BooleanSetting("SwapBack",true);
    private static BooleanSetting silentSwitch = new BooleanSetting("Silent Switch",true);

    public AutoTool() {
        super("AutoTool", "Автоматически берет лучший инструмент в руки при ломании блока", Type.Other);
        addSettings(saveItem, swapBack, silentSwitch);
    }

    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private ItemStack swapedItem;
    private List<Integer> lastItem = new ArrayList<>();

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isCreative() || Minecraft.player.isSpectator())
            return;

        Block hoverBlock = null;
        if (Minecraft.objectMouseOver.getBlockPos() == null)
            return;
        hoverBlock = Minecraft.world.getBlockState(Minecraft.objectMouseOver.getBlockPos()).getBlock();
        List<Integer> bestItem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (hoverBlock == null)
                break;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getMaxDamage()
                    - Minecraft.player.inventory.getStackInSlot(i).getItemDamage() > 1) && saveItem.get())
                continue;
            if (Minecraft.player.getDigSpeed(Minecraft.world.getBlockState(Minecraft.objectMouseOver.getBlockPos()),
                    Minecraft.player.inventory.getStackInSlot(i)) > 1)
                bestItem.add(i);

        }

        bestItem.sort(Comparator
                .comparingDouble(x -> -Minecraft.player.getDigSpeed(Minecraft.world.getBlockState(Minecraft.objectMouseOver.getBlockPos()),
                        Minecraft.player.inventory.getStackInSlot(x))));

        if (!bestItem.isEmpty() && Minecraft.gameSettings.keyBindAttack.pressed) {
            if (Minecraft.player.inventory.getCurrentItem() != swapedItem) {
                lastItem.add(Minecraft.player.inventory.currentItem);
                if (silentSwitch.isToggle())
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(bestItem.get(0)));
                else
                    Minecraft.player.inventory.currentItem = bestItem.get(0);

                itemIndex = bestItem.get(0);
                swap = true;
            }
            swapDelay = System.currentTimeMillis();
        } else if (swap && !lastItem.isEmpty() && System.currentTimeMillis() >= swapDelay + 300
                && swapBack.isToggle()) {

            if (silentSwitch.isToggle())
                Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(lastItem.get(0)));
            else
                Minecraft.player.inventory.currentItem = lastItem.get(0);

            itemIndex = lastItem.get(0);
            lastItem.clear();
            swap = false;
        }
    }
}