package volcano.summer.client.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketRecieve;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Velocity extends Module {
	// test velocity
	public static Value<Double> horizontal;
	public static Value<Double> vertical;
	public ModeValue velocityMode = new ModeValue("VelocityMode", "Mode", "Normal",
			new String[] { "Hypixel", "Normal", "Motion" }, this);
	public Value<Boolean> water = new Value<Boolean>("Water", "water", false, this);
	public static Velocity INSTANCE;
	private double motionX, motionZ;

	public Velocity() {
		super("Velocity", 0, Category.COMBAT);
		horizontal = new ClampedValue<Double>("Horizontal", "horizontal", 0.0, 0.0, 100.0, this);
		vertical = new ClampedValue<Double>("Vertical", "vertical", 0.0, 0.0, 100.0, this);
		this.INSTANCE = this;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (this.velocityMode.getStringValue().equalsIgnoreCase("Normal")) {
			setDisplayName("Normal");
		} else if (this.velocityMode.getStringValue().equalsIgnoreCase("Hypixel")) {
			setDisplayName("Hypixel");
		} else if (this.velocityMode.getStringValue().equalsIgnoreCase("Motion")) {
			setDisplayName("Motion");
		} else {
			setDisplayName("");
		}
		if (mc.theWorld != null) {
			if (this.velocityMode.getStringValue().equalsIgnoreCase("Normal")) {
				if ((event instanceof EventPacketRecieve)
						&& ((((EventPacketRecieve) event).getPacket() instanceof S12PacketEntityVelocity))) {
					if ((Minecraft.thePlayer.isInWater()) && (this.water.getValue())) {
						return;
					}
					setDisplayName("" + this.horizontal.getValue().floatValue() + "% , "
							+ this.vertical.getValue().floatValue() + "%");
					S12PacketEntityVelocity packet = (S12PacketEntityVelocity) ((EventPacketRecieve) event).getPacket();
					if (packet.field_149417_a != this.mc.thePlayer.getEntityId()) {
						return;
					}
					((EventPacketRecieve) event).setCancelled(true);

					double x = packet.func_149411_d() / 8000.0D;
					double y = packet.func_149410_e() / 8000.0D;
					double z = packet.func_149409_f() / 8000.0D;

					double horizontalMultiplier = this.horizontal.getValue().floatValue() / 100.0D;
					double verticalMultiplier = this.vertical.getValue().floatValue() / 100.0D;

					x *= horizontalMultiplier;
					y *= verticalMultiplier;
					z *= horizontalMultiplier;

					if (this.horizontal.getValue().floatValue() == 0 && this.vertical.getValue().floatValue() == 0) {
						return;
					}
					this.mc.thePlayer.motionX = x;
					this.mc.thePlayer.motionY = y;
					this.mc.thePlayer.motionZ = z;
				}
			}
			if (this.velocityMode.getStringValue().equalsIgnoreCase("Hypixel")) {
				if (!(mc.theWorld == null)) {
					if (event instanceof EventPacketRecieve) {
						if ((Minecraft.thePlayer.isInWater()) && (this.water.getValue())) {
							return;
						}
						if (((EventPacketRecieve) event).getPacket() instanceof S27PacketExplosion) {
							((EventPacketRecieve) event).setCancelled(true);
						}
					}
				}
			}
			if (this.velocityMode.getStringValue().equalsIgnoreCase("Motion")) {
				if (event instanceof EventPreMotionUpdate) {
					if ((Minecraft.thePlayer.isInWater()) && (this.water.getValue())) {
						return;
					}
					if (this.mc.thePlayer.hurtTime == 9) {
						this.motionX = this.mc.thePlayer.motionX;
						this.motionZ = this.mc.thePlayer.motionZ;
					} else if (this.mc.thePlayer.hurtTime == 8) {
						this.mc.thePlayer.motionX = (-this.motionX * 0.45D);
						this.mc.thePlayer.motionZ = (-this.motionZ * 0.45D);
					}
				}
			}
		}
	}
}
