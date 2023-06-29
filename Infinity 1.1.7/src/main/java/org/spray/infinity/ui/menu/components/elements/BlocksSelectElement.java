package org.spray.infinity.ui.menu.components.elements;

import java.awt.Color;

import org.spray.infinity.features.Setting;
import org.spray.infinity.ui.menu.components.base.AbstractElement;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;

import net.minecraft.block.Block;
import net.minecraft.client.util.math.MatrixStack;

public class BlocksSelectElement extends AbstractElement {

	public BlocksSelectElement(Setting setting) {
		super(setting);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int xOffset = 3;
		int yOffset = 0;
		for (Block blocks : setting.getRenderBlocks()) {
			this.height = yOffset;
			boolean hover = Drawable.isHovered(mouseX, mouseY, xOffset + x, yOffset + y, 21, 19);
			boolean isAdded = setting.getBlocks().contains(blocks);
			Drawable.drawRectWH(matrices, xOffset + x - 1, yOffset + y + 3, 21, 19,
					isAdded ? 0xFF30639F : hover ? Color.GRAY.getRGB() : 0x70000000);
			Helper.MC.getItemRenderer().renderGuiItemIcon(blocks.asItem().getDefaultStack(),
					(int) ((int) xOffset + x + 2), (int) ((int) yOffset + y + 5));
			xOffset += 23;
			if (xOffset > 140) {
				xOffset = 3;
				yOffset += 21;
			}
		}
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		int xOffset = 3;
		int yOffset = 0;
		for (Block blocks : setting.getRenderBlocks()) {
			if (Drawable.isHovered(mouseX, mouseY, xOffset + x, yOffset + y, 21, 19) && button == 0) {
				if (setting.getBlocks().contains(blocks))
					setting.getBlocks().remove(blocks);
				else
					setting.getBlocks().add(blocks);
			}

			xOffset += 23;
			if (xOffset > 140) {
				xOffset = 3;
				yOffset += 21;
			}
		}
	}

	@Override
	public boolean isVisible() {
		return this.setting.isVisible();
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseScrolled(double d, double e, double amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO Auto-generated method stub

	}

}
