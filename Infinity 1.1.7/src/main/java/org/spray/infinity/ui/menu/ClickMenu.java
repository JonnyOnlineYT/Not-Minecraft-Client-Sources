package org.spray.infinity.ui.menu;

import org.spray.infinity.ui.menu.components.Panel;
import org.spray.infinity.utils.render.RenderUtil;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

/**
 * 
 * @author spray 
 * I'm idiot, it's such a shitty code
 *
 */
public class ClickMenu extends Screen {

	public Panel panel;
	private double anim;
	private float fade;

	public ClickMenu() {
		super(new LiteralText(""));
		panel = new Panel(this, 60, 20, 400, 250);
	}

	@Override
	public void init() {
		anim = 0.23;
		fade = -0.2f;
		panel.init();

		super.init();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);

		fade = (float) (RenderUtil.animate(fade, 1));

		anim = RenderUtil.animate(anim, 0);

		matrices.push();

		if (anim != 0) {
			matrices.translate(panel.x + 200, panel.y + 145, 0);
			matrices.scale((float) (1 + anim), (float) (1 + anim), (float) (1 + anim));
			matrices.translate(-panel.x - 200, -panel.y - 145, 0);
		}

		RenderSystem.setShaderColor(1f, 1f, 1f, fade);
		panel.render(matrices, mouseX, mouseY, delta);

		matrices.pop();

		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void tick() {
		panel.tick();
		super.tick();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		panel.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		panel.mouseReleased(mouseX, mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double amount) {
		panel.mouseScrolled(d, e, amount);
		return super.mouseScrolled(e, e, amount);
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		panel.charTyped(chr, keyCode);
		return super.charTyped(chr, keyCode);
	}

	@Override
	public void onClose() {
		panel.onClose();
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		panel.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

}
