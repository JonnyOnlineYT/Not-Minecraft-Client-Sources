package volcano.summer.client.modules.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.util.R3DUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class ChestESP extends Module {

	public final Value<Boolean> chest = new Value("Chests", "chest", Boolean.valueOf(true), this);
	public static Value<Boolean> enderchest;
	public final Value<Boolean> tags = new Value("Tags", "tags", Boolean.valueOf(false), this);
	public final Value<Boolean> lines = new Value("Lines", "lines", Boolean.valueOf(false), this);
	public static Value<Double> width;
	public static ModeValue chestespMode;

	public ChestESP() {
		super("ChestESP", 0, Category.RENDER);
		width = new ClampedValue<Double>("Width", "width", 1.8, 1.0, 5.0, this);
		chestespMode = new ModeValue("CESPMode", "Mode", "Outline", new String[] { "Outline", "Box" }, this);
		enderchest = new Value("Enderchest", "enderchest", false, this);

	}

	@Override
	public void onEnable() {
		if (this.chestespMode.getStringValue().equalsIgnoreCase("Outline")) {
			Summer.tellPlayer("Please Turn 'FastRender' Off.");
		}
	}

	@Override
	public void onDisable() {
	}

	public static void enableGL3D() {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4353);
		GL11.glDisable(2896);
	}

	public static void disableGL3D() {
		GL11.glEnable(2896);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
	}

	private boolean shouldDraw(TileEntity tileEntity) {
		return (((tileEntity instanceof TileEntityChest)) && (this.chest.getValue()))
				|| (((tileEntity instanceof TileEntityEnderChest)) && (this.enderchest.getValue()));
	}

	private float[] getColor(TileEntity tileEntity) {
		if ((tileEntity instanceof TileEntityChest)) {
			Block block = tileEntity.getBlockType();
			if (block == Blocks.chest) {
				return new float[] { 0.8F, 0.7F, 0.22F };
			}
			if (block == Blocks.trapped_chest) {
				return new float[] { 0.8F, 0.22F, 0.22F };
			}
		}
		if ((tileEntity instanceof TileEntityEnderChest)) {
			return new float[] { 1.0F, 0.0F, 1.0F };
		}
		return new float[] { 1.0F, 1.0F, 1.0F };
	}

	@Override
	public void onEvent(Event event) {
		if (this.chestespMode.getStringValue().equalsIgnoreCase("Box")) {
			setDisplayName("Box");
			if (event instanceof EventRender3D) {
				GlStateManager.pushMatrix();
				this.enableGL3D();
				for (Object object : this.mc.theWorld.loadedTileEntityList) {
					TileEntity tileEntity = (TileEntity) object;
					if (this.shouldDraw(tileEntity)) {
						double x = tileEntity.getPos().getX() - this.mc.getRenderManager().renderPosX;
						double y = tileEntity.getPos().getY() - this.mc.getRenderManager().renderPosY;
						double z = tileEntity.getPos().getZ() - this.mc.getRenderManager().renderPosZ;

						float[] color = this.getColor(tileEntity);
						AxisAlignedBB box = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
						if ((tileEntity instanceof TileEntityChest)) {
							TileEntityChest chest = TileEntityChest.class.cast(tileEntity);
							if (chest.adjacentChestZPos != null) {
								box = new AxisAlignedBB(x + 0.0625D, y, z + 0.0625D, x + 0.9375D, y + 0.875D,
										z + 1.9375D);
							} else if (chest.adjacentChestXPos != null) {
								box = new AxisAlignedBB(x + 0.0625D, y, z + 0.0625D, x + 1.9375D, y + 0.875D,
										z + 0.9375D);
							} else {
								if ((chest.adjacentChestZPos != null) || (chest.adjacentChestXPos != null)
										|| (chest.adjacentChestZNeg != null) || (chest.adjacentChestXNeg != null)) {
									continue;
								}
								box = new AxisAlignedBB(x + 0.0625D, y, z + 0.0625D, x + 0.9375D, y + 0.875D,
										z + 0.9375D);
							}
						} else if ((tileEntity instanceof TileEntityEnderChest)) {
							box = new AxisAlignedBB(x + 0.0625D, y, z + 0.0625D, x + 0.9375D, y + 0.875D, z + 0.9375D);
						}
						GlStateManager.color(color[0], color[1], color[2], 0.45F);
						boolean bobbing = this.mc.gameSettings.viewBobbing;
						if (this.lines.getValue()) {
							GlStateManager.pushMatrix();
							GL11.glLineWidth(this.width.getValue().floatValue());
							GL11.glLoadIdentity();
							this.mc.gameSettings.viewBobbing = false;
							this.mc.entityRenderer.orientCamera(((EventRender3D) event).getPartialTicks());
							GL11.glBegin(1);
							GL11.glVertex3d(0.0D, this.mc.thePlayer.getEyeHeight(), 0.0D);
							GL11.glVertex3d(x + 0.5D, y, z + 0.5D);
							GL11.glEnd();
							GlStateManager.popMatrix();
						}
						R3DUtils.drawBox(box);
						R3DUtils.renderCrosses(box);
						R3DUtils.drawOutlinedBox(box);

						this.mc.gameSettings.viewBobbing = bobbing;
					}
				}
				for (Object object : this.mc.theWorld.loadedTileEntityList) {
					TileEntity tileEntity = (TileEntity) object;
					if (this.shouldDraw(tileEntity)) {
						double x = tileEntity.getPos().getX() + 0.5D - this.mc.getRenderManager().renderPosX;
						double y = tileEntity.getPos().getY() - 1.0D - this.mc.getRenderManager().renderPosY;
						double z = tileEntity.getPos().getZ() + 0.5D - this.mc.getRenderManager().renderPosZ;
						if (this.tags.getValue()) {
							GlStateManager.pushMatrix();
							this.renderTileEntityNameTag(tileEntity, x, y, z);
							GlStateManager.popMatrix();
						}
					}
				}
				this.disableGL3D();
				GlStateManager.popMatrix();
			}
		}
		if (this.chestespMode.getStringValue().equalsIgnoreCase("Outline")) {
			setDisplayName("Outline");
		}
	}

	private void renderTileEntityNameTag(TileEntity tileEntity, double x, double y, double z) {
		double tempY = y;
		tempY += 0.7D;

		double distance = this.mc.func_175606_aa().getDistance(x + this.mc.getRenderManager().viewerPosX,
				y + this.mc.getRenderManager().viewerPosY, z + this.mc.getRenderManager().viewerPosZ);
		int width = this.mc.fontRendererObj.getStringWidth(getDisplayName(tileEntity)) / 2 + 1;
		// scale
		double scale = 0.0018D + 0.003 * distance;
		if (distance <= 8.0D) {
			scale = 0.0245D;
		}
		GlStateManager.pushMatrix();
		GlStateManager.enablePolygonOffset();
		GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
		GlStateManager.disableLighting();
		GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
		GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(this.mc.getRenderManager().playerViewX,
				this.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);

		GlStateManager.scale(-scale, -scale, scale);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();

		GlStateManager.disableAlpha();
		R2DUtils.drawBorderedRectReliant(-width - 2, -(this.mc.fontRendererObj.FONT_HEIGHT + 1), width, 1.5F, 1.6F,
				1996488704, 1426063360);
		GlStateManager.enableAlpha();

		this.mc.fontRendererObj.func_175063_a(getDisplayName(tileEntity), -width,
				-(this.mc.fontRendererObj.FONT_HEIGHT - 1), -1);

		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.disablePolygonOffset();
		GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
		GlStateManager.popMatrix();
	}

	private String getDisplayName(TileEntity tileEntity) {
		if ((tileEntity instanceof TileEntityChest)) {
			Block block = tileEntity.getBlockType();
			if (block == Blocks.chest) {
				return "Chest";
			}
			if (block == Blocks.trapped_chest) {
				return "Trapped Chest";
			}
		}
		if ((tileEntity instanceof TileEntityEnderChest)) {
			return "EnderChest";
		}
		return "Unknown";
	}
}