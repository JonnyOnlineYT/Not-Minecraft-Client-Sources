package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventLiquidSolid;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Jesus extends Module
{
    public static boolean jesusTick;
    public static boolean swap;
    public static int ticks;
    public static ListSetting mode;
    public static NumberSetting speed;
    public static NumberSetting NCPSpeed;
    public static NumberSetting motionUp;
    public static BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private final BooleanSetting speedCheck;
    private final BooleanSetting autoMotionStop;
    private final BooleanSetting autoWaterDown;
    private final BooleanSetting upTp;
    private int waterTicks;
    int y = 0;
    public static boolean inWater;

    public Jesus() {
        super("Jesus", "��� �� ����", ModuleCategory.Movement);
        this.speed = new NumberSetting("Speed", 10f, 1f, 35f, 1f, () -> true);
        this.timerSpeed = new NumberSetting("TimerSpeed", 1.05f, 1.01f, 1.5f, 0.01f, () -> Jesus.useTimer.getCurrentValue());
        this.speedCheck = new BooleanSetting("SpeedCheck", false, () -> true);
        this.autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> Jesus.mode.currentMode.equals("ReallyWorld"));
        this.autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> Jesus.mode.currentMode.equals("ReallyWorld"));
        upTp = new BooleanSetting("Auto Water Up", false, () -> Jesus.mode.currentMode.equals("ReallyWorld"));
        this.waterTicks = 0;
        this.addSettings(Jesus.mode, Jesus.speed, Jesus.NCPSpeed, Jesus.useTimer, this.timerSpeed, Jesus.motionUp, this.speedCheck, this.autoWaterDown, this.autoMotionStop, upTp);
    }

    @Override
    public void onDisable() {
        inWater = false;
        Helper.mc.player.speedInAir = 0.02f;
        Jesus.mc.timer.timerSpeed = 1.0f;
        if (Jesus.mode.currentMode.equals("ReallyWorld") && this.autoWaterDown.getCurrentValue()) {
            final EntityPlayerSP player = Jesus.mc.player;
            player.motionY -= 500.0;
        }
        this.waterTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onLiquidBB(final EventLiquidSolid event) {
        if (Jesus.mode.currentMode.equals("ReallyWorld") || Jesus.mode.currentMode.equals("NCP")) {
        }
    }

    private boolean isWater() {
        final BlockPos bp1 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        final BlockPos bp2 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        final BlockPos bp3 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        final BlockPos bp4 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        return (Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER) || (Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA);
    }

    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        if (Jesus.mc.player.isPotionActive(MobEffects.SPEED) || !this.speedCheck.getCurrentValue()) {
            final BlockPos blockPos = new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.1, Jesus.mc.player.posZ);
            final Block block = Jesus.mc.world.getBlockState(blockPos).getBlock();
            if (Jesus.useTimer.getCurrentValue()) {
                Jesus.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
            }
            if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix")) {
                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.008, Helper.mc.player.posZ)).getBlock() == Blocks.WATER
                        && !Helper.mc.player.onGround) {
                    boolean isUp = Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.03, Helper.mc.player.posZ)).getBlock() == Blocks.WATER;
                    Helper.mc.player.jumpMovementFactor = 0;
                    float yport = MovementUtils.getPlayerMotion() > 0.1 ? 0.02f : 0.032f;
                    Helper.mc.player.setVelocity(Helper.mc.player.motionX, Helper.mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.1, Helper.mc.player.motionZ);

                }
                if (Helper.mc.player.posY > (int) Helper.mc.player.posY + 0.89 && Helper.mc.player.posY <= (int) Helper.mc.player.posY + 1 || Helper.mc.player.fallDistance > 3.5) {
                    Helper.mc.player.posY = (int) Helper.mc.player.posY + 1 + 1E-45;
                    if (!Helper.mc.player.isInWater() && Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY - 0.1, Helper.mc.player.posZ)).getBlock() == Blocks.WATER) {

                        if (Helper.mc.player.collidedHorizontally) {
                            Helper.mc.player.motionY = 0.2;
                            Helper.mc.player.motionX *= 0f;
                            Helper.mc.player.motionZ *= 0f;
                        }
                        MovementUtils.setSpeed(MathHelper.clamp((float) (MovementUtils.getPlayerMotion() + 0.2f), 0.5f, 1.14f));
                    }

                }
                if ((Helper.mc.player.isInWater() || Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.15, Helper.mc.player.posZ)).getBlock() == Blocks.WATER)) {
                    Helper.mc.player.motionY = 0.16;
                    if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 2, Helper.mc.player.posZ)).getBlock() == Blocks.AIR) {
                        Helper.mc.player.motionY = 0.12;
                    }
                    if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 1, Helper.mc.player.posZ)).getBlock() == Blocks.AIR) {
                        Helper.mc.player.motionY = 0.18;
                    }
                }
            }
            else if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld")) {
                Helper.mc.player.speedInAir = 0.02f;
                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY - 0.001f * speed.getNumberValue(), Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9) && !Helper.mc.player.onGround) {
                    speed(9.0);
                    Helper.mc.player.jumpMovementFactor = 0.01f;
                }
                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                    Helper.mc.player.motionX = 0.0;
                    Helper.mc.player.motionY = 0.04700000074505806;
                    Helper.mc.player.jumpMovementFactor = 0.01f;
                    Helper.mc.player.motionZ = 0.0;
                }
                if (upTp.getCurrentValue() && Helper.mc.player.posY < 63 && Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, 62, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9) && Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, 63, Helper.mc.player.posZ)).getBlock() == Blocks.AIR) {
                    if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.3, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        Helper.mc.player.setPositionAndUpdate(Helper.mc.player.posX, 62.8f + y, Helper.mc.player.posZ);
                    }
                } else {
                    if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 1, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        Helper.mc.player.motionY = 0.18;
                    }
                }
            } else if (mode.currentMode.equalsIgnoreCase("ReallyWorld2")) {
                BlockPos blockPos2;
                Block block2;
                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ)).getBlock() == Blocks.WATER || Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ)).getBlock() == Blocks.LAVA) {
                    boolean isUp = Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.0311, Helper.mc.player.posZ)).getBlock() == Blocks.WATER || Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.0311, Helper.mc.player.posZ)).getBlock() == Blocks.LAVA;


                    float yport = MovementUtils.getSpeed() > 0.1 ? 0.02f : 0.032f;
                    Helper.mc.player.setVelocity(Helper.mc.player.motionX * MovementUtils.getSpeed(), Helper.mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.114, Helper.mc.player.motionZ * MovementUtils.getSpeed());
                    if (Helper.mc.player.posY > (int) Helper.mc.player.posY + 0.89 && Helper.mc.player.posY <= (int) Helper.mc.player.posY + 1.0f) {
                        Helper.mc.player.posY = (int) Helper.mc.player.posY + 1.0f + 1E-45;
                        Helper.mc.player.fallDistance = -99999.0F;
                        if (!Helper.mc.player.isInWater()) {
                            Helper.mc.player.motionX = 0.0f;
                            Helper.mc.player.motionZ = 0.0f;
                            inWater = true;
                            MovementUtils.setSpeed(0.1f * speed.getNumberValue());
                        }

                    }

                }
                if ((Helper.mc.player.isInWater() || Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.15, Helper.mc.player.posZ)).getBlock() == Blocks.WATER)) {
                    Helper.mc.player.motionY = 0.16;
                    if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 2, Helper.mc.player.posZ)).getBlock() == Blocks.AIR) {
                        Helper.mc.player.motionY = 0.12;
                    }
                    if (upTp.getCurrentValue() && Helper.mc.player.posY < 63 && Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, 62, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9) && Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, 63, Helper.mc.player.posZ)).getBlock() == Blocks.AIR) {
                        if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 0.3, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                            Helper.mc.player.setPositionAndUpdate(Helper.mc.player.posX, 62.8f + y, Helper.mc.player.posZ);
                        }
                    } else {
                        if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 1, Helper.mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                            Helper.mc.player.motionY = 0.18;
                        }
                    }
                }
                if (Helper.mc.world.getBlockState(new BlockPos(Helper.mc.player.posX, Helper.mc.player.posY + 1, Helper.mc.player.posZ)).getBlock() == Blocks.LAVA) {
                    Helper.mc.player.motionY = 0.18;
                }
            }
        }
    }

    public void speed(double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? 45 : -45);
                } else if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -45 : 45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
                mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0)));
                mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0)) + strafe * speed * Math.cos(Math.toRadians(yaw + 90.0)));
            }
        }
    }

    static {
        Jesus.mode = new ListSetting("Jesus Mode", "Matrix", () -> true, new String[] { "Matrix", "ReallyWorld","ReallyWorld2", "SunRise", "NCP" });
        Jesus.speed = new NumberSetting("Speed", 0.65f, 0.1f, 10.0f, 0.01f, () -> !Jesus.mode.currentMode.equals("NCP"));
        Jesus.NCPSpeed = new NumberSetting("NCP Speed", 0.25f, 0.01f, 0.5f, 0.01f, () -> Jesus.mode.currentMode.equals("NCP"));
        Jesus.useTimer = new BooleanSetting("Use Timer", false, () -> true);
    }
}