package org.spray.heaven.features.module.modules.combat;
import java.util.Timer;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoTotem", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)

public class AutoTotem extends Module {
	Timer timer = new Timer();
	public Setting CheckHealth = register(new Setting("CheckHealth Esp", false));
	public Setting OnHealth = register(new Setting("OnHealth", 5, 1, 20));
	public Setting CheckFallDist = register(new Setting("CheckFallDist", false));
	public Setting FallDist = register(new Setting("FallDist", 5, 1, 20));
	public Setting CheckCrystal = register(new Setting("CheckCrystal", false));
	public Setting CrystalDist = register(new Setting("CrystalDist", 5, 1, 20));
	public Setting CheckTnt = register(new Setting("CheckTnt", false));
	public Setting TntDist = register(new Setting("TntDist", 5, 1, 20));
	public Setting CheckCreeper = register(new Setting("CheckCreeper", false));
	public Setting CreeperDist = register(new Setting("CreeperDist", 5, 1, 20));
	public Setting SwapBackSlot = register(new Setting("SwapBackSlot", false));
	private Item oldSlot = null;

	@EventTarget
	public void onUpdate(UpdateEvent updateEvent) {
		boolean go = false;
		go = CheckFallDist.isToggle() && mc.player.fallDistance > FallDist.getValue();
		for (Entity entity : mc.world.loadedEntityList) {
			if ((entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= CrystalDist.getValue()) && CheckCrystal.isToggle()) {
				go = true;
			}
			if ((entity instanceof EntityMinecartTNT && mc.player.getDistanceToEntity(entity) <= TntDist.getValue()) && CheckTnt.isToggle()) {
				go = true;
			}
			if ((entity instanceof EntityCreeper && mc.player.getDistanceToEntity(entity) <= CreeperDist.getValue()) && CheckCreeper.isToggle()) {
				go = true;
			}
		}
		for (Slot slot : mc.player.inventoryContainer.inventorySlots) {
			if (!(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir) && mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
				oldSlot = mc.player.inventoryContainer.getSlot(45).getStack().getItem();
			}
			if (Item.getIdFromItem(slot.getStack().getItem()) == 449) {
				if (go && Item.getIdFromItem(mc.player.inventoryContainer.getSlot(45).getStack().getItem()) != 449) {
					if (mc.player.ticksExisted % 2 == 0) {
						mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
						mc.playerController.windowClick(0, totem(), 0, ClickType.PICKUP, mc.player);
						mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
					}
				}
				if (CheckHealth.isToggle() && mc.player.getHealth() < OnHealth.getValue()) {
					go = true;
				}
				if (!go && Item.getIdFromItem(mc.player.inventoryContainer.getSlot(45).getStack().getItem()) == 449) {
					if (SwapBackSlot.isToggle()) {
						if (mc.player.inventoryContainer.getSlot(45).getStack().getItem() != null && mc.player.inventoryContainer.getSlot(45).getStack().getItem() != oldSlot) {
							if (!(mc.player.ticksExisted % 19 == 0) && (CheckFallDist.isToggle() || CheckTnt.isToggle() || CheckCrystal.isToggle() || CheckCreeper.isToggle()))
								return;
							if (mc.player.ticksExisted % 2 == 0) {
								mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
								mc.playerController.windowClick(0, oldSlot(), 0, ClickType.PICKUP, mc.player);
								mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
							}
						}
					}
				}
			}
		}
	}

	public int totem() {
		for (int i = 0; i < 45; ++i) {
			final ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
			if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
				return i;
			}
		}
		return -1;
	}

	public int oldSlot() {
		for (int i = 0; i < 45; ++i) {
			final ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
			if (itemStack.getItem() == oldSlot) {
				return i;
			}
		}
		return -1;
	}

	@EventTarget
	public void onRender2D() {
		int υσι = 0;
		if (mc.player.getHeldItemOffhand().getItem().equals(Item.getItemById(449))) {
			υσι = mc.player.getHeldItemOffhand().stackSize;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef(533, 317, 0.0f);
		if (υσι != 0) {
			this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Item.getItemById(449)), -40, -55);
			mc.fontRendererObj.drawString(String.valueOf(υσι), -(int)24f, -(int)46.0f, -1);
		}
		GL11.glPopMatrix();
	}
}