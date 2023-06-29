package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.event.events.impl.player.BlockHelper;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.event.events.impl.render.EventRender3D;
import Celestial.event.events.impl.render.EventRenderBlock;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.math.RotationHelper;
import Celestial.utils.other.ChatUtils;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XRay extends Module {
    public static int done;
    public static int all;
    public static BooleanSetting bypass;
    public static BooleanSetting diamond;
    public static BooleanSetting gold;
    public static BooleanSetting iron;
    public static BooleanSetting emerald;
    public static BooleanSetting redstone;
    public static BooleanSetting lapis;
    public static BooleanSetting coal;
    private final NumberSetting checkSpeed;
    private final NumberSetting renderDist;
    private final NumberSetting rxz;
    private final NumberSetting ry;
    public static ArrayList<BlockPos> ores;
    private final ArrayList<BlockPos> toCheck = new ArrayList();
    private final List<Vec3i> blocks = new CopyOnWriteArrayList<Vec3i>();

    public XRay() {
        super("XRay", "Позволяет просматривать местоположение какой-либо руды", ModuleCategory.Render);
        bypass = new BooleanSetting("Bypass", false, () -> true);
        renderDist = new NumberSetting("Render Distance", 35.0f, 15.0f, 150.0f, 5.0f, () -> !bypass.getCurrentValue());
        diamond = new BooleanSetting("Diamond", true, () -> true);
        gold = new BooleanSetting("Gold", false, () -> true);
        iron = new BooleanSetting("Iron", false, () -> true);
        emerald = new BooleanSetting("Emerald", false, () -> true);
        redstone = new BooleanSetting("Redstone", false, () -> true);
        lapis = new BooleanSetting("Lapis", false, () -> true);
        coal = new BooleanSetting("Coal", false, () -> true);
        checkSpeed = new NumberSetting("CheckSpeed", 4.0f, 1.0f, 10.0f, 1.0f, bypass::getCurrentValue);
        rxz = new NumberSetting("Radius XZ", 20.0f, 5.0f, 200.0f, 1.0f, bypass::getCurrentValue);
        ry = new NumberSetting("Radius Y", 6.0f, 2.0f, 50.0f, 1.0f, bypass::getCurrentValue);
        addSettings(renderDist, bypass, checkSpeed, rxz, ry, diamond, gold, iron, emerald, redstone, lapis, coal);
    }

    private boolean isEnabledOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;

        if (diamond.getCurrentValue() && id != 0) {
            check = 56;
        }

        if (gold.getCurrentValue() && id != 0) {
            check1 = 14;
        }

        if (iron.getCurrentValue() && id != 0) {
            check2 = 15;
        }

        if (emerald.getCurrentValue() && id != 0) {
            check3 = 129;
        }

        if (redstone.getCurrentValue() && id != 0) {
            check4 = 73;
        }

        if (coal.getCurrentValue() && id != 0) {
            check5 = 16;
        }

        if (lapis.getCurrentValue() && id != 0) {
            check6 = 21;
        }

        if (id == 0) {
            return false;
        }

        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(Helper.mc.player.posX - (double) x, Helper.mc.player.posY - (double) y, Helper.mc.player.posZ - (double) z);
        BlockPos max = new BlockPos(Helper.mc.player.posX + (double) x, Helper.mc.player.posY + (double) y, Helper.mc.player.posZ + (double) z);
        return BlockHelper.getAllInBox(min, max);
    }

    @Override
    public void onEnable() {
        if (bypass.getCurrentValue()) {
            int radXZ = (int) rxz.getCurrentValue();
            int radY = (int) ry.getCurrentValue();
            ArrayList<BlockPos> blockPositions = getBlocks(radXZ, radY, radXZ);

            for (BlockPos pos : blockPositions) {
                IBlockState state = BlockHelper.getState(pos);

                if (!isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
                    continue;
                }

                toCheck.add(pos);
            }

            all = toCheck.size();
            done = 0;
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        Helper.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String allDone;
        String string = allDone = done == all ? "Done: " + all : "" + done + " / " + all;

        if (bypass.getCurrentValue()) {
            setSuffix(allDone);
            int i = 0;

            while ((float) i < checkSpeed.getCurrentValue()) {
                if (toCheck.size() < 1) {
                    return;
                }

                BlockPos pos = toCheck.remove(0);
                ++done;
                Helper.mc.playerController.clickBlock(pos, EnumFacing.UP);
                ++i;
            }
        }

        if (!bypass.getCurrentValue() && Helper.mc.player.ticksExisted % 100 == 0) {
            Helper.mc.renderGlobal.loadRenderers();
            ChatUtils.addChatMessage("������������ �����...");
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (bypass.getCurrentValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange) e.getPacket();

                if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock())) && !Helper.mc.world.isAirBlock(p.getBlockPosition())) {
                    ores.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();

                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (!isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock())) || Helper.mc.world.isAirBlock(dat.getPos())) {
                        continue;
                    }

                    ores.add(dat.getPos());
                }
            }
        }
    }

    @EventTarget
    public void onRenderBlock(EventRenderBlock event) {
        if (!bypass.getCurrentValue()) {
            BlockPos pos = event.getPos();
            IBlockState blockState = event.getState();

            if (isEnabledOre(Block.getIdFromBlock(blockState.getBlock()))) {
                Vec3i vec3i = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
                blocks.add(vec3i);
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (bypass.getCurrentValue()) {
            for (BlockPos pos : ores) {
                IBlockState state = BlockHelper.getState(pos);
                Block stateBlock = state.getBlock();

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 56 && diamond.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 56) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 14 && gold.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 14) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 15 && iron.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 15) {
                    RenderUtils.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 129 && emerald.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 129) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 73 && redstone.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 73) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 16 && coal.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 16) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 21 && lapis.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 21) {
                    RenderUtils.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
            }
        } else {
            for (Vec3i neededBlock : blocks) {
                BlockPos pos = new BlockPos(neededBlock);
                IBlockState state = BlockHelper.getState(pos);
                Block stateBlock = state.getBlock();
                Block block = Helper.mc.world.getBlockState(pos).getBlock();

                if (block instanceof BlockAir) {
                    continue;
                }

                if (RotationHelper.getDistance2(Helper.mc.player.posX, Helper.mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > (double) renderDist.getCurrentValue()) {
                    blocks.remove(neededBlock);
                    continue;
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 56 && diamond.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 56) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 14 && gold.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 14) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 15 && iron.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 15) {
                    RenderUtils.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 129 && emerald.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 129) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 73 && redstone.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 73) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 16 && coal.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 16) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }

                if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == 21 && lapis.getCurrentValue() && Block.getIdFromBlock(stateBlock) == 21) {
                    RenderUtils.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
            }
        }
    }

    private boolean isCheckableOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;

        if (diamond.getCurrentValue() && id != 0) {
            check = 56;
        }

        if (gold.getCurrentValue() && id != 0) {
            check1 = 14;
        }

        if (iron.getCurrentValue() && id != 0) {
            check2 = 15;
        }

        if (emerald.getCurrentValue() && id != 0) {
            check3 = 129;
        }

        if (redstone.getCurrentValue() && id != 0) {
            check4 = 73;
        }

        if (coal.getCurrentValue() && id != 0) {
            check5 = 16;
        }

        if (lapis.getCurrentValue() && id != 0) {
            check6 = 21;
        }

        if (id == 0) {
            return false;
        }

        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    static {
        ores = new ArrayList();
    }
}
