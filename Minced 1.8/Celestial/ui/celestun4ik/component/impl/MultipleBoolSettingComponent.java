package Celestial.ui.celestun4ik.component.impl;

import Celestial.Smertnix;
import Celestial.module.impl.Render.ClickGUI;
import Celestial.ui.celestun4ik.component.Component;
import Celestial.ui.celestun4ik.component.ExpandableComponent;
import Celestial.ui.celestun4ik.component.PropertyComponent;
import Celestial.ui.celestun4ik.panelcomponent;
import Celestial.ui.settings.Setting;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.MultipleBoolSetting;
import Celestial.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class MultipleBoolSettingComponent extends ExpandableComponent implements PropertyComponent {

	private final MultipleBoolSetting listSetting;
	Minecraft mc = Minecraft.getMinecraft();

	public MultipleBoolSettingComponent(Component parent, MultipleBoolSetting listSetting, int x, int y, int width,
										int height) {
		super(parent, listSetting.getName(), x, y, width, height);
		this.listSetting = listSetting;
	}

	@Override
	public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
		super.drawComponent(scaledResolution, mouseX, mouseY);
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
		int x = getX();
		int y = getY();
		int width = getWidth();
		int height = getHeight();
		int dropDownBoxY = y + 4;
		int textColor = 0xFFFFFF;
		if (ClickGUI.shadow.getCurrentValue()) {
			RenderUtils.drawBlurredShadow(x + 0.5F - 3, dropDownBoxY + 5, x + getWidth() - 0.5F - 2 - (x + 0.5F - 3), (int) (dropDownBoxY + 16) - (dropDownBoxY + 5), 5, new Color(25, 25, 25, 200));
		}
		Gui.drawRect(x + 0.5F - 3, dropDownBoxY + 5, x + getWidth() - 0.5F - 2, (int) (dropDownBoxY + 16), new Color(25, 25, 25, 200).getRGB());
		mc.mntsb_13.drawString(getName() + ":", x - 2, dropDownBoxY - 1, Smertnix.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
		if (isExpanded()) {
			if (ClickGUI.shadow.getCurrentValue()) {
				RenderUtils.drawBlurredShadow(x + panelcomponent.X_ITEM_OFFSET - 2, y + height, x + width - panelcomponent.X_ITEM_OFFSET - 3 - (x + panelcomponent.X_ITEM_OFFSET - 3), y + getHeightWithExpand() - ( y + height), 5, new Color(25,  25, 25,200));
			}
			Gui.drawRect(x + panelcomponent.X_ITEM_OFFSET - 2, y + height, x + width - panelcomponent.X_ITEM_OFFSET - 3, y + getHeightWithExpand(), new Color(25,  25, 25,200).getRGB());
		}

		handleRender(x, y + getHeight() + 2, width, textColor);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int button) {
		super.onMouseClick(mouseX, mouseY, button);
		if (isExpanded()) {
			handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
		}
	}

	private void handleRender(int x, int y, int width, int textColor) {
		int color = 0;
		ArrayList<BooleanSetting> a = new ArrayList();

		Color onecolor = new Color(ClickGUI.color.getColorValue());
		for (BooleanSetting e : listSetting.getBoolSettings()) {



			if (e.getCurrentValue()) {
				a.add(e);
			}


			if (isExpanded()) {
				if (e.getCurrentValue()) {
					mc.mntsb_14.drawCenteredString(e.getName(), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 2, y + 2.5F, ClickGUI.color.getColorValue());
				} else {
					mc.mntsb_14.drawCenteredString(e.getName(), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 2, y + 2.5F, Color.WHITE.getRGB());
				}}
			y += (panelcomponent.ITEM_HEIGHT - 3);
		}
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtils.scissorRect(0, 25.5f, x + panelcomponent.X_ITEM_OFFSET + width / 2 + 2, 239);
		mc.mntsb_14.drawString(a.get(0).getName() + (a.size()> 0 ? ", " + listSetting.getBoolSettings().get(1).getName()  : "Null"), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 48, getY() + 2.5F + 10, -1);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();

		mc.mntsb_14.drawString("...", x + panelcomponent.X_ITEM_OFFSET + width / 2 + 2, getY() + 2.5F + 10, -1);
	}

	private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
		for (BooleanSetting e : listSetting.getBoolSettings()) {
			if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + panelcomponent.ITEM_HEIGHT - 3) {
				e.setBoolValue(!e.getCurrentValue());
			}

			y += panelcomponent.ITEM_HEIGHT - 3;
		}
	}

	@Override
	public int getHeightWithExpand() {
		return getHeight() + listSetting.getBoolSettings().toArray().length * (panelcomponent.ITEM_HEIGHT - 3);
	}

	@Override
	public void onPress(int mouseX, int mouseY, int button) {
	}

	@Override
	public boolean canExpand() {
		return listSetting.getBoolSettings().toArray().length > 0;
	}

	@Override
	public Setting getSetting() {
		return listSetting;
	}
}