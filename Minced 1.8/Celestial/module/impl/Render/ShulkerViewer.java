package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRenderToolTip;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.awt.*;

public class ShulkerViewer extends Module {
    public ShulkerViewer() {
        super("ShulkerViewer", "Позволяет смотреть содержимое шалкера", ModuleCategory.Render);
    }

    @EventTarget
    public void onRenderToolTip(EventRenderToolTip event) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound;
        if (event.getStack().getItem() instanceof ItemShulkerBox && (tagCompound = event.getStack().getTagCompound()) != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            event.setCancelled(true);
            NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b(blockEntityTag, nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int width = Math.max(144, Helper.mc.tenacity.getStringWidth(event.getStack().getDisplayName()) + 3);
            int x1 = event.getX() + 12;
            int y1 = event.getY() - 12;
            int height = 57;
            Helper.mc.getRenderItem().zLevel = 300.0f;
            RenderUtils.drawBlurredShadow(x1 - 3, y1 - 3,  width + 3, height + 3,15, new Color(17,17,17,200));

            Gui.drawRect2(x1 - 3, y1 - 3,  width + 3, height + 3, new Color(17,17,17,200).getRGB());


            Helper.mc.tenacity.drawStringWithShadow(event.getStack().getDisplayName(), event.getX() + 12, event.getY() - 12, 0xFFFFFF);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = event.getX() + i % 9 * 16 + 11;
                int iY = event.getY() + i / 9 * 16 - 11 + 8;
                ItemStack itemStack = nonnulllist.get(i);
                Helper.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                Helper.mc.getRenderItem().renderItemOverlayIntoGUI(Helper.mc.fontRendererObj, itemStack, iX, iY, null);
            }
            RenderHelper.disableStandardItemLighting();
            Helper.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}
