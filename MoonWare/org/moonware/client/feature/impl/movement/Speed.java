/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventPreMotionUpdate;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Move.MoveUtils;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.moonware.client.helpers.player.MovementHelper.getSpeed1;

public class Speed extends Feature {

    public static int stage;
    private final BooleanSetting strafing;
    private TimerHelper matrixRtime = new TimerHelper();
    private final BooleanSetting boost;
    private final List<Packet<?>> packets;
    private final List<double[]> positions;
    private final TimerHelper pulseTimer = new TimerHelper();
    private final BooleanSetting potionCheck;
    public TimerHelper timerHelper = new TimerHelper();
    public double moveSpeed;
    private float ticks = 35.0F;
    private double matrixExploitSpeedStart;
    private double matrixExploitSpeedBoost;
    private double matrixExploitSpeedFinal;
    private float GuardianBoost;
    public NumberSetting motionMultiplier;

    public static ListSetting speedMode;
    public static float plus = 0.5f;
    public BooleanSetting autoHitDisable;
    public NumberSetting jumpMoveFactor = new NumberSetting("Custom Speed", 0.0265F, 0.01F, 0.1F, 0.001F, () -> speedMode.currentMode.equals("Custom"));
    public BooleanSetting onGround = new BooleanSetting("Ground Only", false, () -> speedMode.currentMode.equals("Custom"));
    public NumberSetting onGroundSpeed = new NumberSetting("Custom Ground Speed", 0.5F, 0.001F, 10, 0.01F, () -> speedMode.currentMode.equals("Custom") && onGround.getBoolValue());
    public NumberSetting motionY = new NumberSetting("Custom Y-Motion", 0.42F, 0.01F, 1, 0.01F, () -> speedMode.currentMode.equals("Custom"));
    public BooleanSetting blink = new BooleanSetting("Blink", false, () -> speedMode.currentMode.equals("Custom"));

        public BooleanSetting timerExploit = new BooleanSetting("Timer Exploit", false, () -> !speedMode.currentMode.equals("Matrix Old"));
    public NumberSetting timer = new NumberSetting("Custom Timer", 1, 0.1F, 10, 0.1F, () -> speedMode.currentMode.equals("Custom") && !timerExploit.getBoolValue());
    public NumberSetting speed = new NumberSetting("Speed", 1, 0.1F, 10, 0.1F, () -> speedMode.currentMode.equals("Motion") || speedMode.currentMode.equals("MatrixGround 6.2.2"));

    public NumberSetting speedBoost = new NumberSetting("BoostValue", 0.13F, 0.01F, 10, 0.1F, () -> speedMode.currentMode.equals("Reallyworld"));
    private int boostTick;
    private boolean disableLogger;
    public ListSetting guardianMode = new ListSetting("Guardian Mode", "Rage", () -> speedMode.getCurrentMode().equalsIgnoreCase("Guardian"), "Rage","Legit");
    private float level = 1;
    private float moveSpeedn = 0.2873f;
    private float lastDist;
    private float timerDelay;
    public NumberSetting rageStart = new NumberSetting("Rage Start Value", 0, 0F, 0.5F, 0.1F, () -> speedMode.currentMode.equals("Guardian") && guardianMode.getCurrentMode().equalsIgnoreCase("Rage") );
    public NumberSetting rageBoost= new NumberSetting("Rage Boost Value", 0.04F, 0F, 2F, 0.001F, () -> speedMode.currentMode.equals("Guardian") && guardianMode.getCurrentMode().equalsIgnoreCase("Rage") );
    public NumberSetting rageFinal= new NumberSetting("Rage Final Value", 1, 0.1F, 8, 0.1F, () -> speedMode.currentMode.equals("Guardian") && guardianMode.getCurrentMode().equalsIgnoreCase("Rage") );
    public static NumberSetting StormBoost = new NumberSetting("LowHop Boost",0,0,1,0.00001F,()-> speedMode.currentMode.equals("ShtormHVH"));
    public static NumberSetting tickboost = new NumberSetting("JumpTick Boost",0,-150,150,1,()-> speedMode.currentMode.equals("ShtormHVH"));
    public static NumberSetting timerboost = new NumberSetting("Timer Boost",0,0,2,0.001F,()-> speedMode.currentMode.equals("ShtormHVH"));
    public static NumberSetting motionboost = new NumberSetting("Motion Boost",0,-1,1,0.001F,()-> speedMode.currentMode.equals("ShtormHVH"));
    public static boolean needSprintState;
    public static int waterTicks;
    public double less;
    public double stair;
    public boolean slowDownHop;
    public Speed() {
        super("Speed", "Увеличивает вашу скорость", Type.Movement);

        speedMode = new ListSetting("Speed Mode", "Damage", () -> true,"Reallyworld","ReallyworldTimer","Damage","NexusGriefFast","ShtormHVH");
        motionMultiplier = new NumberSetting("Motion Multiplier", 0.3f, 0.05f, 0.3f, 0.01f, () -> true);
        strafing = new BooleanSetting("Strafing", false, () -> true);
        boost = new BooleanSetting("GroundSpoof", true, () -> speedMode.currentMode.equals("Matrix Old") || speedMode.currentMode.equals("Custom"));
        potionCheck = new BooleanSetting("Speed Potion Check", false, () -> speedMode.currentMode.equals("Custom"));
        autoHitDisable = new BooleanSetting("Auto Hit Disable", false, () -> true);
        packets = new ArrayList<>();
        positions = new LinkedList<>();
        addSettings(speedMode, StormBoost, tickboost, timerboost, motionboost,  guardianMode,rageStart,rageBoost,rageFinal, speed,speedBoost,strafing, boost,  jumpMoveFactor, motionY, onGroundSpeed, onGround, blink, timerExploit, timer, potionCheck, autoHitDisable);
    }


