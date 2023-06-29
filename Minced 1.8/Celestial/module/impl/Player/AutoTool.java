package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.ui.settings.impl.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@SuppressWarnings("all")

public class AutoTool extends Module {
    private BooleanSetting swapBack = new BooleanSetting("Swap Back", true, () -> true);
    private BooleanSetting saveItem = new BooleanSetting("Save Item", true, () -> true);
    public BooleanSetting silentSwitch = new BooleanSetting("Silent Switch", true, () -> true);

    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private ItemStack swapedItem = null;
    private List<Integer> lastItem = new ArrayList<>();

    public AutoTool() {
        super("AutoTool", "Автоматически берет нужный предмет в руку, для ломания блока и т.п", ModuleCategory.Player);
        addSettings(swapBack, saveItem, silentSwitch);
    }


    @EventTarget
    public void onUpdate(EventUpdate update) {

        Block hoverBlock = null;
        if (Helper.mc.objectMouseOver.getBlockPos() == null) return;
        hoverBlock = Helper.mc.world.getBlockState(Helper.mc.objectMouseOver.getBlockPos()).getBlock();
        List<Integer> bestItem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (hoverBlock == null) break;
            if (!(Helper.mc.player.inventory.getStackInSlot(i).getMaxDamage() - Helper.mc.player.inventory.getStackInSlot(i).getItemDamage() > 1) && saveItem.getCurrentValue())
                continue;
            if(Helper.mc.player.getDigSpeed(Helper.mc.world.getBlockState(Helper.mc.objectMouseOver.getBlockPos()), Helper.mc.player.inventory.getStackInSlot(i)) > 1)
                bestItem.add(i);

        }

        bestItem.sort(Comparator.comparingDouble(x -> -Helper.mc.player.getDigSpeed(Helper.mc.world.getBlockState(Helper.mc.objectMouseOver.getBlockPos()), Helper.mc.player.inventory.getStackInSlot(x))));


        if (!bestItem.isEmpty() && Helper.mc.gameSettings.keyBindAttack.pressed) {
            if (Helper.mc.player.inventory.getCurrentItem() != swapedItem) {
                lastItem.add(Helper.mc.player.inventory.currentItem);
                if (silentSwitch.getCurrentValue())
                    Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(bestItem.get(0)));
                else
                    Helper.mc.player.inventory.currentItem = bestItem.get(0);

                itemIndex = bestItem.get(0);
                swap = true;
            }
            swapDelay = System.currentTimeMillis();
        } else if (swap && !lastItem.isEmpty() && System.currentTimeMillis() >= swapDelay + 300 && swapBack.getCurrentValue()) {

            if (silentSwitch.getCurrentValue())
                Helper.mc.player.connection.sendPacket(new CPacketHeldItemChange(lastItem.get(0)));
            else
                Helper.mc.player.inventory.currentItem = lastItem.get(0);

            itemIndex = lastItem.get(0);
            lastItem.clear();
            swap = false;
        }
    }
}