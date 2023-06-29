package org.spray.heaven.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.misc.TimerMod;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.InvUtil;
import org.spray.heaven.util.RotationUtil;

@ModuleInfo(name = "AntiCrystal", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class AntiCrystal extends Module {

    private ArrayList<BlockPos> invalidPositions = new ArrayList<>();

    private Setting obsidian = register(new Setting("Obsidian", true));
    private Setting bedrock = register(new Setting("Bedrock", true));

    private Setting range = register(new Setting("Range", 3, 1, 6));

    private TimerMod timer = new TimerMod();

    @EventTarget
    public void onMotionUpdate(MotionEvent event) {
        int oldSlot = mc.player.inventory.currentItem;
        BlockPos pos = getSphere(getFloorPos(), (float) range.getValue(), 6, false, true, 0).stream()
                .filter(this::validPos).min(Comparator.comparing(blockPos -> distanceToBlock(mc.player, blockPos)))
                .orElse(null);
        if (blocksIsEmpty() && pos != null) {
            int blockSlot = InvUtil.findBlock();
            if (blockSlot != -2) {
                if (!mc.world.isAirBlock(pos.up(1)))
                    invalidPositions.add(pos);

                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity == null || mc.player == null)
                        continue;
                    if (entity instanceof EntityEnderCrystal && entity.getPosition().getX() == pos.getX()
                            && entity.getPosition().getZ() == pos.getZ())
                        return;
                }

                if (!invalidPositions.contains(pos)) {
                    assert mc.player != null;
                    if (mc.world.rayTraceBlocks(
                            new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
                            new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null)
                        return;

                    float[] rotation = RotationUtil
                            .predictRotation(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.4, pos.getZ() + 0.5));
                    event.setYaw(rotation[0]);
                    event.setPitch(rotation[1]);
                    mc.player.renderYawOffset = rotation[0];
                    mc.player.rotationYawHead = rotation[0];

                    mc.player.inventory.currentItem = blockSlot;

                    mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP,
                            new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
                    mc.player.swingArm(EnumHand.MAIN_HAND);

                    mc.player.inventory.currentItem = oldSlot;
                }
            }
        }
    }

    private List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circulate = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int) r;
        while ((float) x <= (float) cx + r) {
            int z = cz - (int) r;
            while ((float) z <= (float) cz + r) {
                int y = sphere ? cy - (int) r : cy;
                do {
                    float f = sphere ? (float) cy + r : (float) (cy + h);
                    if (!((float) y < f))
                        break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double) (r * r)) || hollow && dist < (double) ((r - 1) * (r - 1)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circulate.add(l);
                    }
                    ++y;
                } while (true);
                ++z;
            }
            ++x;
        }
        return circulate;
    }

    private double distanceToBlock(Entity entity, BlockPos pos) {
        return distance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }

    private double distance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    private boolean blocksIsEmpty() {
        for (int i = 0; i < 9; ++i) {
            if (Wrapper.MC.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }

    private boolean validPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        if ((state.getBlock() instanceof BlockObsidian && obsidian.isToggle())
                || (state.getBlock() == Block.getBlockById(7) && bedrock.isToggle()))
            return mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        return false;
    }

    private BlockPos getFloorPos() {
        if (Wrapper.MC.player == null)
            return BlockPos.ORIGIN;

        return new BlockPos(Math.floor(Wrapper.MC.player.posX), Math.floor(Wrapper.MC.player.posY),
                Math.floor(Wrapper.MC.player.posZ));
    }
}