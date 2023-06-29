package volcano.summer.client.modules.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.util.R2DUtils;

public class SpawnerESP extends Module {

	private boolean nextTick;

	public SpawnerESP() {
		super("SpawnerESP", 0, Category.RENDER);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {
	}

	public void renderNameTag(String type, double pX, double pY, double pZ) {
		FontRenderer var12 = this.mc.fontRendererObj;
		float scale = (float) (this.mc.thePlayer.getDistance(pX + RenderManager.renderPosX,
				pY + RenderManager.renderPosY, pZ + RenderManager.renderPosZ) / 8.0D);
		if (scale < 1.0F) {
			scale = 1.0F;
		}
		RenderManager renderManager = mc.getRenderManager();
		scale /= 50.0F;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		Tessellator var14 = Tessellator.instance;
		int width = this.mc.fontRendererObj.getStringWidth(type) / 2;
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		R2DUtils.drawBorderedRectTag(-width - 2, -(this.mc.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2.0F, 1.0F,
				-16777216, 1593835520);
		var12.func_175063_a(type, -width, -(this.mc.fontRendererObj.FONT_HEIGHT - 1), -65536);
		GL11.glPushMatrix();
		GL11.glPopMatrix();
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventRender3D)) {
			for (Object obj : this.mc.theWorld.loadedTileEntityList) {
				TileEntity tileEntity = (TileEntity) obj;
				if ((tileEntity instanceof TileEntityMobSpawner)) {
					TileEntityMobSpawner spawner = (TileEntityMobSpawner) tileEntity;
					String entityName = spawner.getSpawnerBaseLogic().getEntityNameToSpawn();
					renderNameTag(entityName + " Spawner", spawner.pos.getX() + 0.5D - RenderManager.renderPosX,
							spawner.pos.getY() + 0.5D - RenderManager.renderPosY,
							spawner.pos.getZ() + 0.5D - RenderManager.renderPosZ);
				}
			}
		}
	}
}
