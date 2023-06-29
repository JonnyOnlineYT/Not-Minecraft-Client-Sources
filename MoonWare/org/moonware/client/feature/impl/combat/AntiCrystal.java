package org.moonware.client.feature.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.InventoryHelper;
import org.moonware.client.helpers.world.BlockHelper;
import org.moonware.client.helpers.world.EntityHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Comparator;

public class AntiCrystal extends Feature {

    private final NumberSetting rangeToBlock;
    private final NumberSetting delay;
    private final BooleanSetting throughWalls;
    private final BooleanSetting bedrockCheck;
    private final BooleanSetting obsidianCheck;
    private final TimerHelper timerHelper = new TimerHelper();
    private final ArrayList<BlockPos> invalidPositions = new ArrayList<>();

    public AntiCrystal() {
        super("AntiCrystal", "Автоматически ставит блок на обсидиан/бедрок в радиусе", Type.Combat);
        throughWalls = new BooleanSetting("Through Walls", true, () -> true);
        obsidianCheck = new BooleanSetting("Obsidian Check", true, () -> true);
        bedrockCheck = new BooleanSetting("Bedrock Check", false, () -> true);
        rangeToBlock = new NumberSetting("Range To Block", 5, 3, 6, 0.1F, () -> true);
        delay = new NumberSetting("Place Delay", 0, 0, 2000, 100, () -> true);
        addSettings(obsidianCheck, bedrockCheck, throughWalls, rangeToBlock, delay);
    }

    public static int getSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.player.inventory.getStackInSlot(i);
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = Minecraft.world.getBlockState(pos);
        if ((state.getBlock() instanceof BlockObsidian && obsidianCheck.getBoolValue()) || (state.getBlock() == Block.getBlockById(7) && bedrockCheck.getBoolValue()))
            return Minecraft.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        return false;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

        setSuffix("" + (int) rangeToBlock.getNumberValue());

        /* VARIABLES */

        int oldSlot = Minecraft.player.inventory.currentItem;
        BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), rangeToBlock.getNumberValue(), 6, false, true).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(Minecraft.player, blockPos))).orElse(null);
        if (InventoryHelper.doesHotbarHaveBlock() && pos != null) {

            /* CHECK DELAY */

            if (timerHelper.hasReached(delay.getNumberValue())) {

                if (getSlotWithBlock() != -1) {

                    /* CHECK BLOCK ABOVE */

                    if (!Minecraft.world.isAirBlock(pos.up(1))) {
                        invalidPositions.add(pos);
                    }

                    for (Entity e : Minecraft.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal) {
                            if (e.getPosition().getX() == pos.getX() && e.getPosition().getZ() == pos.getZ()) {
                                return;
                            }
                        }
                    }

                    /* CHECK INVALID POS */
                    if (!invalidPositions.contains(pos)) {

                        /* RAY-TRACE */
                        if (Minecraft.world.rayTraceBlocks(new Vec3d(Minecraft.player.posX, Minecraft.player.posY + Minecraft.player.getEyeHeight(), Minecraft.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null && !throughWalls.getBoolValue()) {
                            return;
                        }

                        /* SEND ROTATION PACKET */
                        float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5), true, 2, 2, 360);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        Minecraft.player.renderYawOffset = rots[0];
                        Minecraft.player.rotationYawHead = rots[0];
                        Minecraft.player.rotationPitchHead = rots[1];

                        /* SWITCH TO BLOCK */
                        Minecraft.player.inventory.currentItem = getSlotWithBlock();

                        /* CLICK ON BLOCK */
                        Minecraft.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);

                        /* SWITCH OLD SLOT */
                        Minecraft.player.inventory.currentItem = oldSlot;

                        /* RESET DELAY TIMER */
                        timerHelper.reset();
                    }
                }
            }
        }
    }
}