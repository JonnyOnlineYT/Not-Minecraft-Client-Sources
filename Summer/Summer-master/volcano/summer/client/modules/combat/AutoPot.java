package volcano.summer.client.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class AutoPot extends Module {

	public static TimerUtil timer;
	public static int haltTicks;
	public static boolean potting;
	private boolean send;
	public static Value<Boolean> silent;
	public static Value<Boolean> jumppot;
	public static Value<Double> health;
	public static Value<Double> delay;

	public AutoPot() {
		super("AutoPot", 0, Category.COMBAT);
		this.timer = new TimerUtil();
		silent = new Value("Silent", "silent", Boolean.valueOf(false), this);
		jumppot = new Value("JumpPot", "jumppot", Boolean.valueOf(true), this);
		health = new ClampedValue<Double>("Health", "health", 5.0, 1.0, 10.0, this);
		delay = new ClampedValue<Double>("Delay", "delay", 350.0, 50.0, 1000.0, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static boolean isMoving() {
		return !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking()
				&& (mc.thePlayer.movementInput.moveForward != 0.0f || mc.thePlayer.movementInput.moveStrafe != 0.0f);
	}

	protected void swap(final int slot, final int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	public static int getPotionFromInv() {
		int pot = -1;
		for (int i = 0; i < 45; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (item instanceof ItemPotion) {
					final ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (final Object o : potion.getEffects(is)) {
							final PotionEffect effect = (PotionEffect) o;
							if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
								pot = i;
							}
						}
					}
				}
			}
		}
		return pot;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			final EventPreMotionUpdate e = (EventPreMotionUpdate) event;
			if (potting && haltTicks < 0) {
				potting = false;
			}
			float health = this.health.getValue().floatValue() * 2.0f;
			if (health < 3.0f && mc.thePlayer.getEquipmentInSlot(4) == null && this.jumppot.getValue()) {
				health += 2.0f;
			}
			final float delay = this.delay.getValue().floatValue();
			if (isMoving()) {
				if (mc.thePlayer.getHealth() <= health && getPotionFromInv() != -1 && this.timer.delay(delay)) {
					this.swap(getPotionFromInv(), 6);
					e.setPitch(120.0f);
					potting = true;
					this.timer.reset();
				}
			} else if (mc.thePlayer.getHealth() <= health && getPotionFromInv() != -1 && this.timer.delay(delay)
					&& haltTicks < 0 && mc.thePlayer.isCollidedVertically) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, -90.0f, true));
				this.swap(getPotionFromInv(), 6);
				mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
				mc.getNetHandler()
						.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
				mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				haltTicks = 5;
				mc.thePlayer.jump();
				potting = true;
			}
			--haltTicks;
		}
		if (event instanceof EventPostMotionUpdate && potting) {
			if (isMoving()) {
				mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
				mc.getNetHandler()
						.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
				mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				if (this.silent.getValue()) {
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw,
							mc.thePlayer.rotationPitch, true));
				}
			}
			this.timer.reset();

		}
		if (event instanceof EventPacketSend && this.silent.getValue()) {
			final EventPacketSend ep = (EventPacketSend) event;
			if (ep.getPacket() instanceof C03PacketPlayer && !potting && this.send) {
				this.send = false;
				return;
			}
			if (potting && ep.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && !this.send) {
				final C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook) ep.getPacket();
				ep.setCancelled(true);
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(
						mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
						new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 120.0f, true));
				this.send = true;
			}
		}
	}
}