package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventMotion;
import summer.cheat.eventsystem.events.player.EventUpdate;

import java.util.ArrayList;

public class Speed extends Cheats {
    // simple ass speed kek
    public Minecraft mc = Minecraft.getMinecraft();
    private int stage;
    private int hops;
    private double moveSpeed;
    private double lastDist;
    private String mode;
    private TargetStrafe targetStrafe;

    public Speed() {
        super("Speed", "Bhop", Selection.MOVEMENT, false);
    }

    @Override
    public void onSetup() {
        final ArrayList<String> options = new ArrayList<String>();
    }
    @EventTarget
    public void Packet(final EventPacket ep) {
        if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
        	this.toggle();
        }
    }
    @EventTarget
    public void onUpdate(final EventUpdate e) {
    	this.setDisplayName("Speed\u00A77 " + "Watchdog");
        if (Minecraft.thePlayer.onGround) {
            Timer.timerSpeed = 1.0f;
        }
        this.lastDist = Math.sqrt((Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX) * (Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX) + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ) * (Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ));
        if (this.lastDist > 5.0) {
            this.lastDist = 0.0;
        }
        e.setOnGround(Minecraft.thePlayer.onGround = Minecraft.thePlayer.posY % 1.0D / 64.0D == 0);
    }

    @EventTarget
    public void onMove(final EventMotion e) {
        if (this.targetStrafe == null)
            this.targetStrafe = CheatManager.getInstance(TargetStrafe.class);
        if (Timer.timerSpeed < 1) {
        }
        if (Minecraft.thePlayer.isCollidedVertically) {
            Timer.timerSpeed = 1f;
        } else
            Timer.timerSpeed = 1f;
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                this.lastDist = 0.0;
                double motionY = 0.42199998688697815D;
                if (Minecraft.thePlayer.moving() && Minecraft.thePlayer.onGround) {
                    if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
                        motionY += (Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                    }
                    e.setY(Minecraft.thePlayer.motionY = motionY);
                    ++this.hops;
                    this.moveSpeed *= 1.60;
                    break;
                }
                break;
            }
            case 3: {
                final double lastDist = this.lastDist;
                final double n = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? (Minecraft.thePlayer.isPotionActive(Potion.jump) ? 0.54 : 0.655) : 0.7025;
                final double lastDist2 = this.lastDist;
                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                this.moveSpeed = lastDist - n * (lastDist2 - EntityPlayerSP.getBaseMoveSpeed());
                break;
            }
            default: {
                if ((Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, Minecraft.thePlayer.motionY, 0.0)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = (mc.thePlayer.moving() ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 69.0;
                break;
            }
        }
        MoveUtils.setMotion(e, this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.defaultSpeed() + 0.011219999998));
        ++this.stage;
        if (this.targetStrafe.canStrafe()) {
            this.targetStrafe.strafe(e, this.moveSpeed);
        }
    }

    public static boolean isNotCollidingBelow(double paramDouble) {
        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -paramDouble, 0.0D)).isEmpty()) {
            return true;
        }
        return false;
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2923;
        if (Minecraft.thePlayer != null) {
            if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null) {
                    final int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                    baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
                }
            }
        }
        return baseSpeed;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.motionZ = 0.0;
        Minecraft.thePlayer.motionX = 0.0;
        Timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.lastDist = 0.0;
        this.hops = 1;
        this.stage = 0;
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        this.moveSpeed = EntityPlayerSP.getBaseMoveSpeed() * (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.0 : 1.34);
    }
}




