package volcano.summer.client.modules.combat;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class AutoClicker extends Module {
	public int ticks;
	double random;
	public boolean negative;
	public static Value<Double> aps;
	public static Value<Boolean> jitter;
	public Random rand = new Random();

	public AutoClicker() {
		super("AutoClicker", 0, Module.Category.COMBAT);
		aps = new ClampedValue<Double>("APS", "aps", 10.0, 1.0, 20.0, this);
		jitter = new Value("Jitter", "Jitter", Boolean.valueOf(true), this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static void jitterEffect(Random rand) {
		if (rand.nextBoolean()) {
			if (rand.nextBoolean()) {
				EntityPlayerSP tmp20_17 = mc.thePlayer;
				tmp20_17.rotationPitch = ((float) (tmp20_17.rotationPitch - (0.11D + rand.nextFloat())));
			} else {
				EntityPlayerSP tmp48_45 = mc.thePlayer;
				tmp48_45.rotationPitch = ((float) (tmp48_45.rotationPitch + (0.11D + rand.nextFloat())));
			}
		} else if (rand.nextBoolean()) {
			EntityPlayerSP tmp83_80 = mc.thePlayer;
			tmp83_80.rotationYaw = ((float) (tmp83_80.rotationYaw - (0.11D + rand.nextFloat())));
		} else {
			EntityPlayerSP tmp111_108 = mc.thePlayer;
			tmp111_108.rotationYaw = ((float) (tmp111_108.rotationYaw + (0.11D + rand.nextFloat())));
		}
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventUpdate)) {
			Random r = new Random();
			this.ticks += 1;
			if (mc.gameSettings.keyBindAttack.pressed) {
				if (this.jitter.getValue()) {
					this.jitterEffect(this.rand);
				}
				if (this.ticks >= 20.0D / this.aps.getValue().floatValue()
						+ (this.negative ? -this.random : this.random)) {
					Minecraft.thePlayer.swingItem();
					if ((Minecraft.getMinecraft().objectMouseOver != null)
							&& ((Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase))) {
						mc.playerController.attackEntity(Minecraft.thePlayer,
								Minecraft.getMinecraft().objectMouseOver.entityHit);
					}
					this.random = (Math.random() * 2.0D);
					this.negative = r.nextBoolean();
					this.ticks = 0;
				}
			}
		}
	}
}
