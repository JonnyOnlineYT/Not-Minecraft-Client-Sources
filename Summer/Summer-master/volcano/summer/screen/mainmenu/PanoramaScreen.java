package volcano.summer.screen.mainmenu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.Colors;
import volcano.summer.client.util.R2DUtils;

public class PanoramaScreen extends GuiScreen {
	private static final ResourceLocation[] titlePanoramaPaths;
	protected static final ResourceLocation minecraftTitleTextures;
	private DynamicTexture viewportTexture;
	private ResourceLocation panoramaView;
	protected static int panoramaTimer;
	private static final ColorContainer c1;
	private static final ColorContainer c2;

	public PanoramaScreen() {
		this.viewportTexture = new DynamicTexture(256, 256);
	}

	@Override
	public void updateScreen() {
		++PanoramaScreen.panoramaTimer;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		++PanoramaScreen.panoramaTimer;
		Gui.drawRect(-1, -1, this.width + 3, this.height + 3, Colors.getColor(0, 40));
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		this.drawButtons(mouseX, mouseY);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		this.drawTitle();
	}

	protected void drawTitle() {
	}

	protected void drawButtons(final int mouseX, final int mouseY) {
		final int width = 150;
		final int hei = 26;
		final boolean override = false;
		for (int i = 0; i < this.buttonList.size(); ++i) {
			final GuiButton g = (GuiButton) this.buttonList.get(i);
			if (override) {
				final int x = g.xPosition;
				final int y = g.yPosition;
				final boolean over = mouseX >= x && mouseY >= y && mouseX < x + g.getButtonWidth() && mouseY < y + hei;
				if (over) {
					Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(5, 40, 85, 255),
							new ColorContainer(0, 0, 0, 0));
				} else {
					Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(0, 0, 0, 255),
							new ColorContainer(0, 0, 0, 0));
				}
				this.fontRendererObj.drawString(g.displayString, g.xPosition + 10, g.yPosition + hei / 2 - 3, -1);
			} else {
				g.drawButton(this.mc, mouseX, mouseY);
			}
		}
	}

	protected void renderSkybox(final int x, final int y, final float f) {
		final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		R2DUtils.rectangle(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), Colors.getColor(23, 23, 23));
	}

	protected void drawPanorama(final int x, final int y, final float partial) {
		final Tessellator tess = Tessellator.getInstance();
		final WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		final byte byt = 8;
		for (int j = 0; j < byt * byt; ++j) {
			GlStateManager.pushMatrix();
			final float var8 = (j % byt / byt - 0.5f) / 64.0f;
			final float var9 = (j / byt / byt - 0.5f) / 64.0f;
			final float var10 = 0.0f;
			GlStateManager.translate(var8, var9, var10);
			GlStateManager.rotate(MathHelper.sin((PanoramaScreen.panoramaTimer + partial) / 400.0f) * 25.0f + 20.0f,
					1.0f, 0.0f, 0.0f);
			GlStateManager.rotate(-(PanoramaScreen.panoramaTimer + partial) * 0.1f, 0.0f, 1.0f, 0.0f);
			for (int i = 0; i < 6; ++i) {
				GlStateManager.pushMatrix();
				if (i == 1) {
					GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
				}
				if (i == 2) {
					GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
				}
				if (i == 3) {
					GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
				}
				if (i == 4) {
					GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
				}
				if (i == 5) {
					GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
				}
				this.mc.getTextureManager().bindTexture(PanoramaScreen.titlePanoramaPaths[i]);
				wr.startDrawingQuads();
				wr.func_178974_a(16777215, 255 / (j + 1));
				final float var11 = 0.0f;
				wr.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var11, 0.0f + var11);
				wr.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var11, 0.0f + var11);
				wr.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var11, 1.0f - var11);
				wr.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var11, 1.0f - var11);
				tess.draw();
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}
		wr.setTranslation(0.0, 0.0, 0.0);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	protected void rotateAndBlurSkybox(final float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.panoramaView);
		GL11.glTexParameteri(3553, 10241, 9729);
		GL11.glTexParameteri(3553, 10240, 9729);
		GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		final Tessellator tess = Tessellator.getInstance();
		final WorldRenderer wr = tess.getWorldRenderer();
		wr.startDrawingQuads();
		GlStateManager.disableAlpha();
		final byte byt = 3;
		for (int i = 0; i < byt; ++i) {
			wr.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f / (i + 1));
			final int width = this.width;
			final int height = this.height;
			final float var8 = (i - byt / 2) / 256.0f;
			wr.addVertexWithUV(width, height, this.zLevel, 0.0f + var8, 1.0);
			wr.addVertexWithUV(width, 0.0, this.zLevel, 1.0f + var8, 1.0);
			wr.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
			wr.addVertexWithUV(0.0, height, this.zLevel, 0.0f + var8, 0.0);
		}
		tess.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	static {
		titlePanoramaPaths = new ResourceLocation[] {
				new ResourceLocation("textures/gui/title/background/panorama_0.png"),
				new ResourceLocation("textures/gui/title/background/panorama_1.png"),
				new ResourceLocation("textures/gui/title/background/panorama_2.png"),
				new ResourceLocation("textures/gui/title/background/panorama_3.png"),
				new ResourceLocation("textures/gui/title/background/panorama_4.png"),
				new ResourceLocation("textures/gui/title/background/panorama_5.png") };
		minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
		c1 = new ColorContainer(0, 0, 0, 100);
		c2 = new ColorContainer(0, 0, 0, 0);
	}
}
