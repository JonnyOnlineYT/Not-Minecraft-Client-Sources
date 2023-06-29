/*
 * Decompiled with CFR 0.150.
 */
package ru.rockstar.api.utils.movement;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.api.event.event.EventStrafe;
import ru.rockstar.api.event.event.MoveEvent;
import ru.rockstar.client.features.impl.combat.KillAura;

public class MovementHelper {
    public static double WALK_SPEED = 0.221;
    public static Minecraft mc = Minecraft.getMinecraft();

    public static int getJumpBoostModifier() {
        PotionEffect effect = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST);
        if (effect != null) {
            return effect.getAmplifier() + 1;
        }
        return 0;
    }
    public static void setEventSpeed(MoveEvent event, double speed) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            event.setX(0);
            event.setZ(0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
    
    public static double[] forward(final double speed) {
        float forward = MovementHelper.mc.player.movementInput.moveForward;
        float side = MovementHelper.mc.player.movementInput.moveStrafe;
        float yaw = MovementHelper.mc.player.prevRotationYaw + (MovementHelper.mc.player.rotationYaw - MovementHelper.mc.player.prevRotationYaw) * MovementHelper.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static float getPlayerDirection() {
        float rotationYaw = MovementHelper.mc.player.rotationYaw;
        if (MovementHelper.mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementHelper.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementHelper.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MovementHelper.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementHelper.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static float getEntityDirection(final EntityLivingBase entity) {
        float rotationYaw = entity.rotationYaw;
        if (entity.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (entity.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (entity.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (entity.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (entity.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return (float)Math.toRadians(rotationYaw);
    }

    public static void calculateSilentMove(EventStrafe event, float yaw) {
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        int difference = (int) ((MathHelper.wrapDegrees(mc.player.rotationYaw - yaw - 23.5F - 135) + 180) / 45);
        float calcForward = 0F;
        float calcStrafe = 0F;
        switch (difference) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
        }
        if (calcForward > 1F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1F || calcForward > -0.9F && calcForward < -0.3F) {
            calcForward *= 0.5F;
        }
        if (calcStrafe > 1F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1F || calcStrafe > -0.9F && calcStrafe < -0.3F) {
            calcStrafe *= 0.5F;
        }
        float dist = calcStrafe * calcStrafe + calcForward * calcForward;
        if (dist >= 1E-4F) {
            dist = (float) (friction / Math.max(1, Math.sqrt(dist)));
            calcStrafe *= dist;
            calcForward *= dist;
            float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180F));
            float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180F));
            mc.player.motionX += calcStrafe * yawCos - calcForward * yawSin;
            mc.player.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    public static boolean isBlockAbove() {
        for (double height = 0.0; height <= 1.0; height += 0.5) {
            List<AxisAlignedBB> collidingList = MovementHelper.mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, height, 0.0));
            if (collidingList.isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float factor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            factor = 1;
        if (mc.player.movementInput.moveForward < 0)
            factor = -1;

        if (factor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * factor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * factor;
        }

        if (factor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }
    public static float getDirection2() {
        Minecraft mc = Minecraft.getMinecraft();
        float var1 = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            forward = -50.5f;
        } else if (mc.player.moveForward > 0.0f) {
            forward = 50.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            var1 -= 22.0f * forward;
        }
        if (mc.player.moveStrafing < 0.0f) {
            var1 += 22.0f * forward;
        }
        return var1 *= (float)Math.PI / 180;
    }

    public static double getXDirAt(float angle) {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = 90.0;
        return Math.cos((rot += angle) * Math.PI / 180.0);
    }

    public static double getZDirAt(float angle) {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = 90.0;
        return Math.sin((rot += angle) * Math.PI / 180.0);
    }

    public static void setSpeedAt(MoveEvent e, float angle, double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            if (mc.player.onGround) {
                if (!(mc.player.getDistanceToEntity(KillAura.target) <= 1.0f)) {
                    e.setX(MovementHelper.getXDirAt(angle) * speed);
                    e.setZ(MovementHelper.getZDirAt(angle) * speed);
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isOnGround() {
        if (!mc.player.onGround) return false;
        return mc.player.isCollidedVertically;
    }

    public static void setMotion(MoveEvent e, double speed, float pseudoYaw, double aa, double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? -45 : 45);
            } else if (aa < 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            } else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }
    public static void setSpeed(double d, float f, double d2, double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;
        if (d4 == 0.0 && d5 == 0.0) {
            mc.player.motionZ = 0.0;
            mc.player.motionX = 0.0;
        } else {
            if (d4 != 0.0) {
                if (d5 > 0.0) {
                    f2 += (float) (d4 > 0.0 ? -45 : 45);
                } else if (d5 < 0.0) {
                    f2 += (float) (d4 > 0.0 ? 45 : -45);
                }
                d5 = 0.0;
                if (d4 > 0.0) {
                    d4 = 1.0;
                } else if (d4 < 0.0) {
                    d4 = -1.0;
                }
            }
            double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
            double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
            mc.player.motionX = d4 * d * d6 + d5 * d * d7;
            mc.player.motionZ = d4 * d * d7 - d5 * d * d6;
        }
    }

    public static void setSpeed(double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void strafe() {
        MovementHelper.strafe(MovementHelper.getSpeed());
    }

    public static float getSpeed() {
        return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }
    
    public static float getEntitySpeed(Entity entity) {
        return (float)Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isMoving() {
        if (mc.player == null) return false;
        if (MovementInput.moveForward != 0.0f) return true;
        return MovementInput.moveStrafe != 0.0f;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean hasMotion() {
        if (mc.player.motionX == 0.0) return false;
        if (mc.player.motionZ == 0.0) return false;
        return mc.player.motionY != 0.0;
    }

    public static void strafe(float speed) {
        if (!MovementHelper.isMoving()) {
            return;
        }
        double yaw = MovementHelper.getDirection();
        mc.player.motionX = -Math.sin(yaw) * (double)speed;
        mc.player.motionZ = Math.cos(yaw) * (double)speed;
    }

    public static double getMoveSpeed(MoveEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        double xspeed = e.getX();
        double zspeed = e.getZ();
        return Math.sqrt(xspeed * xspeed + zspeed * zspeed);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean moveKeysDown() {
        Minecraft mc = Minecraft.getMinecraft();
        if (MovementInput.moveForward != 0.0f) return true;
        return MovementInput.moveStrafe != 0.0f;
    }

    public static double getPressedMoveDir() {
        Minecraft mc = Minecraft.getMinecraft();
        double rot = Math.atan2(mc.player.moveForward, mc.player.moveStrafing) / Math.PI * 180.0;
        if (rot == 0.0) {
            if (mc.player.moveStrafing == 0.0f) {
                rot = 90.0;
            }
        }
        return (rot += mc.player.rotationYaw) - 90.0;
    }

    public static double getPlayerMoveDir() {
        Minecraft mc = Minecraft.getMinecraft();
        double xspeed = mc.player.motionX;
        double zspeed = mc.player.motionZ;
        double direction = Math.atan2(xspeed, zspeed) / Math.PI * 180.0;
        return -direction;
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + 2.5, mc.player.posZ - 0.3);
        return !MovementHelper.mc.world.getCollisionBoxes(mc.player, bb).isEmpty();
    }

    public static void setMotionEvent(MoveEvent event, double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static void startFakePos() {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        mc.player.setPosition(mc.player.posX, mc.player.posY + 0.3, mc.player.posZ);
        Minecraft.getMinecraft();
        double x = mc.player.posX;
        Minecraft.getMinecraft();
        double y = mc.player.posY;
        Minecraft.getMinecraft();
        double z = mc.player.posZ;
        for (int i = 0; i < 3000; ++i) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.09999999999999, z, false));
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, true));
        }
        Minecraft.getMinecraft();
        mc.player.motionY = 0.0;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}
