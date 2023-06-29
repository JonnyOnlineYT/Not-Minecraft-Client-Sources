package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.util.MovementUtil;
import java.util.Arrays;

@ModuleInfo(name = "JumpWalk", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class JumpWalk extends Module {

    @EventTarget
    public void onUpdate(MotionEvent eventPreMotion) {
        float ex2 = 1f;
        float ex = 1f;
        if ((isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX + ex2, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX - ex2, mc.player.posY - ex, mc.player.posZ)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ + ex2)).getBlock()) ||
                isBlockValid(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - ex, mc.player.posZ - ex2)).getBlock())
        )
                && mc.player.ticksExisted % 2 == 0) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.jumpTicks = 0;
                mc.player.fallDistance = 0;
                eventPreMotion.setOnGround(true);
                mc.player.onGround = true;
            }
        }
    }
    public boolean isBlockValid(Block block) {
        return block != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(block));
    }
}