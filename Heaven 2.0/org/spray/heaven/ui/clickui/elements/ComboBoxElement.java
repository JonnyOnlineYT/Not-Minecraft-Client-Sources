package org.spray.heaven.ui.clickui.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.base.AbstractElement;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import java.awt.*;

public class ComboBoxElement extends AbstractElement {

	private boolean open;
	private double paddingY;
	private double wheight;

	private int counter;

	private final Animation rotation = new DecelerateAnimation(240, 1, Direction.FORWARDS);

	public ComboBoxElement(Setting setting) {
		super(setting);
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		super.render(mouseX, mouseY, delta);
		rotation.setDirection(open ? Direction.BACKWARDS : Direction.FORWARDS);
		
		ClickUIMod clickUI = Wrapper.getModule().get("ClickGUI");

		float tx = (float) (x + width - 7);
		float ty = (float) (y + (wheight / 2));

		float thetaRotation = (float) (-180f * rotation.getOutput());
		GlStateManager.pushMatrix();

		GlStateManager.translate(tx, ty, 0);
		GlStateManager.rotate(thetaRotation, 0, 0, 1);
		GlStateManager.translate(-tx, -ty, 0);

		Drawable.drawTexture(new ResourceLocation("heaven/textures/icons/clickui/up-arrow.png"), x + width - 10,
				y + (wheight - 6) / 2, 6, 6);
		GlStateManager.popMatrix();

		IFont.WEB_SETTINGS.drawString(setting.getName(), x + 3,
				y + wheight / 2 - (IFont.WEB_SETTINGS.getFontHeight() / 2), clickUI.textShadow.isToggle(), -1);
		IFont.WEB_SETTINGS.drawString(setting.getCurrentMode(),
				x + width - 16 - IFont.WEB_SETTINGS.getStringWidth(setting.getCurrentMode()),
				y + wheight / 2 - (IFont.WEB_SETTINGS.getFontHeight() / 2), clickUI.textShadow.isToggle(), -1);

		if (open) {
			Color color = ClickUIMod.getColor(0);
			double offsetY = 0;
			for (String mode : this.setting.getModes()) {
				IFont.WEB_SETTINGS_MINI.drawCenteredString(mode, x + width / 2,
						y + wheight + ((12 >> 1) - (IFont.WEB_SETTINGS_MINI.getFontHeight() / 2) - 1) + offsetY,
						setting.getCurrentMode().equalsIgnoreCase(mode) ? color.getRGB() : -1);

				offsetY += 12;
			}
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (Drawable.isHovered(mouseX, mouseY, x, y, width, wheight)) {
			if (button == 0) {
				if (counter + 1 > setting.getModes().size() - 1)
					counter = 0;
				else
					counter++;

				setting.setCurrentMode(setting.getModes().get(counter));
			} else
				open = !open;
		}

		if (open) {
			double offsetY = 0;
			for (String mode : this.setting.getModes()) {

				if (Drawable.isHovered(mouseX, mouseY, x, y + wheight + offsetY, width, 12) && button == 0)
					setting.setCurrentMode(mode);

				offsetY += 12;
			}
		}
	}

	@Override
	public void resetAnimation() {

	}

	public void setWHeight(double height) {
		this.wheight = height;
	}

	public boolean isOpen() {
		return open;
	}

}
