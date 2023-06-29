package volcano.summer.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public enum LogoUtil {

	INSTANCE;

	public void drawImage(ResourceLocation location, Vec2f pos, int width, int height) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
		Gui.INSTANCE.drawModalRectWithCustomSizedTexture((int) pos.getX(), (int) pos.getY(), 0, 0, width, height, width,
				height);
		GlStateManager.popMatrix();

	}

	public ResourceLocation getAssset(String name) {
		return new ResourceLocation("textures/menu/" + name + ".png");
	}

	public ResourceLocation getAsssetJpg(String name) {
		return new ResourceLocation("textures/menu/" + name + ".jpg");
	}
}
