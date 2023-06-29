package volcano.summer.client.modules.misc;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.EntityUtils;
import volcano.summer.client.value.Value;

public class Murder extends Module {

	private EntityPlayer murderer;
	public static Value<Boolean> telleveryone;

	public Murder() {
		super("Murder", 0, Category.MISC);
		telleveryone = new Value("TellEveryone", "telleveryone", Boolean.valueOf(false), this);
	}

	@Override
	public void onEnable() {
		this.murderer = null;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if (murderer == null) {
				for (Object o : this.mc.theWorld.loadedEntityList) {
					Entity e = (Entity) o;
					if (((e instanceof EntityPlayer)) && (((EntityPlayer) e).getCurrentEquippedItem() != null)
							&& ((((EntityPlayer) e).getCurrentEquippedItem().getItem() instanceof ItemSword))) {
						if (this.telleveryone.getValue()) {
							Summer.tellPlayer("§4Be Careful! " + e.getName() + " is the murderer!");
							this.mc.thePlayer.sendChatMessage("Be Careful! " + e.getName() + " is the murderer!");
						} else {
							Summer.tellPlayer("§4Be Careful! " + e.getName() + " is the murderer!");
						}
						murderer = (EntityPlayer) e;
					}
				}
			}
		}
		if (event instanceof EventRender3D) {
			if (this.murderer != null) {
				final double x = EntityUtils.interpolate(this.murderer.posX, this.murderer.lastTickPosX);
				final double y = EntityUtils.interpolate(this.murderer.posY, this.murderer.lastTickPosY);
				final double z = EntityUtils.interpolate(this.murderer.posZ, this.murderer.lastTickPosZ);
				AxisAlignedBB aabb = new AxisAlignedBB(x - this.murderer.width / 2.0f, y,
						z + this.murderer.width / 2.0f, x + this.murderer.width / 2.0f,
						y + this.murderer.getEyeHeight(), z - this.murderer.width / 2.0f);
				GL11.glPushMatrix();
				EntityUtils.pre();
				aabb = aabb.offset(-mc.getRenderManager().renderPosX, -mc.getRenderManager().renderPosY,
						-mc.getRenderManager().renderPosZ);
				GL11.glLineWidth(1.0f);
				GL11.glEnable(2848);
				GL11.glColor3d(1.0, 1.0, 0.0);
				RenderGlobal.drawSelectionBoundingBox(aabb);
				GL11.glDisable(2848);
				EntityUtils.post();
				GL11.glPopMatrix();
			}
			for (final Object o : mc.theWorld.loadedEntityList) {
				Entity ent = (Entity) o;
				if (!(ent instanceof EntityItem)) {
					continue;
				}
				final EntityItem item = (EntityItem) ent;
				if (item.getEntityItem() == null) {
					continue;
				}
				if (item.getEntityItem().getItem() != Items.gold_ingot) {
					continue;
				}
				final double x = EntityUtils.interpolate(ent.posX, ent.lastTickPosX);
				final double y = EntityUtils.interpolate(ent.posY, ent.lastTickPosY);
				final double z = EntityUtils.interpolate(ent.posZ, ent.lastTickPosZ);
				AxisAlignedBB aabb2 = new AxisAlignedBB(x - ent.width / 2.0f, y, z + ent.width / 2.0f,
						x + ent.width / 2.0f, y + ent.getEyeHeight(), z - ent.width / 2.0f);
				GL11.glPushMatrix();
				EntityUtils.pre();
				aabb2 = aabb2.offset(-mc.getRenderManager().renderPosX, -mc.getRenderManager().renderPosY,
						-mc.getRenderManager().renderPosZ);
				GL11.glLineWidth(1.0f);
				GL11.glEnable(2848);
				GL11.glColor3d(1.0, 1.0, 0.0);
				RenderGlobal.drawSelectionBoundingBox(aabb2);
				GL11.glDisable(2848);
				EntityUtils.post();
				GL11.glPopMatrix();
			}
		}
	}
}