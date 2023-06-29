package ru.rockstar.client.features.impl.combat;


import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.combat.EntityHelper;
import ru.rockstar.api.utils.combat.RotationHelper;
import ru.rockstar.api.utils.world.BlockHelper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

import java.util.ArrayList;
import java.util.Comparator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalAura extends Feature {
    public CrystalAura() {
        super("CrystalAura","Автоматически бьёт по кристаллу", 0, Category.COMBAT);
    }
    private final ArrayList<BlockPos> invalidPositions = new ArrayList<>();
    public static int getSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEndCrystal) {
                return i;
            }
        }
        return -1;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        if ((state.getBlock() instanceof BlockObsidian) || (state.getBlock() == Block.getBlockById(7)))
            return mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        return false;
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        /* VARIABLES */

        int oldSlot = mc.player.inventory.currentItem;
        BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), 5, 6, false, true).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        if (InventoryHelper.doesHotbarHaveCrystal() && pos != null) {

            /* CHECK DELAY */

            if (timerHelper.hasReached(100)) {

                if (getSlotWithBlock() != -1) {

                    /* CHECK BLOCK ABOVE */

                    if (!mc.world.isAirBlock(pos.up(1))) {
                        this.invalidPositions.add(pos);
                    }

                    for (Entity e : mc.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal) {
                            if (e.getPosition().getX() == pos.getX() && e.getPosition().getZ() == pos.getZ()) {
                                return;
                            }
                        }
                    }

                    /* CHECK INVALID POS */
                    /*
                    if (!this.invalidPositions.contains(pos)) {
                        // RAY-TRACE 
                        if (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null) {
                            return;
                        }

                        // SEND ROTATION PACKET
                        float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5), true, 2, 2, 360);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        mc.player.renderYawOffset = rots[0];
                        mc.player.rotationYawHead = rots[0];
                        mc.player.rotationPitchHead = rots[1];

                        // SWITCH TO BLOCK
                        mc.player.inventory.currentItem = getSlotWithBlock();

                        // CLICK ON BLOCK
                        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
                        mc.player.swingArm(EnumHand.MAIN_HAND);

                        // SWITCH OLD SLOT 
                        mc.player.inventory.currentItem = oldSlot;

                        // RESET DELAY TIMER
                        timerHelper.reset();
                    }
                    */
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(final EventPreMotionUpdate event){
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityEnderCrystal){
            	if (mc.player.getDistanceToEntity(e) <= 6) {
            		event.setPitch(RotationHelper.getRotations(e)[1]);
                    event.setYaw(RotationHelper.getRotations(e)[0]);
                    mc.player.rotationYawHead = event.getYaw();
                    mc.player.renderYawOffset = event.getYaw();
                    mc.player.rotationPitchHead = event.getPitch();
                    mc.getConnection().sendPacket(new CPacketUseEntity(e));
                    mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            	}
            }
        }
    }
}
