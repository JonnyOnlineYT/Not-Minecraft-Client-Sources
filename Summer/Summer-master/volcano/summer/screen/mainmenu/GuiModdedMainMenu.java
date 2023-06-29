package volcano.summer.screen.mainmenu;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import volcano.summer.screen.altmanager.GuiAltManager;
import volcano.summer.screen.changelog.GuiChanges;
import volcano.summer.screen.particals.ParticleManager;

public class GuiModdedMainMenu extends ClientMainMenu {
	private ParticleManager particles;
	private ResourceLocation text;
	private ResourceLocation background;

	public GuiModdedMainMenu() {
		this.particles = new ParticleManager();
		this.text = new ResourceLocation("textures/menu/Summer.png");
		this.background = new ResourceLocation("textures/menu/SummerBackGround.png");
	}

	@Override
	public void initGui() {
		super.initGui();
		final String strSSP = I18n.format("Singleplayer", new Object[0]);
		final String strSMP = I18n.format("Multiplayer", new Object[0]);
		final String strOptions = I18n.format("Options", new Object[0]);
		final String strQuit = I18n.format("Quit", new Object[0]);
		final String strLang = I18n.format("Language", new Object[0]);
		final String strChangeLog = I18n.format("ChangeLog", new Object[0]);
		final String strAccounts = "Accounts";
		final int initHeight = this.height / 4 + 48;
		final int objHeight = 17;
		final int objWidth = 63;
		final int xMid = this.width / 2 - 75;
		this.buttonList.add(new GuiMenuButton(0, xMid - 150, initHeight, objWidth, objHeight, strSSP));
		this.buttonList.add(new GuiMenuButton(1, xMid - 50, initHeight, objWidth, objHeight, strSMP));
		this.buttonList.add(new GuiMenuButton(2, xMid + 50, initHeight, objWidth, objHeight, strOptions));
		this.buttonList.add(new GuiMenuButton(3, xMid + 150, initHeight, objWidth, objHeight, strLang));
		this.buttonList.add(new GuiMenuButton(4, xMid - 100, initHeight + 100, objWidth, objHeight, strAccounts));
		this.buttonList.add(new GuiMenuButton(5, xMid + 100, initHeight + 100, objWidth, objHeight, strQuit));
		this.buttonList.add(new GuiMenuButton(10, xMid + 4, initHeight + 100, objWidth, objHeight, strChangeLog));
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		} else if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		} else if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		} else if (button.id == 3) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		} else if (button.id == 4) {
			this.mc.displayGuiScreen(new GuiAltManager());
		} else if (button.id == 10) {
			this.mc.displayGuiScreen(new GuiChanges());
		} else if (button.id == 5) {
			this.mc.shutdown();
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		final int w = sr.getScaledWidth();
		final int h = sr.getScaledHeight();
		this.mc.getTextureManager().bindTexture(this.background);
		Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, w + 2, h, w + 2, h, w + 2, h);
		GlStateManager.func_179144_i(0);
		GlStateManager.enableBlend();
		float scale = 2.3F;
		GL11.glScalef(scale, scale, scale);
		this.mc.getTextureManager().bindTexture(this.text);
		Gui.drawModalRectWithCustomSizedTexture(w / 2 - 345, this.height / 7 - 45, 0.0f, 0.0f, 150, 48, 150.0f, 48.0f);
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
		GlStateManager.disableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);

	}
}