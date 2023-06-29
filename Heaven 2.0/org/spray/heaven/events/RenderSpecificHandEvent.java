package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class RenderSpecificHandEvent extends EventCancellable {

	private EnumHand hand;
	private float partialTicks;
	private float interpolatedPitch;
	private float swingProgress;
	private float equipProgress;
	@Nonnull
	private ItemStack stack;

	public RenderSpecificHandEvent(EnumHand hand, float partialTicks, float interpolatedPitch, float swingProgress,
			float equipProgress, @Nonnull ItemStack stack) {
		this.hand = hand;
		this.partialTicks = partialTicks;
		this.interpolatedPitch = interpolatedPitch;
		this.swingProgress = swingProgress;
		this.equipProgress = equipProgress;
		this.stack = stack;
	}

	public EnumHand getHand() {
		return hand;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public float getInterpolatedPitch() {
		return interpolatedPitch;
	}

	public float getSwingProgress() {
		return swingProgress;
	}

	public float getEquipProgress() {
		return equipProgress;
	}

	@Nonnull
	public ItemStack getItemStack() {
		return stack;
	}

}
