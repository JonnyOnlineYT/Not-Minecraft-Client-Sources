package org.moonware.client.helpers.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.motion.EventStrafe;
import org.moonware.client.helpers.Helper;

public class MovementHelper implements Helper {

    public static boolean isMoving() {
        return Minecraft.player.movementInput.moveForward != 0 || Minecraft.player.movementInput.moveStrafe != 0;
    }
    public static void setMotion(double speed) {
        double forward = Minecraft.player.movementInput.moveForward;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        float yaw = Minecraft.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            Minecraft.player.motionX = 0.0D;
            Minecraft.player.motionZ = 0.0D;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (float) (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (float) (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            Minecraft.player.motionX = forward * speed * cos + strafe * speed * sin;
            Minecraft.player.motionZ = forward * speed * sin - strafe * speed * cos;
        }
    }

    public static double[] forward(double speed) {
        float forward = Minecraft.player.movementInput.moveForward;
        float side = Minecraft.player.movementInput.moveStrafe;
        float yaw = Minecraft.player.prevRotationYaw + (Minecraft.player.rotationYaw - Minecraft.player.prevRotationYaw) * Helper.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static float getBaseMoveSpeed() {
        float baseSpeed = 0.2873F;
        if (Minecraft.player != null && Minecraft.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Minecraft.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    public static boolean airBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(Minecraft.player.posX - 0.3, Minecraft.player.posY + (double) Minecraft.player.getEyeHeight(), Minecraft.player.posZ + 0.3, Minecraft.player.posX + 0.3, Minecraft.player.posY + (!Minecraft.player.onGround ? 1.5 : 2.5), Minecraft.player.posZ - 0.3);
        return Minecraft.world.getCollisionBoxes(Minecraft.player, bb).isEmpty();
    }
    public static boolean isInsideBlock2() {
        for (int i = MathHelper.floor(Minecraft.player.boundingBox.minX); i < MathHelper.floor(Minecraft.player.boundingBox.maxX) + 1; ++i) {
            for (int j = MathHelper.floor(Minecraft.player.boundingBox.minY + 1.0); j < MathHelper.floor(Minecraft.player.boundingBox.maxY) + 2; ++j) {
                for (int k = MathHelper.floor(Minecraft.player.boundingBox.minZ); k < MathHelper.floor(Minecraft.player.boundingBox.maxZ) + 1; ++k) {
                    Block block = Minecraft.world.getBlockState(new BlockPos(i, j, k)).getBlock();
                    if (block == null || block instanceof BlockAir) continue;
                    AxisAlignedBB axisAlignedBB = block.getSelectedBoundingBox(Minecraft.world.getBlockState(new BlockPos(i, j, k)), Minecraft.world, new BlockPos(i, j, k));
                    if (block instanceof BlockHopper) {
                        axisAlignedBB = new AxisAlignedBB(i, j, k, i + 1, j + 1, k + 1);
                    }
                    if (axisAlignedBB == null || !Minecraft.player.boundingBox.intersectsWith(axisAlignedBB)) continue;
                    return true;
                }
            }
        }
        return false;
    }
    public static void teleport(double dist) {
        double forward = Minecraft.player.movementInput.moveForward;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        float yaw = Minecraft.player.rotationYaw;
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
        double x = Minecraft.player.posX;
        double y = Minecraft.player.posY;
        double z = Minecraft.player.posZ;
        double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0f));
        double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0f));
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y, z + zspeed, true));
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y + 0.1, z + zspeed, true));
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed, y, z + zspeed, true));
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x + xspeed * 2.0, y, z + zspeed * 2.0, true));
    }
    public static boolean isBlockUnder(float value) {
        if (Minecraft.player.posY < 0.0) {
            return false;
        }
        AxisAlignedBB bb = Minecraft.player.getEntityBoundingBox().offset(0.0, -value, 0.0);
        return Minecraft.world.getCollisionBoxes(Minecraft.player, bb).isEmpty();
    }

    public static float getDirection2() {
        Minecraft mc = Minecraft.getMinecraft();
        float var1 = Minecraft.player.rotationYaw;
        if (Minecraft.player.moveForward < 0.0F) {
            var1 += 180.0F;
        }

        float forward = 1.0F;
        if (Minecraft.player.moveForward < 0.0F) {
            forward = -50.5F;
        } else if (Minecraft.player.moveForward > 0.0F) {
            forward = 50.5F;
        }

        if (Minecraft.player.moveStrafing > 0.0F) {
            var1 -= 22.0F * forward;
        }

        if (Minecraft.player.moveStrafing < 0.0F) {
            var1 += 22.0F * forward;
        }

        return var1 *= 0.017453292F;
    }
    public static float getDirection() {
        float rotationYaw = Minecraft.player.rotationYaw;
        float factor = 0.0F;
        MovementInput var10000 = Minecraft.player.movementInput;
        if (var10000.moveForward > 0.0F) {
            factor = 1.0F;
        }

        var10000 = Minecraft.player.movementInput;
        if (var10000.moveForward < 0.0F) {
            factor = -1.0F;
        }

        if (factor == 0.0F) {
            var10000 = Minecraft.player.movementInput;
            if (var10000.moveStrafe > 0.0F) {
                rotationYaw -= 90.0F;
            }

            var10000 = Minecraft.player.movementInput;
            if (var10000.moveStrafe < 0.0F) {
                rotationYaw += 90.0F;
            }
        } else {
            var10000 = Minecraft.player.movementInput;
            if (var10000.moveStrafe > 0.0F) {
                rotationYaw -= 45.0F * factor;
            }

            var10000 = Minecraft.player.movementInput;
            if (var10000.moveStrafe < 0.0F) {
                rotationYaw += 45.0F * factor;
            }
        }

        if (factor < 0.0F) {
            rotationYaw -= 180.0F;
        }

        return (float)Math.toRadians(rotationYaw);
    }
    public static float getSpeed1() {
        return (float)Math.sqrt(Minecraft.player.motionX * Minecraft.player.motionX + Minecraft.player.motionZ * Minecraft.player.motionZ);
    }

    public static int getSpeedEffect() {
        if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
            return Minecraft.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1;
        } else {
            return 0;
        }
    }

    public static float getMoveDirection() {
        double motionX = Minecraft.player.motionX;
        double motionZ = Minecraft.player.motionZ;
        float direction = (float) (Math.atan2(motionX, motionZ) / Math.PI * 180);
        return -direction;
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(Minecraft.player.posX - 0.3, Minecraft.player.posY + Minecraft.player.getEyeHeight(), Minecraft.player.posZ + 0.3, Minecraft.player.posX + 0.3, Minecraft.player.posY + (!Minecraft.player.onGround ? 1.5 : 2.5), Minecraft.player.posZ - 0.3);
        return Minecraft.world.getCollisionBoxes(Minecraft.player, axisAlignedBB).isEmpty();
    }

    public static void calculateSilentMove(EventStrafe event, float yaw) {
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        int difference = (int) ((MathHelper.wrapDegrees(Minecraft.player.rotationYaw - yaw - 23.5F - 135) + 180) / 45);
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
            Minecraft.player.motionX += calcStrafe * yawCos - calcForward * yawSin;
            Minecraft.player.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }

    public static void setEventSpeed(EventMove event, double speed) {
        double forward = Minecraft.player.movementInput.moveForward;
        double strafe = Minecraft.player.movementInput.moveStrafe;
        float yaw = Minecraft.player.rotationYaw;
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

    public static float getSpeed() {
        return (float) Math.sqrt(Minecraft.player.motionX * Minecraft.player.motionX + Minecraft.player.motionZ * Minecraft.player.motionZ);
    }

    public static void setSpeed(float speed) {
        float yaw = Minecraft.player.rotationYaw;
        float forward = Minecraft.player.movementInput.moveForward;
        float strafe = Minecraft.player.movementInput.moveStrafe;
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
        Minecraft.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
        Minecraft.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
    }
    public static void setSpeedD(float speedD5) {
        float yaw = Minecraft.player.rotationYaw;
        float forward = Minecraft.player.movementInput.moveForward;
        float strafe = Minecraft.player.movementInput.moveStrafe;
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
        Minecraft.player.motionX = (forward * speedD5 * Math.cos(Math.toRadians(yaw + 90)) + strafe * speedD5 * Math.sin(Math.toRadians(yaw + 90)));
        Minecraft.player.motionZ = (forward * speedD5 * Math.sin(Math.toRadians(yaw + 90)) - strafe * speedD5 * Math.cos(Math.toRadians(yaw + 90)));
    }

    public static double getDirectionAll() {
        float rotationYaw = Minecraft.player.rotationYaw;
        float forward = 1f;
        if (Minecraft.player.moveForward < 0f)
            rotationYaw += 180f;
        if (Minecraft.player.moveForward < 0f)
            forward = -0.5f;
        else if (Minecraft.player.moveForward > 0f)
            forward = 0.5f;
        if (Minecraft.player.moveStrafing > 0f)
            rotationYaw -= 90f * forward;
        if (Minecraft.player.moveStrafing < 0f)
            rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
    }

    public static void strafePlayer(float speed) {
        double yaw = getDirectionAll();
        float getSpeed = speed == 0 ? getSpeed() : speed;
        Minecraft.player.motionX = -Math.sin(yaw) * (getSpeed);
        Minecraft.player.motionZ = Math.cos(yaw) * (getSpeed);
    }
    public static void strafePlayer() {
        double yaw = getDirectionAll();
        float speed = getSpeed();
        Minecraft.player.motionX = -Math.sin(yaw) * (double)speed;
        Minecraft.player.motionZ = Math.cos(yaw) * (double)speed;
    }

}
