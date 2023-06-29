package volcano.summer.client.modules.player;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.events.EventUpdate;

public class AutoEat extends Module {

	/**
	 * Contains all the items the player is not allowed to eat, but can eat.
	 **/
	private List<Item> invalidFoodItems;

	/**
	 * Contains all the effects that are considered bad for the player.
	 **/
	private List<Integer> invalidPotionEffects;

	private int ticks;

	public AutoEat() {
		super("AutoEat", Keyboard.KEY_NONE, Category.PLAYER);
		invalidFoodItems = Arrays.asList(Items.rotten_flesh);
		invalidPotionEffects = Arrays.asList(Potion.poison.getId(), Potion.moveSlowdown.getId(),
				Potion.digSlowdown.getId(), Potion.harm.getId(), Potion.confusion.getId(), Potion.hunger.getId(),
				Potion.weakness.getId(), Potion.poison.getId(), Potion.wither.getId());
	}

	@Override
	public void onEnable() {
		this.ticks = 0;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventUpdate) {
			if (mc.currentScreen != null)
				return;

			/**
			 * Checks if the player is can eat or the player has bad potion
			 * effects.
			 **/
			if ((mc.thePlayer.getFoodStats().getFoodLevel() <= 17 || hasBadPotionEffect()) && ticks == 0) {

				/**
				 * Gets the slot to switch to depending on if you have the
				 * clearEffects options set to true and you have a bad potion
				 * effect.
				 **/
				int slotToSwitch = getBestItemSlot(hasBadPotionEffect());

				/**
				 * If the user has no item to eat it stops the rest of the code
				 * from running.
				 **/
				if (slotToSwitch == -1)
					return;

				/**
				 * Sets the number of ticks till you can eat again to 32, since
				 * it takes 32 ticks to eat an food item in minecraft.
				 **/
				ticks = 32;

				/** Switches to the slot with food. **/
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slotToSwitch));

				/**
				 * Sends a BlockPlacement Packet so the server knows you are
				 * eating food.
				 **/
				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
						mc.thePlayer.inventoryContainer.getSlot(slotToSwitch + 36).getStack()));
			}
			/** Checks if the user is not eating. **/
			if (ticks == 0) {

				/** Switches back to your old slot. **/
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			}
		}
		if (e instanceof EventTick) {
			if (this.ticks > 0)
				this.ticks--;
		}
	}

	/**
	 * Goes through the players hotbar and checks if the player has an food item
	 * or an milk bucket.
	 */
	private int getBestItemSlot(boolean checkForMilkBucket) {
		int slot = -1;
		int max = -1;
		for (int i = 36; i < 45; ++i) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack == null)
				continue;
			if (stack.getItem().equals(Items.milk_bucket) && checkForMilkBucket) {
				return i - 36;
			}
			if (stack.getItem() instanceof ItemFood && !invalidFoodItems.contains(stack.getItem())) {
				ItemFood food = (ItemFood) stack.getItem();
				if (food.getHealAmount(stack) >= max) {
					max = food.getHealAmount(stack);
					slot = i - 36;
				}
			}
		}
		return slot;
	}

	/**
	 * Goes through all the potion effects the player has, and checks if the
	 * player has any harmful potion effects.
	 */
	private boolean hasBadPotionEffect() {
		for (Object o : mc.thePlayer.getActivePotionEffects()) {
			PotionEffect pe = (PotionEffect) o;
			if (invalidPotionEffects.contains(pe.getPotionID())) {
				return true;
			}
		}
		return false;
	}
}
