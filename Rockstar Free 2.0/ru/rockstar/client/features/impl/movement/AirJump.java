package ru.rockstar.client.features.impl.movement;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class AirJump extends Feature {

    public AirJump() {
        super("AirJump", "Позволяет прыгать по воздуху", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void sdd(EventUpdate eventUpdate) {
    	
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
                mc.player.onGround = true;
            }
        }
        
    }
    public boolean isBlockValid(Block block) {
        return block != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(block));
    }
}
