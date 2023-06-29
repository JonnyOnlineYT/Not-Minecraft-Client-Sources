package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "StairSpeed", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class StairSpeed extends Module {
	
    @EventTarget
    public void onUpdate(UpdateEvent event){
        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        double yaw = (double)mc.player.rotationYaw * 0.017453292D;
        float expand = mc.player.onGround ? 0.5F : 1000.0F;
        if ((mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.STONE_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.OAK_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.NETHER_BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.SANDSTONE_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.QUARTZ_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.ACACIA_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.DARK_OAK_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.PURPUR_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.RED_SANDSTONE_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.JUNGLE_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.BIRCH_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.STONE_BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x, y - (double)expand, z)).getBlock() == Blocks.SPRUCE_STAIRS) && (mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.STONE_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.OAK_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.NETHER_BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.SANDSTONE_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.QUARTZ_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.ACACIA_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.DARK_OAK_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.PURPUR_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.RED_SANDSTONE_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.JUNGLE_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.BIRCH_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.STONE_BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.BRICK_STAIRS || mc.world.getBlockState(new BlockPos(x - Math.sin(yaw) * (double)expand, y, z + Math.cos(yaw) * (double)expand)).getBlock() == Blocks.SPRUCE_STAIRS)) {

            mc.player.jump();

            if (!mc.player.movementInput.jump); {

                if (mc.player.onGround) {

                    mc.player.jump();
                }
            }
            mc.player.jumpMovementFactor = 0.0F;
            mc.player.motionY = -0.42D;
        }

    }
}

