/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.button;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.ResourceLocation;
import ru.fluger.client.helpers.input.MouseHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;

public class ImageButton {
    protected int height;
    protected String description;
    protected int width;
    protected Minecraft mc;
    protected ResourceLocation image;
    protected int target;
    protected int x;
    protected float ani = 0.0f;
    protected int y;
    public static boolean hoverShop;

    public ImageButton(ResourceLocation resourceLocation, int x, int y, int width, int height, String description, int target) {
        this.image = resourceLocation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        this.mc = Minecraft.getMinecraft();
    }

    protected void hoverAnimation(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 228) {
                hoverShop = true;
            }
            if (this.ani < 5.0f) {
                this.ani = (float)((double)this.ani + 0.3 * Minecraft.frameTime * 0.1);
            }
        } else if (this.ani > 0.0f) {
            this.ani = (float)((double)this.ani - 0.2 * Minecraft.frameTime * 0.1);
            hoverShop = false;
        }
    }

    public void onClick(int mouseX, int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 16) {
                this.mc.displayGuiScreen(new GuiOptions(null, this.mc.gameSettings));
            }
            if (this.target == 15) {
                this.mc.displayGuiScreen(new GuiLanguage(null, this.mc.gameSettings, this.mc.getLanguageManager()));
            }
            if (this.target == 14) {
                this.mc.shutdown();
            }
            if (this.target == 19) {
                Minecraft.getMinecraft().displayGuiScreen(ClickGuiScreen.oldScreen);
            }
            if (this.target == 32) {
                for (int i = 0; i < 46; ++i) {
                    if (!this.mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, this.mc.player);
                }
            }
        }
    }

    public void draw(int mouseX, int mouseY, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        this.hoverAnimation(mouseX, mouseY);
        if (this.ani > 0.0f) {
            RenderHelper.drawImage(this.image, (float)this.x - this.ani, (float)this.y - this.ani, (float)(this.width + 4) + this.ani * 2.0f, (float)(this.height + 4) + this.ani * 2.0f, Color.BLACK);
            RenderHelper.drawImage(this.image, (float)this.x - this.ani, (float)this.y - this.ani, (float)this.width + this.ani * 2.0f, (float)this.height + this.ani * 2.0f, this.isHovered(mouseX, mouseY) ? new Color(156, 156, 156, 255) : Color.WHITE);
            if (this.isHovered(mouseX, mouseY)) {
                this.mc.robotoRegularFontRender.drawStringWithShadow(this.description, (float)this.x + (float)this.width / 2.0f + (float)this.mc.robotoRegularFontRender.getStringWidth(this.description) / 2.0f - (float)(this.target == 228 ? 35 : 6), this.y + this.height - 17, new Color(255, 255, 255, 255).getRGB());
            }
        } else {
            RenderHelper.drawImage(this.image, this.x, this.y, this.width + 3, this.height + 3, Color.BLACK);
            RenderHelper.drawImage(this.image, this.x, this.y, this.width, this.height, color);
        }
        GlStateManager.popMatrix();
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return MouseHelper.isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY);
    }
}