    @EventTarget
    public void onMove(EventMove eventMove) {
        if (speedMode.currentMode.equals("NCP LowHop")) {
            if (MovementHelper.isMoving() && !Minecraft.player.isInLiquid()) {
                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown() && Minecraft.player.jumpTicks == 0) {
                    Minecraft.timer.timerSpeed = 1.09F;
                    eventMove.setY(Minecraft.player.motionY = 0.1);
                    Minecraft.player.jumpTicks = 10;
                } else if (eventMove.getY() < 0) {
                    Minecraft.timer.timerSpeed = 1.04f;
                }

                float speed = Minecraft.player.isSprinting() ? 0.97F : 1;
                double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                MovementHelper.setEventSpeed(eventMove, moveSpeed);
            }
        }
        if (speedMode.currentMode.equalsIgnoreCase("Matrix strafe")) {
            Minecraft.player.setSprinting(false);
            Minecraft.player.jumpMovementFactor = 0.0266f;
            if (!Minecraft.player.onGround) {
                Minecraft.gameSettings.keyBindJump.setPressed(Minecraft.gameSettings.keyBindJump.isKeyDown());
                if (MoveUtils.getSqrtSpeed() < 0.218) {
                    MoveUtils.strafe(0.218f);
                    Minecraft.player.jumpMovementFactor = 0.0269f;
                }
            }
            if (Minecraft.player.motionY < 0) {
                Minecraft.timer.timerSpeed =1.09f;
                if (Minecraft.player.fallDistance > 1.4)
                    Minecraft.timer.timerSpeed = 1.0F;
            } else {
                Minecraft.timer.timerSpeed = 0.95f;
            }
            if (Minecraft.player.onGround && MoveUtils.isMoving()) {
                Minecraft.timer.timerSpeed=1.08f;
                Minecraft.gameSettings.keyBindJump.setPressed(true);
                // if(mc.player.movementInput.moveStrafe <= 0.01) {
                MoveUtils.strafe((MoveUtils.getSqrtSpeed() * 1.3371f));
                // }
            } else if (!MoveUtils.isMoving()) {
                Minecraft.timer.timerSpeed = 1.0F;
            }

            if (MoveUtils.getSqrtSpeed() < 0.22)
                MoveUtils.strafe();
        }
        if (speedMode.currentMode.equalsIgnoreCase("MatrixNoElytra")) {
            if (!matrixRtime.hasReached(100)) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindForward.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindBack.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindLeft.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    if (matrixExploitSpeedBoost < 0.2) {
                        if (Minecraft.player.ticksExisted % 1.5 == 0) {
                            matrixExploitSpeedBoost += matrixExploitSpeedBoost + 0.1F;
                        }
                    }
                    float rageplus = 0.0F;
                    float rightSpeed = 2.41f;
                    if (MovementHelper.isMoving()) {
                        if (Minecraft.player.ticksExisted % 2.5 == 0 && rageplus < rageFinal.getNumberValue()) {
                            rageplus += 0;
                        }

                    }
                    if (!Minecraft.player.onGround) {
                        if (Minecraft.player.ticksExisted % 2.5 == 0) {
                            rageplus -= 0.4f;
                        }

                        rightSpeed = 1.88f;
                    } else {
                        rageplus = (float) matrixExploitSpeedBoost;
                        rightSpeed = 2.41f;
                    }


                    MovementHelper.setSpeedD((rightSpeed + rageplus) / 5f);
                }
            }else if (matrixRtime.hasReached(250)) {
                matrixRtime.reset();
            }
        }
        if (speedMode.currentMode.equals("ShtormHVH")) {
            if (MovementHelper.isMoving() && !Minecraft.player.isInLiquid()) {
                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.isKeyDown() && Minecraft.player.jumpTicks == 0) {
                    Minecraft.timer.timerSpeed = 1.05F + timerboost.getCurrentValue();
                    eventMove.setY(Minecraft.player.motionY = 0.35 + motionboost.getCurrentValue());
                    Minecraft.player.jumpTicks = (int) (40 + tickboost.getCurrentValue());
                } else if (eventMove.getY() < 0) {
                    Minecraft.timer.timerSpeed = 1.05f + timerboost.getCurrentValue();
                }

                if (!Minecraft.player.onGround) {

                    float speed = Minecraft.player.isSprinting() ? 0.97F + StormBoost.getCurrentValue() : 1 + StormBoost.getCurrentIntValue();
                    double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                    MovementHelper.setEventSpeed(eventMove, moveSpeed);
                }else{
                    float speed = Minecraft.player.isSprinting() ? 0.97F: 1;
                    double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                    MovementHelper.setEventSpeed(eventMove, moveSpeed);
                }
            }
        }
        if (speedMode.getCurrentMode().equalsIgnoreCase("MatrixNewExploit") && Minecraft.player.onGround) {
            if (Minecraft.player.ticksExisted % 2 == 0) {
                Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindForward.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindBack.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindLeft.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                if (matrixExploitSpeedBoost < 2) {
                    if (Minecraft.player.ticksExisted % 1.5 == 0) {
                        matrixExploitSpeedBoost += matrixExploitSpeedBoost + 0.1F;
                    }
                }
                float rageplus = 0.0F;
                float rightSpeed = 2.41f;
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.ticksExisted % 2.5 == 0 && rageplus < rageFinal.getNumberValue()) {
                        rageplus += 0;
                    }

                }
                if (!Minecraft.player.onGround) {
                    if (Minecraft.player.ticksExisted % 2.5 == 0) {
                        rageplus -= 0.4f;
                    }

                    rightSpeed = 1.88f;
                } else {
                    rageplus = (float) matrixExploitSpeedBoost;
                    rightSpeed = 2.41f;
                }


                MovementHelper.setSpeedD((rightSpeed + rageplus + 1.0F) / 5f);
            } else if (Minecraft.gameSettings.keyBindForward.isKeyDown() || Minecraft.gameSettings.keyBindBack.isKeyDown() || Minecraft.gameSettings.keyBindLeft.isKeyDown() || Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                if (strafing.getCurrentValue()) {
                    if (Minecraft.player.ticksExisted % 1.5 == 0) {
                        Minecraft.player.motionY -= 0.0005;
                    }
                }
            }

        }
        if (speedMode.currentMode.equalsIgnoreCase("SunriseStrafe")) {
            boolean falseA = false;
            if (MovementHelper.isMoving() && !Minecraft.player.onGround && Minecraft.player.fallDistance > 0.01f) {
                if (!Minecraft.gameSettings.keyBindJump.isKeyDown() && Minecraft.player.jumpTicks == 0 && Minecraft.player.fallDistance < 0.44f && falseA) {
                    Minecraft.timer.timerSpeed = 1.02F;
                    eventMove.setY(Minecraft.player.motionY = 0.42);
                    Minecraft.player.jumpTicks = 5;
                } else if (eventMove.getY() < 0) {
                    Minecraft.timer.timerSpeed = 1.02f;
                }

                float speed = Minecraft.player.isSprinting() ? 0.97F : 1;
                double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                //MovementHelper.setEventSpeed(eventMove, moveSpeed);
                MovementHelper.setSpeed(MovementHelper.getBaseMoveSpeed() * 0.89f);
            }

        }
        if (speedMode.currentMode.equalsIgnoreCase("SpoofExploit")) {
            if (Minecraft.player.onGround)
                Minecraft.player.jump();
            if (Minecraft.player.motionY > 0.01233425894537203957)
                Minecraft.player.motionY += 1.0f;
        }
        if (speedMode.currentMode.equalsIgnoreCase("SunriseStrafeExploit")) {
            boolean falseA = false;
            if (MovementHelper.isMoving() && !Minecraft.player.onGround && Minecraft.player.fallDistance > 0.01f) {
                if (!Minecraft.gameSettings.keyBindJump.isKeyDown() && Minecraft.player.jumpTicks == 0 && Minecraft.player.fallDistance < 0.44f && falseA) {
                    Minecraft.timer.timerSpeed = 1.02F;
                    eventMove.setY(Minecraft.player.motionY = 0.42);
                    Minecraft.player.jumpTicks = 5;
                } else if (eventMove.getY() < 0) {
                    Minecraft.timer.timerSpeed = 1.02f;
                    Minecraft.player.speedInAir = 0.555f;
                }

                float speed = Minecraft.player.isSprinting() ? 0.97F : 1;
                double moveSpeed = Math.max(MovementHelper.getBaseMoveSpeed(), MovementHelper.getSpeed()) * speed;
                //MovementHelper.setEventSpeed(eventMove, moveSpeed);
                MovementHelper.setSpeed(MovementHelper.getBaseMoveSpeed() * 0.89f);
            }
        }
    }
    public double getCurrentSpeed(int stage) {
        double speed = MoveUtils.getBaseSpeed() + 0.028 * (double) MoveUtils.getSpeedEffect()
                + (double) MoveUtils.getSpeedEffect() / 15.0;
        double initSpeed = 0.4145 + (double) MoveUtils.getSpeedEffect() / 12.5;
        double decrease = (double) stage / 500.0 * 1.87;
        if (stage == 0) {
            speed = 0.64 + ((double) MoveUtils.getSpeedEffect() + 0.028 * (double) MoveUtils.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            speed = initSpeed;
        } else if (stage >= 2) {
            speed = initSpeed - decrease;
        }
        return Math.max(speed,
                slowDownHop ? speed : MoveUtils.getBaseSpeed() + 0.028 * (double) MoveUtils.getSpeedEffect());
    }
    @EventTarget
    public void onPreMove(EventPreMotionUpdate move) {
    }
    public static boolean canspeedMove() {
        if (Minecraft.player.fallDistance >= 1) {
            return true;
        }
        return Minecraft.player.onGround || Minecraft.player.fallDistance <= 1;
    }
    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        setSuffix(speedMode.getCurrentMode());
        if (getState()) {
            String mode = speedMode.getOptions();
            EntityPlayerSP var10000;
            double x;
            double y;
            double z;
            double yaw = Minecraft.player.rotationYaw * 0.0174532920F;
            setSuffix(mode);

//            if (speedMode.getOptions().equalsIgnoreCase("ReallyworldSP")) {
//                //EventPreMotionUpdate move = (EventPreMotionUpdate) movee;
////        if (autojump.isEnabled(false) && !mc.gameSettings.keyBindJump.isKeyDown()) {
////            return;
////        }
////        EventPreMove move = (EventPreMove) event;
//                Speed.mc.player.jumpMovementFactor = (float) ((double) Speed.mc.player.jumpMovementFactor * 1.04);
//                boolean collided = Speed.mc.player.isCollidedHorizontally;
//                if (collided) {
//                    stage = -1;
//                }
//                if (this.stair > 0.0) {
//                    this.stair -= 0.3;
//                }
//                this.less -= this.less > 1.0 ? 0.24 : 0.17;
//                if (this.less < 0.0) {
//                    this.less = 0.0;
//                }
//                if (!Speed.mc.player.isInWater() && Speed.mc.player.onGround) {
//                    collided = Speed.mc.player.isCollidedHorizontally;
//                    if (stage >= 0 || collided) {
//                        stage = 0;
//                        float motY = 0.42f;
//                        Speed.mc.player.motionY = motY;
//                        if (this.stair == 0.0) {
//                            event.setPosY(motY);
//                        }
//                        this.less += 1.0;
//                        this.slowDownHop = this.less > 1.0 && !this.slowDownHop;
//                        if (this.less > 1.15) {
//                            this.less = 1.15;
//                        }
//                    }
//                }
//                this.moveSpeed = this.getCurrentSpeed(stage) + 0.0335;
//                this.moveSpeed *= 0.85;
//                if (this.stair > 0.0) {
//                    this.moveSpeed *= 1.0;
//                }
//                if (this.slowDownHop) {
//                    this.moveSpeed *= 0.8575;
//                }
//                if (Speed.mc.player.isInWater()) {
//                    this.moveSpeed = 0.351;
//                }
//                if (MoveUtils.isMoving()) {
//                    MoveUtils.setSpeed((float) this.moveSpeed);
//                }
//                ++stage;
//            }
            if (mode.equalsIgnoreCase("NCPExploit")){
                ++timerDelay;
                timerDelay %= 5;
                EntityPlayerSP thePlayer = Minecraft.player;
                if (timerDelay != 0) {
                    Minecraft.timer.timerSpeed = 1f;
                } else {
                    if (MovementHelper.isMoving()) Minecraft.timer.timerSpeed = 32767f;
                    if (MovementHelper.isMoving()) {
                        Minecraft.timer.timerSpeed = 1.3f;
                        Minecraft.player.motionX *= 1.0199999809265137;
                        Minecraft.player.motionZ *= 1.0199999809265137;
                    }
                }

                if (Minecraft.player.onGround && MovementHelper.isMoving()) level = 2;
                if (Math.round(Minecraft.player.posY - Minecraft.player.posY) == Math.round(0.138)) {

                    thePlayer.motionY -= 0.08;
                    event.setPosY(event.getPosY() - 0.09316090325960147);
                    thePlayer.posY -= 0.09316090325960147;
                }
                if (level == 1 && (Minecraft.player.moveForward != 0.0f || Minecraft.player.moveStrafing != 0.0f)) {
                    level = 2;
                    moveSpeed = 1.35 * MovementHelper.getBaseMoveSpeed() - 0.01;
                } else if (level == 2) {
                    level = 3;
                    Minecraft.player.motionY = 0.399399995803833;
                    event.setPosY(0.399399995803833);
                    moveSpeed *= 2.149;
                } else if (level == 3) {
                    level = 4;
                    float difference = (float) (0.66 * (lastDist - MovementHelper.getBaseMoveSpeed()));
                    moveSpeedn = lastDist - difference;
                } else {
                    if (!(Minecraft.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0.0, Minecraft.player.motionY, 0.0)).isEmpty()) || Minecraft.player.isCollidedVertically) level = 1;
                    moveSpeedn = (float) (lastDist - lastDist / 159.0);
                }
                moveSpeed = Math.max(moveSpeed, MovementHelper.getBaseMoveSpeed());
                MovementInput movementInput = Minecraft.player.movementInput;
                float forward = movementInput.moveForward;
                float strafe = movementInput.moveStrafe;
                float yaww = Minecraft.player.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.setPosX(0.0);
                    event.setPosZ(0.0);
                } else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        yaw += ((forward > 0.0f) ? -45 : 45);
                        strafe = 0.0f;
                    } else if (strafe <= -1.0f) {
                        yaw += ((forward > 0.0f) ? 45 : -45);
                        strafe = 0.0f;
                    }
                    if (forward > 0.0f) {
                        forward = 1.0f;
                    } else if (forward < 0.0f) {
                        forward = -1.0f;
                    }
                }
                double mx2 = Math.cos(Math.toRadians(yaw + 90.0f));
                double mz2 = Math.sin(Math.toRadians(yaw + 90.0f));
                event.setPosX(forward * moveSpeed * mx2 + strafe * moveSpeed * mz2);
                event.setPosZ(forward * moveSpeed * mz2 - strafe * moveSpeed * mx2);
                Minecraft.player.stepHeight = 0.6f;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.setPosX(0.0);
                    event.setPosZ(0.0);
                }
            }
            if (speedMode.getOptions().equalsIgnoreCase("Reallyworld")) {
                if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                    return;
                }

                if (Minecraft.player.onGround && !Minecraft.gameSettings.keyBindJump.pressed) {
                    Minecraft.player.jump();
                }
                if (Minecraft.player.ticksExisted % 3 == 0) {
                    Minecraft.timer.timerSpeed = 1.3f;
                } else {
                    Minecraft.timer.timerSpeed = 1.f;
                }
                if (Minecraft.player.motionY == -0.4448259643949201D && Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.9D, Minecraft.player.posZ)).getBlock() != Blocks.AIR) {
                    Minecraft.player.jumpMovementFactor = 0.05F;
                    if(Minecraft.player.ticksExisted % 2 == 0) {
                        Minecraft.player.motionX *= 2D;
                        Minecraft.player.motionZ *= 2D;
                    } else {
                        MovementHelper.setMotion(MovementHelper.getSpeed() * 1 + (0.22f));

                    }
                }
            }
            if (mode.equalsIgnoreCase("HypixelHop")) {
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                        float speed = (MovementHelper.getSpeed() < 0.56f) ? MovementHelper.getSpeed() * 1.045f : 0.56f;
                        if (Minecraft.player.onGround) {
                            MovementHelper.strafePlayer(speed);
                            return;
                        }
                    }else if (Minecraft.player.motionY < 0.2f){
                        float speed = (MovementHelper.getSpeed() < 0.56f) ? MovementHelper.getSpeed() * 1.045f : 0.56f;
                        Minecraft.player.motionY -= 0.02;
                        MovementHelper.strafePlayer(MovementHelper.getSpeed() * 1.01889f);
                    }
                }else {
                    Minecraft.player.motionZ = 0.0f;
                    Minecraft.player.motionX = Minecraft.player.motionZ;
                }
            }
            if (mode.equalsIgnoreCase("Strafe")) {
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                    }
                    MovementHelper.setSpeed(MovementHelper.getSpeed() * 1.05f);
                }
            }
            if (mode.equalsIgnoreCase("SunriseStrafe2")) {
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                    }
                    MovementHelper.setSpeed(MovementHelper.getSpeed() * 0.99f);
                }
            }
            if (mode.equalsIgnoreCase("SunriseNew")) {
                if (Minecraft.player.ticksExisted % 2 == 0) {
                    if (strafing.getCurrentValue()) {
                        MovementHelper.setSpeed(MovementHelper.getSpeed());
                    }
                    if (MovementHelper.isMoving()) {
                        if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                            return;
                        }
                        if (timerExploit.getCurrentValue()) {
                            Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                        }
                        Minecraft.player.jumpMovementFactor = (float) ((double) Minecraft.player.jumpMovementFactor * 0.94f);
                        x = Minecraft.player.posX;
                        y = Minecraft.player.posY;
                        z = Minecraft.player.posZ;
                        yaw = (double) Minecraft.player.rotationYaw * 0.017453292;
                        MovementHelper.strafePlayer(MovementHelper.getBaseMoveSpeed() * 1.01f);
                        if (Minecraft.player.onGround) {
                            Minecraft.timer.timerSpeed = 0.97f;
                            boostTick = 45;
                            Minecraft.player.jump();
                        } else if (boostTick < 45) {
                            ++boostTick;
                        } else {
                            if (timerHelper.hasReached(321.0f)) {
                                Minecraft.player.onGround = false;
                                Minecraft.player.motionX *= (double) (1.0f + motionMultiplier.getCurrentValue()) + 0.1f;
                                Minecraft.player.motionZ *= (double) (1.0f + motionMultiplier.getCurrentValue()) + 0.1f;
                                Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003f, y, z + Math.cos(yaw) * 0.003f);
                                timerHelper.reset();
                            }
                            boostTick = 0;
                        }
                    }
                }
            }
            if (mode.equalsIgnoreCase("SunriseLegit")) {
                if (Minecraft.player.ticksExisted % 2 == 0) {
                    if (strafing.getCurrentValue()) {
                        MovementHelper.setSpeed(MovementHelper.getSpeed());
                    }
                    if (MovementHelper.isMoving()) {
                        if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                            return;
                        }
                        if (timerExploit.getCurrentValue()) {
                            Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                        }
                        Minecraft.player.jumpMovementFactor = (float) ((double) Minecraft.player.jumpMovementFactor * 0.94f);
                        x = Minecraft.player.posX;
                        y = Minecraft.player.posY;
                        z = Minecraft.player.posZ;
                        yaw = (double) Minecraft.player.rotationYaw * 0.017453292;
                        if (Minecraft.player.onGround) {
                            Minecraft.timer.timerSpeed = 0.97f;
                            boostTick = 45;
                            MovementHelper.strafePlayer();
                            Minecraft.player.jump();
                        } else if (boostTick < 45) {
                            ++boostTick;
                        } else {
                            if (timerHelper.hasReached(321.0f)) {
                                Minecraft.player.onGround = false;
                                Minecraft.player.motionX *= (double) (1.0f + motionMultiplier.getCurrentValue()) + 0.1f;
                                Minecraft.player.motionZ *= (double) (1.0f + motionMultiplier.getCurrentValue()) + 0.1f;
                                Minecraft.player.setPosition(x - Math.sin(yaw) * 0.0003f, y, z + Math.cos(yaw) * 0.0003f);
                                timerHelper.reset();
                            }
                            boostTick = 0;
                        }
                    }
                }
            }
            if (mode.equalsIgnoreCase("Reallyworld4")) {
                if (strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                        return;
                    }
                    if (timerExploit.getCurrentValue()) {
                        Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    Minecraft.player.jumpMovementFactor = (float)((double) Minecraft.player.jumpMovementFactor * 1.04);
                    x = Minecraft.player.posX;
                    y = Minecraft.player.posY;
                    z = Minecraft.player.posZ;
                    yaw = (double) Minecraft.player.rotationYaw * 0.017453292;
                    if (Minecraft.player.onGround) {
                        Minecraft.timer.timerSpeed = 1.3f;
                        boostTick = 11;
                        MovementHelper.strafePlayer();
                        Minecraft.player.jump();
                    } else if (boostTick < 11) {
                        ++boostTick;
                    } else {
                        if (timerHelper.hasReached(260.0f)) {
                            Minecraft.player.onGround = false;
                            Minecraft.player.motionX *= (double)(1.0f + motionMultiplier.getCurrentValue()) + 0.4;
                            Minecraft.player.motionZ *= (double)(1.0f + motionMultiplier.getCurrentValue()) + 0.4;
                            Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                            timerHelper.reset();
                        }
                        boostTick = 0;
                    }
                }
            }
