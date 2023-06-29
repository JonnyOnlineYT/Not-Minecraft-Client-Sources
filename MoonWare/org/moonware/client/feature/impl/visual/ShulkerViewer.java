package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.callables.EventRenderToolTip;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render.rect.RectHelper;

public class ShulkerViewer
        extends Feature {
    public ShulkerViewer() {
        super("ShulkerViewer", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u0435 \u0448\u0430\u043b\u043a\u0435\u0440\u0430", Type.Visuals);
    }

    @EventTarget
    public void onRenderToolTip(EventRenderToolTip event) {
        Minecraft mc = Minecraft.getMinecraft();
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound;
        if (event.getStack().getItem() instanceof ItemShulkerBox && (tagCompound = event.getStack().getTagCompound()) != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            event.setCancelled(true);
            NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(27, ItemStack.EMPTY);
            ItemStackHelper.func_191283_b(blockEntityTag, nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int width = Math.max(144, Minecraft.font.getStringWidth(event.getStack().getDisplayName()) + 3);
            int x1 = event.getX() + 12;
            int y1 = event.getY() - 12;
            int height = 57;
            Minecraft.getRenderItem().zLevel = 300.0f;
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 0x505000FF, 1344798847);
            RectHelper.drawMinecraftRect(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 0x505000FF, 1344798847);
            RectHelper.drawMinecraftRect(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 0x505000FF, 0x505000FF);
            RectHelper.drawMinecraftRect(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);
            Minecraft.font.drawStringWithShadow(event.getStack().getDisplayName(), event.getX() + 12, event.getY() - 12, 0xFFFFFF);
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
                Minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                Minecraft.getRenderItem().renderItemOverlayIntoGUI(Minecraft.font, itemStack, iX, iY, null);
            }
            RenderHelper.disableStandardItemLighting();
            Minecraft.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}
