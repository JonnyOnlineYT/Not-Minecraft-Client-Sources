package ru.rockstar.client.features.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.*;
import ru.rockstar.api.event.*;
import ru.rockstar.api.event.event.*;
import ru.rockstar.api.utils.movement.*;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.*;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.ui.settings.*;
import ru.rockstar.client.ui.settings.impl.*;

import com.mojang.realmsclient.gui.*;

import net.minecraft.block.BlockAir;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;

public class Speed extends Feature
{
    private float ticks;
    private final ListSetting speedMode;
    private final NumberSetting speed;
    private final BooleanSetting lagbackCheck;
    public static BooleanSetting targetCheck = new BooleanSetting("TargetCheck", "Проверяет есть ли у киллауры таргет, только после этого использует данный модуль", false, () -> true);
    public static BooleanSetting cdCheck = new BooleanSetting("CoolDownCheck", "Проверяет полный ли кулдаун", false, () -> targetCheck.getBoolValue());
    public static boolean needSprintState = true;
    
    public Speed() {
        super("Speed", "Ускорение бега", 0, Category.MOVEMENT);
        this.ticks = 35.0f;
        this.speedMode = new ListSetting("Speed Mode", "Matrix", () -> true, new String[] { "Matrix",  "MatrixGround", "Matrix New", "NCP", "NexusGrief", "SunriseDamage", "StormDuels", "StormGrief", "Timer" });
        this.speed = new NumberSetting("Speed", 2.0f, 0.1f, 10.0f, 0.1f, () -> this.speedMode.currentMode.equalsIgnoreCase("StormDisabler"));
        this.lagbackCheck = new BooleanSetting("Lagback Check", "Возвращает назад если античит спамит чеками", false, () -> true);
        this.addSettings(this.speedMode, this.speed, this.lagbackCheck, targetCheck);
    }
    
