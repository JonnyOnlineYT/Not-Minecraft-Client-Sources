package volcano.summer.client.modules.player;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.BlockUtil;
import volcano.summer.client.util.TimerUtil;

public class Cowabunga extends Module {

	private TimerUtil time;
	private boolean move;
	private boolean canChangeMotion;
	private int airTicks;
	private int groundTicks;
	private float headStart;
	private double lastHDistance;
	private boolean isSpeeding;

	public Cowabunga() {
		super("Cowabunga", 0, Category.PLAYER);
		this.time = new TimerUtil();
		this.move = true;
		this.canChangeMotion = false;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		Timer.timerSpeed = 1.0F;
	}

	public void updatePosition(double x, double y, double z) {
		this.mc.thePlayer.sendQueue
				.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, this.mc.thePlayer.onGround));
	}

	private double getDistance(EntityPlayer player, double distance) {
		List<AxisAlignedBB> boundingBoxes = player.worldObj.getCollidingBoundingBoxes(player,
				player.getEntityBoundingBox().addCoord(0.0D, -distance, 0.0D));
		if (boundingBoxes.isEmpty()) {
			return 0.0D;
		}
		double y = 0.0D;
		for (AxisAlignedBB boundingBox : boundingBoxes) {
			if (boundingBox.maxY > y) {
				y = boundingBox.maxY;
			}
		}
		return player.posY - y;
	}

	public boolean isMoving() {
		return (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F);
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventUpdate)) {
			if (isMoving()) {
				if (Keyboard.isKeyDown(56)) {
					updatePosition(0.0D, 2.147483647E9D, 0.0D);
				}
				if ((mc.theWorld != null) && (Minecraft.thePlayer != null) && (Minecraft.thePlayer.onGround)
						&& (!Minecraft.thePlayer.isDead)) {
					this.lastHDistance = 0.0D;
				}
				float direction = Minecraft.thePlayer.rotationYaw + (Minecraft.thePlayer.moveForward < 0.0F ? 180 : 0)
						+ (Minecraft.thePlayer.moveStrafing > 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward < 0.0F
								? -0.5F : Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F) : 0.0F)
						- (Minecraft.thePlayer.moveStrafing < 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward < 0.0F
								? -0.5F : Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F) : 0.0F);

				float xDir = (float) Math.cos((direction + 90.0F) * 3.141592653589793D / 180.0D);
				float zDir = (float) Math.sin((direction + 90.0F) * 3.141592653589793D / 180.0D);
				if (!Minecraft.thePlayer.isCollidedVertically) {
					this.airTicks += 1;
					this.isSpeeding = true;
					if (mc.gameSettings.keyBindSneak.isPressed()) {
						Minecraft.thePlayer.sendQueue.addToSendQueue(
								new C03PacketPlayer.C04PacketPlayerPosition(0.0D, 2.147483647E9D, 0.0D, false));
					}
					this.groundTicks = 0;
					if (!Minecraft.thePlayer.isCollidedVertically) {
						if (Minecraft.thePlayer.motionY == -0.07190068807140403D) {
							Minecraft.thePlayer.motionY *= 0.3499999940395355D;
						}
						if (Minecraft.thePlayer.motionY == -0.10306193759436909D) {
							Minecraft.thePlayer.motionY *= 0.550000011920929D;
						}
						if (Minecraft.thePlayer.motionY == -0.13395038817442878D) {
							Minecraft.thePlayer.motionY *= 0.6700000166893005D;
						}
						if (Minecraft.thePlayer.motionY == -0.16635183030382D) {
							Minecraft.thePlayer.motionY *= 0.6899999976158142D;
						}
						if (Minecraft.thePlayer.motionY == -0.19088711097794803D) {
							Minecraft.thePlayer.motionY *= 0.7099999785423279D;
						}
						if (Minecraft.thePlayer.motionY == -0.21121925191528862D) {
							Minecraft.thePlayer.motionY *= 0.20000000298023224D;
						}
						if (Minecraft.thePlayer.motionY == -0.11979897632390576D) {
							Minecraft.thePlayer.motionY *= 0.9300000071525574D;
						}
						if (Minecraft.thePlayer.motionY == -0.18758479151225355D) {
							Minecraft.thePlayer.motionY *= 0.7200000286102295D;
						}
						if (Minecraft.thePlayer.motionY == -0.21075983825251726D) {
							Minecraft.thePlayer.motionY *= 0.7599999904632568D;
						}
						if ((getDistance(Minecraft.thePlayer, 69.0D) < 0.5D)
								&& (!BlockUtil
										.getBlock(new BlockPos(Minecraft.thePlayer.posX,
												Minecraft.thePlayer.posY - 0.32D, Minecraft.thePlayer.posZ))
										.isFullCube())) {
							if (Minecraft.thePlayer.motionY == -0.23537393014173347D) {
								Minecraft.thePlayer.motionY *= 0.029999999329447746D;
							}
							if (Minecraft.thePlayer.motionY == -0.08531999505205401D) {
								Minecraft.thePlayer.motionY *= -0.5D;
							}
							if (Minecraft.thePlayer.motionY == -0.03659320313669756D) {
								Minecraft.thePlayer.motionY *= -0.10000000149011612D;
							}
							if (Minecraft.thePlayer.motionY == -0.07481386749524899D) {
								Minecraft.thePlayer.motionY *= -0.07000000029802322D;
							}
							if (Minecraft.thePlayer.motionY == -0.0732677700939672D) {
								Minecraft.thePlayer.motionY *= -0.05000000074505806D;
							}
							if (Minecraft.thePlayer.motionY == -0.07480988066790395D) {
								Minecraft.thePlayer.motionY *= -0.03999999910593033D;
							}
							if (Minecraft.thePlayer.motionY == -0.0784000015258789D) {
								Minecraft.thePlayer.motionY *= 0.10000000149011612D;
							}
							if (Minecraft.thePlayer.motionY == -0.08608320193943977D) {
								Minecraft.thePlayer.motionY *= 0.10000000149011612D;
							}
							if (Minecraft.thePlayer.motionY == -0.08683615560584318D) {
								Minecraft.thePlayer.motionY *= 0.05000000074505806D;
							}
							if (Minecraft.thePlayer.motionY == -0.08265497329678266D) {
								Minecraft.thePlayer.motionY *= 0.05000000074505806D;
							}
							if (Minecraft.thePlayer.motionY == -0.08245009535659828D) {
								Minecraft.thePlayer.motionY *= 0.05000000074505806D;
							}
							if (Minecraft.thePlayer.motionY == -0.08244005633718426D) {
								Minecraft.thePlayer.motionY = -0.08243956442521608D;
							}
							if (Minecraft.thePlayer.motionY == -0.08243956442521608D) {
								Minecraft.thePlayer.motionY = -0.08244005590677261D;
							}
							if ((Minecraft.thePlayer.motionY > -0.1D) && (Minecraft.thePlayer.motionY < -0.08D)
									&& (!Minecraft.thePlayer.onGround) && (mc.gameSettings.keyBindForward.pressed)) {
								Minecraft.thePlayer.motionY = -9.999999747378752E-5D;
							}
						} else {
							if ((Minecraft.thePlayer.motionY < -0.2D) && (Minecraft.thePlayer.motionY > -0.24D)) {
								Minecraft.thePlayer.motionY *= 0.7D;
							}
							if ((Minecraft.thePlayer.motionY < -0.25D) && (Minecraft.thePlayer.motionY > -0.32D)) {
								Minecraft.thePlayer.motionY *= 0.8D;
							}
							if ((Minecraft.thePlayer.motionY < -0.35D) && (Minecraft.thePlayer.motionY > -0.8D)) {
								Minecraft.thePlayer.motionY *= 0.98D;
							}
							if ((Minecraft.thePlayer.motionY < -0.8D) && (Minecraft.thePlayer.motionY > -1.6D)) {
								Minecraft.thePlayer.motionY *= 0.99D;
							}
						}
					}
					net.minecraft.util.Timer.timerSpeed = (float) 0.8500000238418579D;
					double[] speedVals = { 0.420606D, 0.417924D, 0.415258D, 0.412609D, 0.409977D, 0.407361D, 0.404761D,
							0.402178D, 0.399611D, 0.39706D, 0.394525D, 0.392D, 0.3894D, 0.38644D, 0.383655D, 0.381105D,
							0.37867D, 0.37625D, 0.37384D, 0.37145D, 0.369D, 0.3666D, 0.3642D, 0.3618D, 0.35945D, 0.357D,
							0.354D, 0.351D, 0.348D, 0.345D, 0.342D, 0.339D, 0.336D, 0.333D, 0.33D, 0.327D, 0.324D,
							0.321D, 0.318D, 0.315D, 0.312D, 0.309D, 0.307D, 0.305D, 0.303D, 0.3D, 0.297D, 0.295D,
							0.293D, 0.291D, 0.289D, 0.287D, 0.285D, 0.283D, 0.281D, 0.279D, 0.277D, 0.275D, 0.273D,
							0.271D, 0.269D, 0.267D, 0.265D, 0.263D, 0.261D, 0.259D, 0.257D, 0.255D, 0.253D, 0.251D,
							0.249D, 0.247D, 0.245D, 0.243D, 0.241D, 0.239D, 0.237D };
					if (mc.gameSettings.keyBindForward.pressed) {
						try {
							Minecraft.thePlayer.motionX = (xDir * speedVals[(this.airTicks - 1)] * 3.0D);
							Minecraft.thePlayer.motionZ = (zDir * speedVals[(this.airTicks - 1)] * 3.0D);
						} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
						}
					} else {
						Minecraft.thePlayer.motionX = 0.0D;
						Minecraft.thePlayer.motionZ = 0.0D;
					}
				} else {
					net.minecraft.util.Timer.timerSpeed = (float) 1.0D;
					this.airTicks = 0;
					this.groundTicks += 1;
					this.headStart -= 1.0F;
					Minecraft.thePlayer.motionX /= 13.0D;
					Minecraft.thePlayer.motionZ /= 13.0D;
					if (this.groundTicks == 1) {
						updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
						updatePosition(Minecraft.thePlayer.posX + 0.0624D, Minecraft.thePlayer.posY,
								Minecraft.thePlayer.posZ);
						updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.419D,
								Minecraft.thePlayer.posZ);
						updatePosition(Minecraft.thePlayer.posX + 0.0624D, Minecraft.thePlayer.posY,
								Minecraft.thePlayer.posZ);
						updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.419D,
								Minecraft.thePlayer.posZ);
					}
					if (this.groundTicks > 2) {
						this.groundTicks = 0;
						Minecraft.thePlayer.motionX = (xDir * 0.3D);
						Minecraft.thePlayer.motionZ = (zDir * 0.3D);
						Minecraft.thePlayer.motionY = 0.42399999499320984F;
					}
				}
			}
		}
	}
}
