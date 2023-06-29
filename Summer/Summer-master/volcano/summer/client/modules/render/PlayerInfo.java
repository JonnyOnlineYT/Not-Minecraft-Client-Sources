package volcano.summer.client.modules.render;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.modules.Helper;
import volcano.summer.client.util.MathUtil;

public class PlayerInfo extends Module {

	public PlayerInfo() {
		super("PlayerInfo", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			int renderWidth = Helper.windowWidth() / 2;
			int renderHeight = 15;
			int yPos = 6;
			int longest = 0;
			Entity entityOver = mc.objectMouseOver.entityHit;
			if ((entityOver instanceof EntityLivingBase) && (!entityOver.isInvisible())) {
				if (mc.objectMouseOver.entityHit == null) {
					return;
				}
				if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
					return;
				}
				EntityLivingBase ent = (EntityLivingBase) entityOver;
				String name = String
						.format("§7Name: §r%s",
								new Object[] { FriendManager.isFriend(ent.getName())
										? String.format("§b%s",
												new Object[] { FriendManager.getAliasName(ent.getName()) })
										: ent.getName() });
				String pos2 = String.format("§7XYZ: §r%s §7/ §r%s §7/ §r%s",
						new Object[] { Integer.valueOf((int) ent.posX), Integer.valueOf((int) ent.posY),
								Integer.valueOf((int) ent.posZ) });
				String health = String.format("§7HP: §r%s §7/ §r%s",
						new Object[] { Double.valueOf(MathUtil.roundHover(ent.getHealth(), 2)),
								Double.valueOf(MathUtil.roundHover(ent.getMaxHealth(), 2)) });
				if (longest < mc.fontRendererObj.getStringWidth(name)) {
					longest = mc.fontRendererObj.getStringWidth(name);
				}
				if (longest < mc.fontRendererObj.getStringWidth(pos2)) {
					longest = mc.fontRendererObj.getStringWidth(pos2);
				}
				Helper.drawBordered(renderWidth - (longest + 5) / 2, renderHeight, longest + 5, 85.0D, 1.0D,
						Helper.withTransparency(-1, 0.3F), -1);
				mc.fontRendererObj.func_175063_a(name, renderWidth - mc.fontRendererObj.getStringWidth(name) / 2,
						renderHeight + yPos, -1);
				yPos += 12;
				mc.fontRendererObj.func_175063_a(pos2, renderWidth - mc.fontRendererObj.getStringWidth(pos2) / 2,
						renderHeight + yPos, -1);
				yPos += 12;
				mc.fontRendererObj.func_175063_a(health, renderWidth - mc.fontRendererObj.getStringWidth(health) / 2,
						renderHeight + yPos, -1);
				yPos += 50;
				GuiInventory.drawEntityOnScreen(renderWidth, renderHeight + yPos, 20, -45.0F, 0.0F, ent);
				int i = 1;
				for (;;) {
					ItemStack ia = ent.getEquipmentInSlot(i);
					if (ia != null) {
						RenderHelper.enableStandardItemLighting();
						int width;
						int height = width = 0;
						if (i == 0) {
							width = Helper.windowWidth() / 2 - 9;
							height = Helper.windowHeight() - 52;
						} else if (i >= 3) {
							width = renderWidth - 30;
							height = renderHeight + yPos - (i == 4 ? 32 : 17);
							mc.getRenderItem().func_175042_a(ia, width, height);
							mc.getRenderItem().func_175030_a(mc.fontRendererObj, ia, width, height);
						} else {
							width = renderWidth + 14;
							height = renderHeight + yPos - (i == 2 ? 32 : 17);
							mc.getRenderItem().func_175042_a(ia, width, height);
							mc.getRenderItem().func_175030_a(mc.fontRendererObj, ia, width, height);
						}
						RenderHelper.disableStandardItemLighting();
					}
					i++;
					if (i >= 5) {
						break;
					}
				}
			}
		}
	}
}
