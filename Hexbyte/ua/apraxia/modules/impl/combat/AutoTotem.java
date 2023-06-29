package ua.apraxia.modules.impl.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.math.TimerUtility;

import java.util.ArrayList;
import java.util.List;

public class AutoTotem extends Module {
    private final BooleanSetting checkTnt = new BooleanSetting("Check Tnt", true);
    private final SliderSetting radiusTnt = new SliderSetting("Distance to Tnt", 6, 1, 8, 1);

    private final BooleanSetting switchBack = new BooleanSetting("Swap Back", true);
    public SliderSetting health = new SliderSetting("Health Amount", 3.5f, 1.f, 20.f, 0.5F);
    private final BooleanSetting inventoryOnly = new BooleanSetting("Only Inventory", false);
    public SliderSetting swapBackDelay = new SliderSetting("Swap back delay", 100, 10, 500, 5);
    private final BooleanSetting countTotem = new BooleanSetting("Count Totem", true);
    private final BooleanSetting checkFall = new BooleanSetting("Check Fall", true);
    private final SliderSetting fallDistance = new SliderSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f);
    private final BooleanSetting checkCrystal = new BooleanSetting("Check Crystal", true);
    private final SliderSetting radiusCrystal = new SliderSetting("Distance to Crystal", 6, 1, 8, 1);
    private final List<Integer> lastItem = new ArrayList<>();
    private final TimerUtility timerHelper = new TimerUtility();
    private boolean swap = false;

    public AutoTotem() {
        super("AutoTotem", Categories.Combat);
        addSetting(switchBack, swapBackDelay, health, inventoryOnly, countTotem, checkFall, fallDistance, checkCrystal, radiusCrystal, checkTnt, radiusTnt);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }


  /*  @EventTarget
    public void onRender2D(EventRender2D event) {
        if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
            mc.rubik_18.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.Totem) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    } */

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (inventoryOnly.value && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING && tIndex == -1) {
                tIndex = i;
            }
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemCount++;
            }
        }

        if ((mc.player.getHealth() < health.value || checkCrystal() || checkTNT() || checkFall(fallDistance.value)) && totemCount != 0 && tIndex != -1) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 0, ClickType.PICKUP, mc.player);
                swap = true;
                lastItem.add(tIndex);
            }
        } else if (switchBack.value && (swap || totemCount == 0) && lastItem.size() > 0) {
            if (!(mc.player.inventory.getStackInSlot(lastItem.get(0)).getItem() instanceof ItemAir)) {
                if (timerHelper.hasReached(swapBackDelay.value)) {

                    mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);

                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);
                    timerHelper.reset();

                }
            }
            swap = false;
            lastItem.clear();
        }
    }

    private boolean checkFall(float fallDist) {
        if (!checkFall.value) {
            return false;
        }
        return mc.player.fallDistance > fallDist;
    }

    private boolean checkTNT() {
        if (!checkTnt.value) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistance(entity) <= radiusTnt.value) {
                return true;
            }
            if (!(entity instanceof EntityMinecartTNT) || !(AutoTotem.mc.player.getDistance(entity) <= radiusTnt.value))
                continue;
            return true;
        }
        return false;
    }

    private boolean checkCrystal() {
        if (!checkCrystal.value) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(AutoTotem.mc.player.getDistance(entity) <= radiusCrystal.value))
                continue;
            return true;
        }
        return false;
    }
}