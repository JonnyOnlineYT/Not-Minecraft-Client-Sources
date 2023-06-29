package volcano.summer.screen.clickgui.sektor;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.modules.render.ClickGui;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Click extends GuiScreen {
	public ArrayList<Frame> frames = new ArrayList();
	private float scale = 1.0f;
	private DynamicTexture dyn;
	private ResourceLocation rsc;

	public static String capitalize(String line) {
		return String.valueOf(String.valueOf(Character.toUpperCase(line.charAt(0)))) + line.substring(1);
	}

	public Click() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		Module.Category[] arrcategory = Module.Category.values();
		int n = arrcategory.length;
		int n2 = 0;
		while (n2 < n) {
			Module.Category cat = arrcategory[n2];
			String catName = Click.capitalize(cat.toString());
			Frame modFrame = new Frame(catName);
			ArrayList<Module> mods = Summer.moduleManager.getModsInCategory(cat);
			for (final Module mod : mods) {
				Button button = new Button(mod.getName(), mod.getName(), modFrame) {

					@Override
					public void onPressed() {
						mod.toggleMod();
						this.enabled = !this.enabled;
					}

					@Override
					public void onUpdate() {
						this.enabled = mod.getState();
					}
				};
				for (Value p : Summer.valueManager.getModValues(mod)) {
					button.addExtendedComponent(this.getSettingComponent(p, button));
				}
				button.addExtendedComponent(new Keybinder(mod, button));
				modFrame.addItem(button);
			}
			this.frames.add(modFrame);
			++n2;
		}
		this.layoutFrames(sr);
	}

	public Component getSettingComponent(Value property, Component parent) {
		if (property.getValue() instanceof Boolean) {
			final Value booleanProperty = property;
			CheckButton modCheckBox = new CheckButton(Click.capitalize(property.getCommandName()), parent) {

				@Override
				public void onPressed() {
					booleanProperty.setValue((Boolean) booleanProperty.getValue() == false);
				}

				@Override
				public void onUpdate() {
					this.selected = (Boolean) booleanProperty.getValue();
				}
			};
			return modCheckBox;
		}
		if (property instanceof ClampedValue) {
			final ClampedValue numberProperty = (ClampedValue) property;
			Slider modSlider = new Slider(Click.capitalize(numberProperty.getCommandName()),
					((Number) numberProperty.getValue()).doubleValue(),
					((Number) numberProperty.getMin()).doubleValue(), ((Number) numberProperty.getMax()).doubleValue(),
					parent) {

				@Override
				public void onUpdate(double currentValue) {
					numberProperty.setValue(currentValue);
				}
			};
			return modSlider;
		}
		if (property instanceof ModeValue) {
			final ModeValue enumProperty = (ModeValue) property;
			Button modButton = new Button(
					String.valueOf(Click.capitalize(enumProperty.getCommandName())) + ":"
							+ enumProperty.getValue().toString(),
					String.valueOf(Click.capitalize(enumProperty.getCommandName())) + "-" + enumProperty.getModule(),
					parent) {

				@Override
				public void onPressed() {
					enumProperty.next();
				}

				@Override
				public void onUpdate() {
					this.enabled = false;
					this.title = String.valueOf(Click.capitalize(enumProperty.getCommandName())) + ":"
							+ enumProperty.getStringValue();
				}
			};
			modButton.hasValue = true;
			return modButton;
		}
		return null;
	}

	private void layoutFrames(ScaledResolution sr) {
		int x = 10;
		int y = 10;
		int h = 15;
		int w = 100;
		for (Frame frame : this.frames) {
			if (frame.getX() != 0 && frame.getY() != 0)
				continue;
			if (x + w > sr.getScaledWidth()) {
				x = 10;
				y += h + 5;
			}
			frame.setX(x);
			frame.setY(y);
			x += w + 10;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
if(ClickGui.blur.getValue()) {
	this.mc.entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[18]);
}
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.pushMatrix();
		for (Frame frame : this.frames) {
			frame.draw(mouseX, mouseY, partialTicks, 0, 0);
		}
		GlStateManager.popMatrix();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Frame frame : this.frames) {
			frame.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : this.frames) {
			frame.mouseReleased(mouseX, mouseY, state);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (Frame frame : this.frames) {
			frame.keyPressed(keyCode);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public float getScale() {
		return this.scale;
	}

}
