package ru.rockstar.client.features.impl.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ItemESP extends Feature {
    private BooleanSetting tag;
    private BooleanSetting border;
    private ColorSetting borderColor;

    public ItemESP() {
        super("ItemESP", "Отображение айтемов",0, Category.VISUALS);
        tag = new BooleanSetting("Tag", true, () -> true);
        border = new BooleanSetting("Full Box", true, () -> true);
        borderColor = new ColorSetting("Border Color", new Color(0xFFFFFF).getRGB() , () -> border.getBoolValue());
       addSettings(tag,border,borderColor);
    }
    @EventTarget
    public void onRender(Event3D event) {
        for (Entity e : this.mc.world.loadedEntityList) {
            if (!(e instanceof EntityItem)) continue;
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - RenderManager.renderPosX - 0.1;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - RenderManager.renderPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - RenderManager.renderPosZ - 0.15;
            if (this.tag.getBoolValue()) {
                GL11.glPushMatrix();
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                float size = Math.min(Math.max(1.2f * (Minecraft.getMinecraft().player.getDistanceToEntity(e) * 0.15f), 1.25f), 6.0f) * 0.014f;
                GL11.glTranslatef((float)((float)x), (float)((float)y + e.height + 0.5f), (float)((float)z));
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                GL11.glScalef((float)(-size), (float)(-size), (float)size);
                Gui.drawRect(-Minecraft.fontRendererObj.getStringWidth(((EntityItem)e).getEntityItem().getDisplayName()) / 2 - 2, -2.0, Minecraft.fontRendererObj.getStringWidth(((EntityItem)e).getEntityItem().getDisplayName()) / 2 + 2, 9.0, Integer.MIN_VALUE);
                Minecraft.fontRendererObj.drawStringWithShadow(((EntityItem)e).getEntityItem().getDisplayName(), -Minecraft.fontRendererObj.getStringWidth(((EntityItem)e).getEntityItem().getDisplayName()) / 2, 0, -1);
                GlStateManager.resetColor();
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glPopMatrix();
            }
            if(border.getBoolValue()) {
                DrawHelper.drawEntityBox(e, new Color(borderColor.getColorValue()), true, 0.20F);
            }
        }
    }
}
