package org.moonware.client.feature.impl.misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class Nuker extends Feature {

    private final BooleanSetting sendRotations;
    private final BooleanSetting sortTrashBlocks;
    private final BooleanSetting autoNoBreakDelay;
    private final NumberSetting nukerHorizontal;
    private final NumberSetting nukerVertical;
    private final BooleanSetting nukerESP;
    private final ColorSetting color;

    private BlockPos blockPos;

    public Nuker() {
        super("Nuker", "Рушит блоки вокруг тебя", Type.Other);
        nukerESP = new BooleanSetting("Nuker ESP", true, () -> true);
        color = new ColorSetting("Nuker Color", new Color(0xFFFFFF).getRGB(), nukerESP::getBoolValue);
        sendRotations = new BooleanSetting("Send Rotations", true, () -> true);
        sortTrashBlocks = new BooleanSetting("Sort trash blocks", true, () -> true);
        autoNoBreakDelay = new BooleanSetting("No Delay", true, () -> true);
        nukerHorizontal = new NumberSetting("Nuker Horizontal", 3, 1, 5, 1F, () -> true);
        nukerVertical = new NumberSetting("Nuker Vertical", 3, 1, 5, 1F, () -> true);
        addSettings(color, nukerESP, sendRotations, sortTrashBlocks, autoNoBreakDelay, nukerHorizontal, nukerVertical);
    }

    private boolean canNuker(BlockPos pos) {
        IBlockState blockState = Minecraft.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, Minecraft.world, pos) != -1;
    }

    private BlockPos getPositionXYZ() {
        BlockPos blockPos = null;
        float vDistance = nukerVertical.getNumberValue();
        float hDistance = nukerHorizontal.getNumberValue();
        for (float x = 0; x <= hDistance; x++) {
            for (float y = 0; y <= vDistance; y++) {
                for (float z = 0; z <= hDistance; z++) {
                    for (int reversedX = 0; reversedX <= 1; reversedX++, x = -x) {
                        for (int reversedZ = 0; reversedZ <= 1; reversedZ++, z = -z) {
                            BlockPos pos = new BlockPos(Minecraft.player.posX + x, Minecraft.player.posY + y, Minecraft.player.posZ + z);
                            if (Minecraft.world.getBlockState(pos).getBlock() != Blocks.AIR && canNuker(pos)) {
                                blockPos = pos;
                            }
                        }
                    }
                }
            }
        }
        return blockPos;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (!nukerESP.getBoolValue()) {
            return;
        }
        if ((Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.GRASS || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.MONSTER_EGG || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.DIRT || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.GRAVEL || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.WATER || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.LAVA) && (sortTrashBlocks.getBoolValue())) {
            return;
        }
        blockPos = getPositionXYZ();
        Color nukerColor = new Color(color.getColorValue());
        BlockPos blockPos = new BlockPos(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
        RenderHelper.blockEsp(blockPos, nukerColor, true);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        if (autoNoBreakDelay.getBoolValue()) {
            Minecraft.playerController.blockHitDelay = 0;
        }
        blockPos = getPositionXYZ();
        float[] rotations = RotationHelper.getRotationVector(new Vec3d(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f), true, 2, 2, 360);
        if ((Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.GRASS || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.MONSTER_EGG || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.DIRT || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.GRAVEL || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.WATER || Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.LAVA) && (sortTrashBlocks.getBoolValue())) {
            return;
        }
        if (Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemTool || Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemTool || Minecraft.player.isCreative()) {
            if (blockPos != null) {
                if (sendRotations.getBoolValue()) {
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                    Minecraft.player.renderYawOffset = rotations[0];
                    Minecraft.player.rotationYawHead = rotations[0];
                    Minecraft.player.rotationPitchHead = rotations[1];
                }
                if (canNuker(blockPos)) {
                    Minecraft.playerController.onPlayerDamageBlock(blockPos, Minecraft.player.getHorizontalFacing());
                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
