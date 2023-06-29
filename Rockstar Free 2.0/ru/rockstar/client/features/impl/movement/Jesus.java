package ru.rockstar.client.features.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.*;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import ru.rockstar.Main;
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

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;

public class Jesus extends Feature
{
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
    private final BooleanSetting autoDisable;
    private final BooleanSetting autoPearl;
    private int waterTicks;
    public static boolean inWater = false;
    public static BooleanSetting targetCheck = new BooleanSetting("TargetCheck", "Проверяет есть ли у киллауры таргет, только после этого использует данный модуль", false, () -> true);
    public static BooleanSetting canJump = new BooleanSetting("CanJump", "Ты можешь прыгать на джесусе", false, () -> true);
    public static BooleanSetting noJump = new BooleanSetting("NoJump", "Позволяет не заходить на берег, без зажатия пробела", false, () -> true);
    int y = 0;
    
    public Jesus() {
        super("Jesus", "Бег по воде", 0, Category.MOVEMENT);
        this.speed = new NumberSetting("Speed", 10f, 1f, 35f, 1f, () -> true);
        this.timerSpeed = new NumberSetting("Timer Speed", 1.05f, 1.01f, 1.5f, 0.01f, () -> Jesus.useTimer.getBoolValue());
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        this.autoDisable = new BooleanSetting("Auto Disable","Автоматически выключает джесус при флаге", false, () -> true);
        this.autoPearl = new BooleanSetting("Auto Pearl","Автоматически кидает под себя пёрл при флаге", false, () -> autoDisable.getBoolValue());
        this.autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> Jesus.mode.currentMode.equals("ReallyWorld"));
        this.autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> Jesus.mode.currentMode.equals("ReallyWorld"));
        upTp = new BooleanSetting("Auto Water Up", false, () -> Jesus.mode.currentMode.equals("ReallyWorld") || Jesus.mode.currentMode.equals("ReallyWorld2") || Jesus.mode.currentMode.equals("ReallyWorld3") || Jesus.mode.currentMode.equals("ReallyWorld4"));
        this.waterTicks = 0;
        this.addSettings(Jesus.mode, Jesus.speed, Jesus.NCPSpeed, Jesus.useTimer, this.timerSpeed, Jesus.motionUp, this.speedCheck, this.autoWaterDown, this.autoMotionStop, upTp, targetCheck, canJump,noJump, autoDisable, autoPearl);
    }
    
    @Override
    public void onDisable() {
    	MovementHelper.setSpeed(0.1f);
    	inWater = false;
    	mc.player.noClip = false;
        Jesus.mc.timer.timerSpeed = 1.0f;
        if (Jesus.mode.currentMode.equals("ReallyWorld") && this.autoWaterDown.getBoolValue()) {
            final EntityPlayerSP player = Jesus.mc.player;
            player.motionY -= 500.0;
        }
        this.waterTicks = 0;
        super.onDisable();
    }
    
    @EventTarget
    public void onLiquidBB(final EventLiquidSolid event) {
    	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld4")) {
    		if (mc.player.isInWater()) {
                return;
            }

            if (mc.player.posY > (double)event.getPos().getY() + 1) {
                event.setColision(Block.FULL_BLOCK_AABB.expand(0.0, -9.0E-6, 0.0));
            }
    	}
    	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld2")) {
    		if (mc.player.isInWater()) {
                return;
            }

            if (mc.player.posY > (double)event.getPos().getY() + 0.999999999) {
                event.setColision(Block.FULL_BLOCK_AABB.expand(0.0, -9.0E-6, 0.0));
            }
    	}
    	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld3")) {
    		if (mc.player.isInWater()) {
                return;
            }

            if (mc.player.posY > (double)event.getPos().getY() + 0.999999999) {
                event.setColision(Block.FULL_BLOCK_AABB.expand(0.0, -9.0E-6, 0.0));
            }
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
    public void onLagbackSpeed(final EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook && this.autoDisable.getBoolValue() && isToggled()) {
            Main.msg("Anti-cheat discovered jesus", true);
            NotificationPublisher.queue(ChatFormatting.RED + "Anti-Cheat", ChatFormatting.WHITE + "Anti-cheat discovered jesus", NotificationType.WARNING);
        
            if (autoPearl.getBoolValue()) {
            	mc.player.rotationPitch = -90;
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                
                if (mc.player.rotationPitch == -90) {
                	int slot = InventoryHelper.getEnderPearl();
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                    mc.playerController.updateController();
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.playerController.updateController();
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                }
            }
        
            this.toggle();
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotionUpdate event) {
    	boolean a = false;
     
        this.setSuffix(Jesus.mode.getCurrentMode(), true);
        if (Jesus.mc.player.isPotionActive(MobEffects.SPEED) || !this.speedCheck.getBoolValue()) {
        	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld4")) {
        		
        		int slot = 0;
        		if (InventoryHelper.getElytraAtHotbar() != 6) {
        			slot = InventoryHelper.getElytraAtHotbar();
        		}
        		if (inWater && !mc.player.onGround && InventoryHelper.getElytraAtHotbar() != 6) {
        			mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
        			mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
                 }
        		
        		

         
         
        	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                    && !mc.player.onGround && canJump.getBoolValue()) {
             	mc.player.jump();
             	mc.player.motionX *= 0.1f;
             	mc.player.motionZ *= 0.1f;
             	mc.player.fallDistance = -99999.0F;
             }
        	
            	if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER
                        && !mc.player.onGround) {
        	 BlockPos blockPos2;
             Block block2;
        	blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
            block2 = mc.world.getBlockState(blockPos2).getBlock();

            boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() == Blocks.WATER;
            mc.player.jumpMovementFactor = 0;
            mc.player.fallDistance = -99999.0F;
            float yport = MovementHelper.getSpeed() > 0.1 ? 0.02f : 0.032f;
            mc.player.setVelocity(0,mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.114, 0);
            
            inWater = true;
            if (mc.player.posY > (int)mc.player.posY + 0.89 && mc.player.posY <= (int)mc.player.posY + 1.0f || mc.player.fallDistance > 3.5) {
            	mc.player.posY = (int)mc.player.posY + 1.0f + 1E-45;
                if (MovementHelper.isMoving()) {
                	MovementHelper.setSpeed(KillAura.target!=null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() ? 0.1f * speed.getNumberValue() / 1.5f : 0.1f * speed.getNumberValue());
                } else {
                	mc.player.motionX -= Math.sin(mc.player.rotationYaw) * 0.005;
                	mc.player.motionZ += Math.cos(mc.player.rotationYaw) * 0.005;
                	
                }
                
                

            }
            
            
            
            	} else {
                 	inWater = false;
                 }
            	if ((mc.player.isInWater() || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.15, mc.player.posZ)).getBlock() == Blocks.WATER)) {
                    mc.player.motionY = 0.16;
                    if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) {
                        mc.player.motionY = 0.12;
                    }
                    if (upTp.getBoolValue() && mc.player.posY < 63 && mc.world.getBlockState(new BlockPos(mc.player.posX, 62, mc.player.posZ)).getBlock() == Block.getBlockById(9) && mc.world.getBlockState(new BlockPos(mc.player.posX, 63, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.3, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        	mc.player.setPositionAndUpdate(mc.player.posX, 62.8f + y, mc.player.posZ);
                        }
                    } else {
                    	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        	mc.player.motionY = 0.18;
                        }
                    }
                }
        	}
            BlockPos blockPos = new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.1, Jesus.mc.player.posZ);
            final Block block = Jesus.mc.world.getBlockState(blockPos).getBlock();
            if (Jesus.useTimer.getBoolValue()) {
                Jesus.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
            }
            if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld2")) {
            	event.setGround(false);
            }
            
            if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix")) {
                if (Jesus.mc.player.isInLiquid() && Jesus.mc.player.motionY < 0.0) {
                    Jesus.mc.player.motionY = Jesus.motionUp.getNumberValue();
                    MovementHelper.setSpeed(Jesus.speed.getNumberValue() * 0.01);
                }
            }
            else if (Jesus.mode.currentMode.equalsIgnoreCase("NCP") && this.isWater() && block instanceof BlockLiquid) {
            	double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                mc.timer.timerSpeed = 1.0f;
                /*
                if (
                        mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.LAVA
                ) {
                    IceSpeed.go = false;
                }*/
                if (
                        mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z+0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z-0.3)).getBlock() == Blocks.WATER ||
                                mc.world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x,y,z-0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z-0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x-0.3,y,z+0.3)).getBlock() == Blocks.LAVA ||
                                mc.world.getBlockState(new BlockPos(x+0.3,y,z-0.3)).getBlock() == Blocks.LAVA
                ) {
                    if (mc.player.movementInput.jump || mc.player.isCollidedHorizontally) {
                        if (mc.player.isCollidedHorizontally) {
                            mc.player.setPosition(x, y + 0.2, z);
                        } mc.player.onGround = true;if (mc.player.fallDistance < 0.2){  mc.player.motionY += 0.021f; }}
                    mc.player.motionX = 0; mc.player.motionZ = 0; mc.player.motionY = 0.04;
                    if (!(mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.LAVA)) {
                        if (mc.player.fallDistance != 0 && mc.player.motionX == 0 && mc.player.motionZ == 0) {
                            mc.player.setPosition(x, y, z);
                            if(mc.player.fallDistance < 0.08) {
                                mc.player.setPosition(x, y + 0.2, z);
                            }}}
                    if(!mc.player.isPotionActive(Potion.getPotionById(1))) {
                        mc.player.jumpMovementFactor = 0.2865f;}
                    if(mc.player.isPotionActive(Potion.getPotionById(1))) {
                        mc.player.jumpMovementFactor = 0.4005f;}
                }
                MovementHelper.setSpeed(MovementHelper.getSpeed());
                if (!mc.gameSettings.keyBindJump.isKeyDown() && (mc.player.isInWater() || mc.player.isInLava())) {
                    mc.player.motionY = 0.12;
                    mc.timer.timerSpeed = 1.1F;
                    inWater = true;
                    if (mc.player.isInWater() && mc.world.getBlockState(new BlockPos(x, y + 0.9, z)).getBlock() == Blocks.WATER &&
                            mc.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR &&
                            !(mc.world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.WATER)) {
                        mc.player.posY += 0.1;
                    }
                } else {
                	inWater = false;
                }
            }
            else if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld")) {
            	mc.player.speedInAir = 0.02f;
            	event.setGround(true);
            	if (!mc.player.isCollidedHorizontally) {
            		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER
                            && !mc.player.onGround && canJump.getBoolValue()) {
                     	mc.player.jump();
                     	mc.player.motionX *= 0.1f;
                     	mc.player.motionZ *= 0.1f;
                     	mc.player.fallDistance = -99999.0F;
                     	inWater = true;
                     } else {
                     	inWater = false;
                     }
            		if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.001f * speed.getNumberValue(), mc.player.posZ)).getBlock() == Block.getBlockById(9) && !mc.player.onGround) {
            			if (targetCheck.getBoolValue()) {
                			boolean n = false;
                			if (KillAura.target != null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
                					n = false;
                			} else {
                					n = true;
                			}
                			if (n) {
                				MovementHelper.setSpeed(9.9f);
                				inWater = true;
                                mc.player.jumpMovementFactor = 0.01f;
                			} else {
                                mc.player.jumpMovementFactor = 0.01f;
                			}
                		} else {
                			MovementHelper.setSpeed(9.9f);
                            mc.player.jumpMovementFactor = 0.01f;
                		}
                    }
                    if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        mc.player.motionX = 0.0;
                        Jesus.mc.player.motionY = 0.04700000074505806;
                        mc.player.jumpMovementFactor = 0.01f;
                        mc.player.motionZ = 0.0;
                    }
                    BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                    BlockPos newPos = new BlockPos(feet.getX(), 62, feet.getZ());
                    BlockPos newPos2 = new BlockPos(feet.getX(), 63, feet.getZ());
                    if (upTp.getBoolValue() && mc.player.posY < 63 && mc.world.getBlockState(new BlockPos(mc.player.posX, 62, mc.player.posZ)).getBlock() == Block.getBlockById(9) && mc.world.getBlockState(new BlockPos(mc.player.posX, 63, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.3, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        	mc.player.setPositionAndUpdate(mc.player.posX, 62.8f + y, mc.player.posZ);
                        }
                    } else {
                    	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        	mc.player.motionY = 0.18;
                        }
                    }
            	} else {
            		if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
            			mc.player.motionY = 0.1f;
                		mc.player.motionZ = 0f;
                		mc.player.motionX = 0f;
            		}
            	}
            	 
                }
            else if (Jesus.mode.currentMode.equalsIgnoreCase("SunRise")) {
                if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.4f, Jesus.mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                	if (MovementHelper.isMoving()) {
                        if (mc.player.isCollidedHorizontally) {
                        	mc.player.motionY = 0.18;
                        } else {
                        	Speed.mc.player.addVelocity(-Math.sin(MovementHelper.getDirection()) * speed.getNumberValue() / 24.5, 0.0, Math.cos(MovementHelper.getDirection()) * speed.getNumberValue() / 24.5);
                            MovementHelper.strafe();
                            mc.player.motionY = 0.0;
                        }
                    }
                }
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                	mc.player.motionY = 0.16;
                }
            }
        }
    }
    @EventTarget
    public void onPreMotion(final EventPlayerMotionUpdate event) {
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld2")) {
        	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                    && !mc.player.onGround && canJump.getBoolValue()) {
             	mc.player.jump();
             	mc.player.motionX *= 0.1f;
             	mc.player.motionZ *= 0.1f;
             	mc.player.fallDistance = -99999.0F;
             }
            	if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                        && !mc.player.onGround) {
                    boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() == Blocks.WATER;
                    mc.player.jumpMovementFactor = 0;
                    mc.player.fallDistance = -99999.0F;
                    float yport = MovementHelper.getSpeed() > 0.1 ? 0.02f : 0.032f;
                    mc.player.setVelocity(0,mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.114, 0);
                    if (mc.player.posY > (int)mc.player.posY + 0.89 && mc.player.posY <= (int)mc.player.posY + 1.0f || mc.player.fallDistance > 3.5) {
                        mc.player.posY = (int)mc.player.posY + 1.0f + 1E-45;
                        if (!mc.player.isInWater()) {
                        	mc.player.motionX = 0.0f;
                        	mc.player.motionZ = 0.0f;
                        	mc.player.onGround = true;
                        	inWater = true;
                        	
                        	MovementHelper.setSpeed(KillAura.target!=null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() ? 0.1f * speed.getNumberValue() / 2 : 0.1f * speed.getNumberValue());
                        	
                            
                        }

                    } else {
                    	inWater = false;
                    }
                    	mc.player.onGround = false;
                    if (mc.player.isCollidedHorizontally) {
                    	if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && noJump.getBoolValue()) {
                    		return;
                    	}
                    	mc.player.motionY += 0.091f;
                    	mc.player.motionX *= 0.4f * MovementHelper.getSpeed();
                    	mc.player.motionZ *= 0.4f * MovementHelper.getSpeed();
                    }
                }
            if ((mc.player.isInWater() || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.15, mc.player.posZ)).getBlock() == Blocks.WATER)) {
                mc.player.motionY = 0.16;
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    mc.player.motionY = 0.12;
                }
                if (upTp.getBoolValue() && mc.player.posY < 63 && mc.world.getBlockState(new BlockPos(mc.player.posX, 62, mc.player.posZ)).getBlock() == Block.getBlockById(9) && mc.world.getBlockState(new BlockPos(mc.player.posX, 63, mc.player.posZ)).getBlock() == Blocks.AIR) {
                	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.3, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                    	mc.player.setPositionAndUpdate(mc.player.posX, 62.8f + y, mc.player.posZ);
                    }
                } else {
                	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                    	mc.player.motionY = 0.18;
                    }
                }
            }
    	}
    	if (Jesus.mode.currentMode.equalsIgnoreCase("ReallyWorld3")) {
    		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                    && !mc.player.onGround && canJump.getBoolValue()) {
             	mc.player.jump();
             	mc.player.motionX *= 0.1f;
             	mc.player.motionZ *= 0.1f;
             	mc.player.fallDistance = -99999.0F;
             }
            	if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA) 
                        && !mc.player.onGround) {
                    boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA;
                    mc.player.jumpMovementFactor = 0;
                    mc.player.fallDistance = -99999.0F;
                    float yport = MovementHelper.getSpeed() > 0.1 ? 0.02f : 0.032f;
                    mc.player.setVelocity(0,mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.114, 0);
                    if (mc.player.posY > (int)mc.player.posY + 0.89 && mc.player.posY <= (int)mc.player.posY + 1.0f || mc.player.fallDistance > 3.5) {
                        mc.player.posY = (int)mc.player.posY + 1.0f + 1E-45;
                        if (!mc.player.isInWater()) {
                        	mc.player.motionX = 0.0f;
                        	mc.player.motionZ = 0.0f;
                        	inWater = true;
                        	
                        	if (mc.player.hurtTime > 0) {
                        		MovementHelper.setSpeed(1.1);
                        	} else {
                        		MovementHelper.setSpeed(0.3);
                        	}
                        	
                            
                        }
 
                    } else {
                    	inWater = false;
                    }
                    	mc.player.onGround = false;
                    if (mc.player.isCollidedHorizontally) {
                    	if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && noJump.getBoolValue()) {
                    		return;
                    	}
                    	mc.player.motionY += 0.091f;
                    	mc.player.motionX *= 0.4f * MovementHelper.getSpeed();
                    	mc.player.motionZ *= 0.4f * MovementHelper.getSpeed();
                    }
                }
            if ((mc.player.isInWater() || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.15, mc.player.posZ)).getBlock() == Blocks.WATER)) {
                mc.player.motionY = 0.16;
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) {
                    mc.player.motionY = 0.12;
                }
                if (upTp.getBoolValue() && mc.player.posY < 63 && mc.world.getBlockState(new BlockPos(mc.player.posX, 62, mc.player.posZ)).getBlock() == Block.getBlockById(9) && mc.world.getBlockState(new BlockPos(mc.player.posX, 63, mc.player.posZ)).getBlock() == Blocks.AIR) {
                	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.3, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                    	mc.player.setPositionAndUpdate(mc.player.posX, 62.8f + y, mc.player.posZ);
                    }
                } else {
                	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                    	mc.player.motionY = 0.18;
                    }
                }
            }
    	}
    }
    
    static {
        Jesus.mode = new ListSetting("Jesus Mode", "Matrix", () -> true, new String[] { "Matrix", "ReallyWorld", "ReallyWorld2", "ReallyWorld3", "ReallyWorld4", "SunRise", "NCP" });
        Jesus.speed = new NumberSetting("Speed", 0.65f, 0.1f, 10.0f, 0.01f, () -> !Jesus.mode.currentMode.equals("NCP"));
        Jesus.NCPSpeed = new NumberSetting("NCP Speed", 0.25f, 0.01f, 0.5f, 0.01f, () -> Jesus.mode.currentMode.equals("NCP"));
        Jesus.motionUp = new NumberSetting("Motion Up", 0.42f, 0.1f, 2.0f, 0.01f, () -> Jesus.mode.currentMode.equals("Matrix"));
        Jesus.useTimer = new BooleanSetting("Use Timer", false, () -> true);
    }
}
