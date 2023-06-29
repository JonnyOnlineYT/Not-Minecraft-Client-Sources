package splash.client.modules.movement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.api.value.impl.ModeValue;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventStep;
import splash.client.modules.combat.Criticals;
import splash.client.modules.combat.Criticals.Mode;
import splash.client.modules.movement.Flight;
import splash.utilities.player.BlockUtils;
import splash.utilities.time.Stopwatch;

public class Step extends Module { 

	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.NCP, this);
	public enum Mode {
		NCP, MOTION, VANILLA
	}
    public static long lastStep, lastFuck, lastPacket;
    private boolean stepping;
    private static Map<Float, float[]> offsets = new HashMap<>();
    private int ticks;
    public int waitTicks;
    private int ncpNextStep;
    private boolean resetTimer;
    private Stopwatch timer = new Stopwatch();
    public Step() {
		super("Step", "Goes up blocks for you.", ModuleCategory.MOVEMENT);
        offsets.put(1.0F, new float[]{0.41999998688698f, 0.753f});
        offsets.put(1.5F, new float[]{0.41999998688698f, 0.75f, 1, 1.16f});
        offsets.put(2.0F, new float[]{0.41999998688698f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f});
    }
    
    @Collect
    public void onMove(EventMove e) {
 
    	if (modeValue.getValue().equals(Mode.MOTION) && mc.thePlayer.isCollidedHorizontally) {
            float yaw = mc.thePlayer.getDirection();
            double x = -Math.sin(yaw) * 0.4;
            double z = Math.cos(yaw) * 0.4;
    		if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(x, 1.001335979112147, z)).isEmpty() && !mc.thePlayer.isOnLadder()) {
    			mc.thePlayer.motionY = 0;
    			e.setY(0.41999998688698);
    			e.setX(0);
    			e.setY(0);
    			ncpNextStep = 2;
    		}
    		
    		if (ncpNextStep == 2) {
    			e.setY(0.7531999805212 - 0.41999998688698);
    			e.setX(0);
    			e.setY(0);
    			ncpNextStep = 3;
    		}
    		
    		if (ncpNextStep == 3) {
    			e.setY(1.001335979112147 - 0.7531999805212);
    			e.setMoveSpeed(.01);
    			ncpNextStep = 0;
    		}
    	}
    }
    
    @Collect
    public void onPlayerUpdate(EventPlayerUpdate e) {
    	if (modeValue.getValue().equals(Mode.NCP)) {

            if ((System.currentTimeMillis() - lastStep) > 230L) {
                ticks = 0;
            } else {
                if (mc.thePlayer.ticksExisted % 2 == 0) ticks++;
            }
    	}
    }
    

    @Collect
    public void onStep(EventStep e) {

    	if (modeValue.getValue().equals(Mode.VANILLA) && mc.thePlayer.isCollidedHorizontally) { 
    		e.setStepHeight(2.5f);
    	}
    	if (modeValue.getValue().equals(Mode.NCP)) {
 
    		/*
    		 * 
    		 * @author FlyCode
    		 * 
    		 * Given to Splashdev by: Spec
    		 * 
    		 * NCP Step Credit: FlyCode
    		 * 
    		 * Code edits: interfaced with criticals, replaced mc.thePlayer.isOnLiquid() && mc.thePlayer.isInLiquid() with .isInWater & .isInLava
    		 * 
    		 * */
	        if (mc.thePlayer != null && mc.theWorld != null && (System.currentTimeMillis() - lastFuck) > 20L) {
	
	        	if (e.getStage().equals(Stage.PRE)) {
	    	        if ((System.currentTimeMillis() - lastPacket) > 150L && this.resetTimer) { 
	    	            mc.timer.timerSpeed = 1f;
	    	            resetTimer = false;
	    	        }
	    			if (EventMove.stillTime > 0) return;
	               	if (!mc.thePlayer.isCollidedHorizontally || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isOnLadder() || mc.thePlayer.checkBlockAbove(0.1f) || mc.gameSettings.keyBindJump.isKeyDown())  {
	               		e.setStepHeight(.626f);
	               		return;
	               	}
	               	if (waitTicks > 0) {
	               		waitTicks--;
	               		return;
	               	}
	               	if (Splash.getInstance().getModuleManager().getModuleByClass(Speed.class).isModuleActive() || Splash.getInstance().getModuleManager().getModuleByClass(LongJump.class).isModuleActive() || Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive()) {
	               		e.setStepHeight(Splash.getInstance().getModuleManager().getModuleByClass(LongJump.class).isModuleActive() || Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive() || EventMove.stillTime > 0 ? 0 : .626f);
	               		return;
	               	}
		            double radius = 0.50;
		        	
		            double currentX = mc.thePlayer.posX, currentY = mc.thePlayer.posY, currentZ = mc.thePlayer.posZ;
		
		            boolean isInvalid = false;
		            String[] invalidBlocks = {"snow", "chest", "slab", "stair"};
		
		            for (double x = currentX - radius; x <= currentX + radius; x++) {
		                for (double y = currentY - radius; y <= currentY + radius; y++) {
		                    for (double z = currentZ - radius; z <= currentZ + radius; z++) {
		                        if (!isInvalid) {
		                            String blockName = getBlockAtPos(new BlockPos(x, y, z)).getUnlocalizedName().toLowerCase();
		                            for (String s : invalidBlocks) {
		                                if (blockName.contains(s.toLowerCase())) isInvalid = true;
		                            }
		                        }
		                    }
		                }
		            }
		            if (timer.delay(isInvalid ? 300 : 250) && (System.currentTimeMillis() - lastPacket) > 100) {
	               		e.setStepHeight(mc.thePlayer.isPotionActive(Potion.jump) ? 1 : 2f); 
		        	}
		        	  
	        	} else { 
	        		if (Splash.getInstance().getModuleManager().getModuleByClass(Speed.class).isModuleActive() || Splash.getInstance().getModuleManager().getModuleByClass(LongJump.class).isModuleActive() || Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive()) return; 
 
	                double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
	                boolean canStep = height >= 0.625;
	                if (canStep) {
            			mc.timer.timerSpeed = (float) ((0.7F + height * .01f) - (height >= 1 ? Math.abs(1 - (float) height) * ((0.7F + height * .01f) * 0.55f) : 0));
            			sendPosition(0,0,0, mc.thePlayer.onGround, mc.thePlayer.isMoving());
 	                    if (mc.timer.timerSpeed <= 0.05f) {
 	                        mc.timer.timerSpeed = 0.05f;
 	                    }
 	                    resetTimer = true;
	                    double posX = mc.thePlayer.posX;
	                    double posZ = mc.thePlayer.posZ;
	                    double y = mc.thePlayer.posY;
	                    if (height <= 1.0) {
	                        float first = 0.41999998688698f;
	                        float second = 0.753f;
	                        if (height != 1) {
	                            first *= height;
	                            second *= height;
	                            if (first > 0.425f) {
	                                first = 0.425f;
	                            }
	                            if (second > 0.75f) {
	                                second = 0.75f;
	                            }
	                            if (second < 0.49f) {
	                                second = 0.49f;
	                            }
	                        }
	                        if (first == 0.42)
	                            first = 0.41999998688698f;
	                        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, !(BlockUtils.getBlockAtPos(new BlockPos(posX, y + first, posZ)) instanceof BlockAir)));
	                        if (y + second < y + height)
	                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, !(BlockUtils.getBlockAtPos(new BlockPos(posX, y + second, posZ)) instanceof BlockAir)));
	                        
	                        lastPacket = System.currentTimeMillis();
	                        return;
 	                    } else if (height <= 1.5) {
 	                        float[] heights = {0.42f, 0.333f, 0.248f, 0.083f, -0.078f};
 	                        for (float off : heights) {
 	                            y += off; 
 	                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, !(BlockUtils.getBlockAtPos(new BlockPos(posX, y, posZ)) instanceof BlockAir)));
 	                        }
 	                        lastPacket = System.currentTimeMillis();
 	                    } else if (height <= 2) {
 	                        float[] heights = {0.4249999f, 0.821001f, 0.699f, 0.598f, 1.02217f, 1.372f, 1.652f, 1.869f};
 	                        for (float off : heights) { 
 	                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, !(BlockUtils.getBlockAtPos(new BlockPos(posX, y + off, posZ)) instanceof BlockAir)));
 	                        }
 	                        lastPacket = System.currentTimeMillis();
 	                    }

 	    	            lastStep = System.currentTimeMillis();
 	    	            stepping = true;
 	    	            lastFuck = System.currentTimeMillis();
	                    timer.reset();
	                } 
	        	}
	        }
    	}
    }
    
    private boolean couldStep() {
        return false;
    }
    
    public float getNeededStepHeight() {

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX, 1.1, mc.thePlayer.motionZ)).size() == 0)
            return 1.0F;

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX, 1.6, mc.thePlayer.motionZ)).size() == 0)
            return 1.5F;

        return (float) 2D;
    }
    public Block getBlockAtPos(BlockPos pos) {
        IBlockState blockState = getBlockStateAtPos(pos);
        if (blockState == null)
            return null;
        return blockState.getBlock();
    }

    public IBlockState getBlockStateAtPos(BlockPos pos) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null)
            return null;
        return Minecraft.getMinecraft().theWorld.getBlockState(pos);
    }

    public boolean isOnGround() {
        for (double d = 0.0; d <= 1.00; d+=0.05) {
            if (!Objects.requireNonNull(getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - d, mc.thePlayer.posZ))).getUnlocalizedName().toLowerCase().contains("air")) {
                return true;
            }
        }
        return false;
    }
}