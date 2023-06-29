package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRenderModelEntity;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class Chams extends Module {
	@Override
	protected void initSettings() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRenderModelEntity) {
			Entity entity = ((EventRenderModelEntity) event).getEntity();
			CastHelper castHelper = new CastHelper();
			if (visuals_entitiestab.get(0).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.PLAYERS);
			}
			if (visuals_entitiestab.get(1).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.MOBS);
			}
			if (visuals_entitiestab.get(2).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.ANIMALS);
			}
			if (visuals_entitiestab.get(3).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.VILLAGERS);
			}
			if (visuals_entitiestab.get(4).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.FRIENDS);
			}
			if (visuals_entitiestab.get(5).chams.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.SELF);
			}
			CastHelper.EntityType type;
			if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null) {
				CheckBox chams = visuals_entitiestab.get(type.ordinal()).chams;
				if (chams.isEnabled()) {
					boolean rainbow = chams.getColor().isRainbow();
					float red = chams.getColor().getRed() / 255F;
					float green = chams.getColor().getGreen() / 255F;
					float blue = chams.getColor().getBlue() / 255F;
					float alpha = chams.getColor().getAlpha() / 255F;
					if (rainbow) {
						int c1 = PaletteHelper.rainbow(0.5F);
						red = ((c1 >> 16) & 255) / 255F;
						green = ((c1 >> 8) & 255) / 255F;
						blue = (c1 & 255) / 255F;
					}
					if (((EventRenderModelEntity) event).getType() == EventRenderModelEntity.Type.PRE) {
						GL11.glPushMatrix();
						GlStateManager.disableBlend();
						OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GlStateManager.color(red, green, blue, alpha);
					} else {
						GlStateManager.disableBlend();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glPopMatrix();
						GlStateManager.resetColor();
					}
				}
			}
		}
	}
}