//            if (mode.equals("Reallyworld") && mc.player.fallDistance <= 1) {
//                if (MovementHelper.isMoving()) {
//                    if (mc.player.onGround) {
//                        this.boostTick = 1;
//                        mc.player.jump();
//                    } else if (this.boostTick < 1) {
//                        this.boostTick++;
//                    } else {
//                        if (timerHelper.hasReached(460F) && mc.player.fallDistance >= 0.05f && mc.player.fallDistance <= 0.9f) {
//                            mc.player.motionX *= 1 + 0.14;
//                            mc.player.motionZ *= 1 + 0.14;
//                            timerHelper.reset();
//                        }
//                        this.boostTick = 0;
//                    }
//                }
//            }
            if (mode.equalsIgnoreCase("Matrix new") && Minecraft.player.fallDistance <= 1) {
                if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                    return;
                }

                x = Minecraft.player.posX;
                y = Minecraft.player.posY;
                z = Minecraft.player.posZ;
                yaw = (double) Minecraft.player.rotationYaw * 0.017453292D;
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                    Minecraft.timer.timerSpeed = 1.3F;
                    ticks = 11.0F;
                } else if (ticks < 11.0F) {
                    ++ticks;
                }

                if (Minecraft.player.motionY == -0.4448259643949201D) {
                    var10000 = Minecraft.player;
                    var10000.motionX *= 2.0D;
                    var10000 = Minecraft.player;
                    var10000.motionZ *= 2.0D;
                    Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003D, y, z + Math.cos(yaw) * 0.003D);
                }

                ticks = 0.0F;
            }
            if (mode.equalsIgnoreCase("MatrixExploit")) {
                if (Minecraft.player.onGround) {
                    if (Minecraft.player.ticksExisted % 2 == 0) {
                        if (plus <= 3.2f) {
                            plus += 0.3f;
                        }
                    }
                    if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                        float yawe = Minecraft.player.rotationYaw * 0.0174532920F;
                        Minecraft.player.motionX -= MathHelper.sin(yawe) * (plus / 5);
                        Minecraft.player.motionZ += MathHelper.cos(yawe) * (plus / 5);
                    }
                    if (Minecraft.gameSettings.keyBindBack.isKeyDown()) {
                        float yawe= Minecraft.player.rotationYaw * 0.0174532920F;
                        Minecraft.player.motionX -= -MathHelper.sin(yawe) * (plus / 5);
                        Minecraft.player.motionZ += -MathHelper.cos(yawe) * (plus / 5);
                    }
                }


                ticks = 0.0F;
            }
            if (mode.equalsIgnoreCase("Motion")) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindForward.pressed) {
                    Minecraft.player.jump();
                }
                Minecraft.player.jumpMovementFactor = speed.getNumberValue();
            }

            if (mode.equalsIgnoreCase("MatrixGround 6.2.2")) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindForward.pressed) {
                    Minecraft.player.jump();
                    Minecraft.player.onGround = false;
                }

                if (Minecraft.player.motionY > 0 && !Minecraft.player.isInWater()) {
                    Minecraft.player.motionY -= speed.getNumberValue();
                }
            }else if (mode.equalsIgnoreCase("ReallyworldTimer") && Minecraft.player.fallDistance <= 1) {
                if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                    return;
                }

                x = Minecraft.player.posX;
                y = Minecraft.player.posY;
                z = Minecraft.player.posZ;
                yaw = (double) Minecraft.player.rotationYaw * 0.017453292D;
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                    Minecraft.timer.timerSpeed = 1.3F;
                    ticks = 11.0F;
                } else if (ticks < 11.0F) {
                    ++ticks;
                }

                if (Minecraft.player.motionY == -0.4448259643949201D) {
                    var10000 = Minecraft.player;
                    var10000.motionX *= 2.0D;
                    var10000 = Minecraft.player;
                    var10000.motionZ *= 2.0D;
                    Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003D, y, z + Math.cos(yaw) * 0.003D);
                }

                ticks = 0.0F;
            }
            if (mode.equalsIgnoreCase("Reallyworld3")|| mode.equalsIgnoreCase("Matrix Long")) {
                if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid()) {
                    return;
                }
                boolean a = false;
                boolean b = false;
                if (Minecraft.player.fallDistance > 1.430f) {
                    a = false;
                }
                if (Minecraft.player.onGround || Minecraft.player.fallDistance <= 0.230f) {
                    a = true;
                }
                if (a && Minecraft.player.fallDistance <= 1.430f) {
                    b = true;
                }
                if (b) {
                    x = Minecraft.player.posX;
                    y = Minecraft.player.posY;
                    z = Minecraft.player.posZ;
                    yaw = (double) Minecraft.player.rotationYaw * 0.017453292D;
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                        ticks = 10.0F;
                    } else if (ticks < 10.0F) {
                        ++ticks;
                    }

                    if (Minecraft.player.motionY == -0.4448259643949201D) {
                        var10000 = Minecraft.player;
                        var10000.motionX *= 2.7999999523162842D;
                        var10000 = Minecraft.player;
                        var10000.motionZ *= 2.6999999523162842D;
                        Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003D, y, z + Math.cos(yaw) * 0.003D);
                    }
                    ticks = 0.0F;
                }
            }
            if (mode.equalsIgnoreCase("NexusGriefFast")) {
                if (MovementHelper.isMoving()) {
                    MovementHelper.strafePlayer(getSpeed1());
                    if (Minecraft.player.onGround) {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 6.8D / 24.5D, 0.0D, Math.cos(MovementHelper.getDirection()) * 6.8D / 24.5D);
                        MovementHelper.strafePlayer(getSpeed1());
                    } else if (Minecraft.player.isInWater()) {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 7.5D / 24.5D, 0.0D, Math.cos(MovementHelper.getDirection()) * 7.5D / 24.5D);
                        MovementHelper.strafePlayer(getSpeed1());
                    } else if (!Minecraft.player.onGround) {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection2()) * 0.11D / 24.5D, 0.0D, Math.cos(MovementHelper.getDirection2()) * 0.11D / 24.5D);
                        MovementHelper.strafePlayer(getSpeed1());
                    } else {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 0.005D * (double)MovementHelper.getSpeed(), 0.0D, Math.cos(MovementHelper.getDirection()) * 0.005D * (double)MovementHelper.getSpeed());
                        MovementHelper.strafePlayer(getSpeed1());
                    }
                }
            }
            if (mode.equalsIgnoreCase("Custom")) {
                if (!Minecraft.player.isPotionActive(MobEffects.SPEED) && potionCheck.getBoolValue())
                    return;
                if (Minecraft.player.onGround && !onGround.getBoolValue() && MovementHelper.isMoving()) {
                    if (boost.getBoolValue()) {
                        Minecraft.player.onGround = false;
                    }
                    Minecraft.player.motionY = motionY.getNumberValue();
                }
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                Minecraft.player.jumpMovementFactor = jumpMoveFactor.getNumberValue();
                if (onGround.getBoolValue()) {
                    MovementHelper.setSpeed(onGroundSpeed.getNumberValue());
                }
                if (!timerExploit.getBoolValue()) {
                    Minecraft.timer.timerSpeed = timer.getNumberValue();
                }
                if (timerExploit.getBoolValue()) {
                    Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 60 > 39 ? 1000 : 1;
                }
            } else if (mode.equalsIgnoreCase("Matrix 6.2.2")) {
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                x = Minecraft.player.posX;
                y = Minecraft.player.posY;
                z = Minecraft.player.posZ;
                yaw = Minecraft.player.rotationYaw * 0.017453292;
                if (MovementHelper.isMoving() && Minecraft.player.fallDistance < 0.1) {
                    ItemStack stack = Minecraft.player.getHeldItemOffhand();
                    if (Minecraft.player.isInWeb || Minecraft.player.isOnLadder() || Minecraft.player.isInLiquid() || Minecraft.player.isCollidedHorizontally) {
                        return;
                    }
                    boolean isSpeed = Minecraft.player.isPotionActive(MobEffects.SPEED);
                    boolean isRune = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат");
                    boolean isRuneAndSpeed = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("небесных врат") && Minecraft.player.isPotionActive(MobEffects.SPEED);
                    if (Minecraft.player.onGround) {
                        boostTick = 8;
                        Minecraft.player.jump();
                    } else if (boostTick < 8) {
                        Minecraft.player.jumpMovementFactor *= 1.04F;
                        if (boostTick == 0) {
                            double motion = isRuneAndSpeed ? 1.67 : isSpeed ? 1.7 : isRune ? 1.7 : 1.7605;
                            Minecraft.player.motionX *= motion;
                            Minecraft.player.motionZ *= motion;
                            Minecraft.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                        } else {
                            Minecraft.player.motionY -= 0.0098;
                        }
                        boostTick++;
                    } else {
                        boostTick = 0;
                    }
                } else if (mode.equalsIgnoreCase("Matrix 6.0.4")) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                    } else {
                        if (Minecraft.player.ticksExisted % 5 == 0) {
                            Minecraft.player.jumpMovementFactor = 0.0F;
                            Minecraft.timer.timerSpeed = 0.6F;
                        }

                        if (Minecraft.player.ticksExisted % 5 == 0) {
                            Minecraft.player.jumpMovementFactor = 0.28F;
                            Minecraft.timer.timerSpeed = 1.0F;
                        }

                        if (Minecraft.player.ticksExisted % 10 == 0) {
                            Minecraft.player.jumpMovementFactor = 0.38F;
                        }

                        if (Minecraft.player.ticksExisted % 20 == 0) {
                            Minecraft.player.jumpMovementFactor = 0.35F;
                            Minecraft.timer.timerSpeed = 1.1F;
                        }
                    }
                }
            } else if (mode.equalsIgnoreCase("Old Sunrise")) {
                if (Minecraft.player.onGround) {
                    Minecraft.player.jump();
                } else {
                    if (Minecraft.player.ticksExisted % 5 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.0F;
                        Minecraft.timer.timerSpeed = 0.6F;
                    }

                    if (Minecraft.player.ticksExisted % 5 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.28F;
                        Minecraft.timer.timerSpeed = 1.0F;
                    }

                    if (Minecraft.player.ticksExisted % 10 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.38F;
                    }

                    if (Minecraft.player.ticksExisted % 20 == 0) {
                        Minecraft.player.jumpMovementFactor = 0.35F;
                        Minecraft.timer.timerSpeed = 1.3F;
                    }
                }
            } else if (mode.equalsIgnoreCase("Matrix Timer")) {
                Minecraft.gameSettings.keyBindJump.pressed = false;
                if (strafing.getBoolValue()) {
                    MovementHelper.strafePlayer(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                    }
                    if (!Minecraft.player.onGround && Minecraft.player.fallDistance <= 0.1F) {
                        Minecraft.timer.timerSpeed = 1.21F;
                    }
                    if (Minecraft.player.fallDistance > 0.1F && Minecraft.player.fallDistance < 1.3F) {
                        Minecraft.timer.timerSpeed = 1F;
                    }
                }
            } else if (mode.equalsIgnoreCase("MatrixOnGround")) {
                //3eLeHyy / MyLifeIsShit
                if (Minecraft.player.onGround) {
                    Minecraft.timer.timerSpeed = 1.05F;
                    Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + 1.00000000000E-14F, Minecraft.player.posZ);
                    Minecraft.player.motionX *= 1.55F;
                    Minecraft.player.motionZ *= 1.55F;
                    Minecraft.player.motionY = 0f;
                    Minecraft.player.onGround = true;
                } else if (Minecraft.player.fallDistance > 0) {
                    Minecraft.player.motionY = -10000;
                }
            } else if (mode.equalsIgnoreCase("MatrixHop")) {
                if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                    if (Minecraft.player.onGround) {
                        Minecraft.player.jump();
                        Minecraft.timer.timerSpeed = 1.05F;
                        Minecraft.player.motionX *= 1.0071;
                        Minecraft.player.motionZ *= 1.0071;
                        Minecraft.player.moveStrafing *= 2;
                    } else {
                        if (!Minecraft.player.onGround && Minecraft.player.fallDistance <= 0.1F) {
                            Minecraft.player.motionY -= 0.0098;
                        }
                    }
                }
            }else if (mode.equalsIgnoreCase("Guardian")) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindForward.isKeyDown() || Minecraft.player.onGround &&  Minecraft.gameSettings.keyBindBack.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindLeft.isKeyDown() || Minecraft.player.onGround && Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                    if (GuardianBoost < 0.15f) {
                        if (Minecraft.player.ticksExisted % 2 == 0) {
                            GuardianBoost += 0.005f;
                        }
                    }
                    if (guardianMode.currentMode.equalsIgnoreCase("Legit")) {
                        MovementHelper.setSpeedD(1.51f / 5f);
                    }else if (guardianMode.currentMode.equalsIgnoreCase("Rage")){
                        float rageplus = rageStart.getCurrentValue();
                        float rightSpeed = 2.41f;
                        if (MovementHelper.isMoving()) {
                            if (Minecraft.player.ticksExisted % 2.5 == 0 && rageplus < rageFinal.getNumberValue()) {
                                rageplus += rageBoost.getCurrentValue();
                            }

                        }
                        if (!Minecraft.player.onGround) {
                            if (Minecraft.player.ticksExisted % 2.5 == 0) {
                                rageplus -= 0.4f;
                            }

                            rightSpeed = 1.88f;
                        }else{
                            rightSpeed = 2.41f;
                        }


                        MovementHelper.setSpeedD((rightSpeed + rageplus) / 5f);
                    }
                }else if (Minecraft.gameSettings.keyBindForward.isKeyDown() || Minecraft.gameSettings.keyBindBack.isKeyDown() || Minecraft.gameSettings.keyBindLeft.isKeyDown() || Minecraft.gameSettings.keyBindRight.isKeyDown()){
                    if (strafing.getCurrentValue()) {
                        if (Minecraft.player.ticksExisted % 1.5 == 0) {
                            Minecraft.player.motionY -= 0.0005;
                        }
                        MovementHelper.strafePlayer();
                    }
                }
            }else if (mode.equalsIgnoreCase("Damage")) {
                if (MovementHelper.isMoving()) {
                    if (strafing.get())
                        MoveUtils.setStrafe(MoveUtils.getSpeed());
                    if (Minecraft.player.onGround) {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 9.8 / 24.5, 0, Math.cos(MovementHelper.getDirection()) * 9.8 / 24.5);
                        //MovementHelper.strafePlayer();
                    } else if (Minecraft.player.isInWater()) {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 8.5 / 24.5, 0, Math.cos(MovementHelper.getDirection()) * 9.5 / 24.5);
                        //MovementHelper.strafePlayer();
                    } else if (!Minecraft.player.onGround) {
                        //mc.player.addVelocity(-Math.sin(MovementHelper.getDirection2()) * 0.11 / 24.5, 0, Math.cos(MovementHelper.getDirection2()) * 0.11 / 24.5);
                        //MovementHelper.strafePlayer();
                    } else {
                        Minecraft.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 0.005 * MovementHelper.getSpeed(), 0, Math.cos(MovementHelper.getDirection()) * 0.005 * MovementHelper.getSpeed());
                        //MovementHelper.strafePlayer();

                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            if (boost.getBoolValue()) {
                Minecraft.player.onGround = false;
            }
            Minecraft.gameSettings.keyBindJump.pressed = false;
            Minecraft.timer.timerSpeed = 6.78F;
            Minecraft.player.jumpMovementFactor *= 1.04F;
            if (Minecraft.player.motionY > 0 && !Minecraft.player.onGround) {
                Minecraft.player.motionY -= 0.00994;
            } else {
                Minecraft.player.motionY -= 0.00995;
            }
        }
    }

    @EventTarget
    public void onUpdateTwo(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer") || (mode.equalsIgnoreCase("Custom") && blink.getBoolValue())) {
            synchronized (positions) {
                positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY, Minecraft.player.posZ});
            }
            if (pulseTimer.hasReached(600)) {
                blink();
                pulseTimer.reset();
            }
        }
    }

    @Override
    public void onEnable() {

        boostTick = 0;
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            synchronized (positions) {
                positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY + Minecraft.player.getEyeHeight() / 2.0f, Minecraft.player.posZ});
                positions.add(new double[]{Minecraft.player.posX, Minecraft.player.getEntityBoundingBox().minY, Minecraft.player.posZ});
            }
        }
        if (mode.equalsIgnoreCase("NCP")) {
            Minecraft.timer.timerSpeed = 1f;
            level = (Minecraft.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0.0, Minecraft.player.motionY, 0.0)).size() > 0 || Minecraft.player.isCollidedVertically) ? 1 : 4;
        }
        if (mode.equalsIgnoreCase("MatrixExploit")) {
            /*
            FindItemResult elytra = InvUtils.find(Items.ELYTRA);
            if (!elytra.found()) {
                this.error("Elytra not found");
                this.toggle();
            }
            else {
                tick = 0;
            }

             */
        }
        super.onEnable();
    }

    @EventTarget
    public void onMotion(EventPreMotion event) {
        double xDist = Minecraft.player.posX - Minecraft.player.prevPosX;
        double zDist = Minecraft.player.posZ - Minecraft.player.prevPosZ;
        lastDist = (float) Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            if (Minecraft.player == null || !(event.getPacket() instanceof CPacketPlayer) || disableLogger) {
                return;
            }
            event.setCancelled(true);
            if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
                return;
            }
            packets.add(event.getPacket());
        }
    }

    private void blink() {
        try {
            disableLogger = true;
            Iterator<Packet<?>> packetIterator = packets.iterator();
            while (packetIterator.hasNext()) {
                Minecraft.player.connection.sendPacket(packetIterator.next());
                packetIterator.remove();
            }
            disableLogger = false;
        } catch (Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }
        synchronized (positions) {
            positions.clear();
        }
    }

    @Override
    public void onDisable() {
        Minecraft.timer.timerSpeed = 1.0F;
        Minecraft.player.speedInAir = 0.02F;
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("YPort")) {
            Minecraft.player.motionX = 0;
            Minecraft.player.motionZ = 0;
        }
        if (mode.equalsIgnoreCase("NCPExploit")) {
            Minecraft.timer.timerSpeed = 1f;
            moveSpeedn = MovementHelper.getBaseMoveSpeed();
            level = 0;
        }
        if (mode.equalsIgnoreCase("Matrix Timer")) {
            blink();
        }
        super.onDisable();
    }
}
