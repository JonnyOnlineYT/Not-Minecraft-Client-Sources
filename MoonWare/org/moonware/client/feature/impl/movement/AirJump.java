package org.moonware.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

import java.util.Arrays;

public class AirJump extends Feature {

    public AirJump() {
        super("AirJump", "Позволяет прыгать по воздуху", Type.Movement);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        final float ex2 = 1.0f;
        final float ex3 = 1.0f;
        if ((this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - ex3, Minecraft.player.posZ)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - ex3, Minecraft.player.posZ)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX - ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ - ex2)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX + ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ + ex2)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX - ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ + ex2)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX + ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ - ex2)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX + ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX - ex2, Minecraft.player.posY - ex3, Minecraft.player.posZ)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - ex3, Minecraft.player.posZ + ex2)).getBlock()) || this.isBlockValid(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - ex3, Minecraft.player.posZ - ex2)).getBlock())) && Minecraft.player.ticksExisted % 2 == 0 && AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            Minecraft.player.jumpTicks = 0;
            Minecraft.player.fallDistance = 0.0f;
            event.setOnGround(true);
            Minecraft.player.onGround = true;
        }
    }
    public boolean isBlockValid(final Block block) {
        return block != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(block));
    }
}