    @EventTarget
    public void onLagbackSpeed(final EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook && this.lagbackCheck.getBoolValue() && isToggled()) {
            this.toggle();
            Main.msg("Anti-cheat discovered speedhack", true);
            NotificationPublisher.queue(ChatFormatting.RED + "Anti-Cheat", ChatFormatting.WHITE + "Anti-cheat discovered speedhack", NotificationType.WARNING);
        }
    }
    
    
    
    @EventTarget
    public void onPreMotion(final EventPreMotionUpdate event) {
        if (this.isToggled()) {
        	//if ((Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && KillAura.target == null && targetCheck.getBoolValue()) || (mc.player.getCooledAttackStrength(0.5f) >= KillAura.attackCoolDown.getNumberValue())) {
        	//	return;
        	//}
            final String mode = this.speedMode.getOptions();
            if (mode.equalsIgnoreCase("MatrixGround")) {
                event.setGround(true);
                mc.player.motionY = 0.00001;
                if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
                    mc.player.jump();
                }

                if (!mc.player.isInWater()) {
                    mc.player.motionY -= 10;
                }
            }
            if (mode.equalsIgnoreCase("Matrix")) {
            	if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                    return;
                }

                if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
                    mc.player.jump();
                }
                if (mc.player.ticksExisted % 3 == 0) {
                    mc.timer.timerSpeed = 1.5f;
                } else {
                    mc.timer.timerSpeed = 1.f;
                }
                if (mc.player.motionY == -0.4448259643949201D && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.9D, mc.player.posZ)).getBlock() != Blocks.AIR) {
                    mc.player.jumpMovementFactor = 0.05F;
                    if(mc.player.ticksExisted % 2 == 0) {
                       mc.player.motionX *= 2.5D;
                        mc.player.motionZ *= 2.5D;
                    } else {
                        MovementHelper.setSpeed(MovementHelper.getSpeed() * 1 + (0.22f));

                    }
                }
            }
            else if (mode.equalsIgnoreCase("Matrix New")) {
            	BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.0001, feet.getZ());
                if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                    return;
                }
            	if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                    return;
                }
                final double x = Speed.mc.player.posX;
                final double y = Speed.mc.player.posY;
                final double z = Speed.mc.player.posZ;
                final double yaw = Speed.mc.player.rotationYaw * 0.017453292;
                if (Speed.mc.player.onGround) {
                    Speed.mc.player.jump();
                    Speed.mc.timer.timerSpeed = 1f;
                    this.ticks = 11.0f;
                }
                else if (this.ticks < 11.0f) {
                    ++this.ticks;
                }
                if (targetCheck.getBoolValue()) {
                	boolean n = false;
        			if (KillAura.target != null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        					n = false;
        			} else {
        					n = true;
        			}
        			if (n) {
        				if (Speed.mc.player.motionY == -0.4448259643949201) {
                            final EntityPlayerSP player4 = Speed.mc.player;
                            player4.motionX *= 2;
                            final EntityPlayerSP player5 = Speed.mc.player;
                            player5.motionZ *= 2;
                            Minecraft.getMinecraft().player.setPosition(x - Math.sin(yaw) * 0.000, y, z + Math.cos(yaw) * 0.000);
                        }
        			} else {
        				mc.timer.timerSpeed = 1;
        			}
                } else {
                	if (Speed.mc.player.motionY == -0.4448259643949201) {
                        final EntityPlayerSP player4 = Speed.mc.player;
                        player4.motionX *= 2;
                        final EntityPlayerSP player5 = Speed.mc.player;
                        player5.motionZ *= 2;
                        Minecraft.getMinecraft().player.setPosition(x - Math.sin(yaw) * 0.000, y, z + Math.cos(yaw) * 0.000);
                    }
                }
                this.ticks = 0.0f;
                
            }
            else if (mode.equalsIgnoreCase("NCP")) {
            	if (MovementHelper.isMoving()) {
            		mc.timer.timerSpeed = 1.0865f;
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.speedInAir = 0.0223f;
                    }
                    MovementHelper.strafe(0.37f);
                } else {
                	mc.timer.timerSpeed = 1.0f;
                    mc.player.motionX = 0.0;
                    mc.player.motionZ = 0.0;
                }
            }
            else if (mode.equalsIgnoreCase("StormDuels")) {
            	block13: {
    				block14: {
    					mc.player.jumpMovementFactor = 0.01f;
    					BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    	                BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 1, feet.getZ());
    	                if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
    	                    return;
    	                }
    					if (mc.player.onGround) {
    						mc.player.jump();
    					}
    					if (mc.player.isInWater()) break block13;
        				if (mc.player.onGround) {
        					if (!MovementHelper.isMoving())
        						if(mc.player.ticksExisted %1==0 && mc.player.speedInAir < 0.03 ) {
        							mc.player.jumpMovementFactor = 0.01f;
        							mc.player.speedInAir *= 2f + 1.32412f ;
        						}
        				}
        				if (!MovementHelper.isMoving()) {
        					mc.player.speedInAir = 0.02f;
        					return;
        				}
        				if (mc.player.onGround != true) {
        					mc.player.speedInAir = 0.0f;
        					MovementHelper.strafe(0.45f);
        				}
    				}
    			}
            }
            else if (mode.equalsIgnoreCase("StormGrief")) {
            	block13: {
    				block14: {
    					mc.player.jumpMovementFactor = 0.01f;
    					BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    	                BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 1, feet.getZ());
    	                if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
    	                    return;
    	                }
    					if (mc.player.onGround) {
    						mc.player.jump();
    					}
    					if (mc.player.isInWater()) break block13;
        				if (mc.player.onGround) {
        					if (!MovementHelper.isMoving())
        						if(mc.player.ticksExisted %1==0 && mc.player.speedInAir < 0.03 ) {
        							mc.player.jumpMovementFactor = 0.01f;
        							mc.player.speedInAir *= 2f + 1.32412f ;
        						}
        				}
        				if (!MovementHelper.isMoving()) {
        					mc.player.speedInAir = 0.02f;
        					return;
        				}
        				if (mc.player.onGround != true) {
        					mc.player.speedInAir = 0.0f;
        					MovementHelper.strafe(0.8f);
        				}
    				}
    			}
            }
            else if (mode.equalsIgnoreCase("NexusGrief")) {
            	mc.gameSettings.keyBindJump.pressed = true;
            	mc.gameSettings.keyBindForward.pressed = true;
            }
            else if (mode.equalsIgnoreCase("SunriseDamage")) {
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 9.8 / 24.5, 0.0, Math.cos(MovementHelper.getDirection()) * 9.8 / 24.5);
                        MovementHelper.strafe();
                    }
                    else if (Speed.mc.player.isInWater()) {
                        Speed.mc.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 8.5 / 24.5, 0.0, Math.cos(MovementHelper.getDirection()) * 9.5 / 24.5);
                        MovementHelper.strafe();
                    }
                    else if (!Speed.mc.player.onGround) {
                        Speed.mc.player.addVelocity(-Math.sin(MovementHelper.getDirection2()) * 0.11 / 24.5, 0.0, Math.cos(MovementHelper.getDirection2()) * 0.11 / 24.5);
                        MovementHelper.strafe();
                    }
                    else {
                        Speed.mc.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * 0.05 * MovementHelper.getSpeed(), 0.0, Math.cos(MovementHelper.getDirection()) * 0.05 * MovementHelper.getSpeed());
                        MovementHelper.strafe();
                    }
                }
            }
            else if (mode.equalsIgnoreCase("StormDisabler")) {
                if (Speed.mc.player.onGround) {
                    Speed.mc.player.jump();
                }
                else {
                    MovementHelper.setSpeed(this.speed.getNumberValue());
                }
            } else if (mode.equalsIgnoreCase("Timer")) {
            	 mc.timer.timerSpeed = 1.0f;

            		        if (!MovementHelper.isMoving() || mc.player.isInWater() || mc.player.isInLava() ||
            		            mc.player.isOnLadder() || mc.player.isRiding()) { return;
            		        }
            		        if (mc.player.onGround) {
            		        	mc.player.jump();
            		        }else {
            		            if (mc.player.fallDistance <= 0.1) {
            		                mc.timer.timerSpeed = 1.3f;
            		            } else if (mc.player.fallDistance > 1.0) {
            		                mc.timer.timerSpeed = 0.6f;
            		            } else
            		                mc.timer.timerSpeed = 1f;
            		        }
            }
            this.setSuffix(mode, true);
            }
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        Speed.mc.player.speedInAir = 0.02f;
        super.onDisable();
    }
}
