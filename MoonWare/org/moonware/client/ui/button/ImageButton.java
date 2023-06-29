package org.moonware.client.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Namespaced;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.ui.components.draggable.GuiHudEditor;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.ui.GuiConfig;

import java.awt.*;

public class ImageButton {

    protected int height;
    protected String description;
    protected int width;
    protected Minecraft mc;
    protected Namespaced image;
    protected int target;
    protected int x;
    protected int ani;
    protected int y;
    //protected int color = Color.WHITE.getRGB();

    public ImageButton(Namespaced namespaced, int x, int y, int width, int height, String description, int target) {
        image = namespaced;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        mc = Minecraft.getMinecraft();
    }



    protected void hoverAnimation(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            if (ani < 3) {
                ani++;
            }
        } else if (ani > 0) {
            ani--;
        }
    }

    public void onClick(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY) && !true) {
            if (target == 19) {
                Minecraft.openScreen(null);
            }
            if (target == 22) {
                Minecraft.openScreen(new GuiConfig());
            }
            if (target == 18) {
                Minecraft.openScreen(new GuiHudEditor());
            }
            if (target == 30) {
                GuiChest chest = (GuiChest) Minecraft.screen;
                if (chest != null) {
                    new Thread(() -> {
                        try {
                            for (int i = 0; i < chest.getInventoryRows() * 9; i++) {
                                ContainerChest container = (ContainerChest) Minecraft.player.openContainer;
                                if (container != null) {
                                    Thread.sleep(50L);
                                    Minecraft.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, Minecraft.player);
                                }
                            }
                        } catch (Exception ignored) {
                        }

                    }).start();
                }
            }
            if (target == 31) {
                GuiChest chest = (GuiChest) Minecraft.screen;
                if (chest != null) {
                    new Thread(() -> {
                        try {
                            for (int i = chest.getInventoryRows() * 9; i < chest.getInventoryRows() * 9 + 44; i++) {
                                Slot slot = chest.inventorySlots.inventorySlots.get(i);
                                if (slot.getStack() != null) {
                                    Thread.sleep(50L);
                                    chest.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                                }
                            }
                        } catch (Exception ignored) {
                        }

                    }).start();
                }
            }
            if (target == 32) {
                for (int i = 0; i < 46; i++) {
                    if (Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) {
                        Minecraft.playerController.windowClick(Minecraft.player.inventoryContainer.windowId, i, 1, ClickType.THROW, Minecraft.player);
                    }
                }
            }
        }
    }

    public void draw(int mouseX, int mouseY, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        hoverAnimation(mouseX, mouseY);
        if (ani > 0) {
            if (target == 22) {
                RenderHelper.drawImage(image, x - ani, y - ani, width + ani * 2, height + ani * 2, new Color(21, 103, 129, 255));
            }else{
                RenderHelper.drawImage(image, x - ani, y - ani, width + ani * 2, height + ani * 2, new Color(156, 156, 156, 255));
            }
        } else if (target == 22) {
            RenderHelper.drawImage(image, x - ani, y - ani, width, height, new Color(26, 127, 161, 255));
        }else{
            RenderHelper.drawImage(image, x, y, width, height, color);
        }
        GlStateManager.popMatrix();
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return MWUtils.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }
}