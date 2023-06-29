package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class WallClimb extends Feature {

    public static NumberSetting climbTicks;
    private final TimerHelper timerHelper = new TimerHelper();
    TimerHelper timerUtils = new TimerHelper();
    public static ListSetting spidermode;

    public WallClimb() {
        super("Spider", "Автоматически взберается на стены", Type.Movement);
        climbTicks = new NumberSetting("Climb Ticks", 1, 0, 5, 0.1F, () -> true);
        spidermode = new ListSetting("Spider Mode", "Default","Default", "Sunrise Disabler");
        addSettings(climbTicks, spidermode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        setSuffix("" + climbTicks.getNumberValue());
        if (spidermode.currentMode.equalsIgnoreCase("Default")) {
            if (MovementHelper.isMoving() && Minecraft.player.isCollidedHorizontally) {
                if (timerHelper.hasReached(climbTicks.getNumberValue() * 100)) {
                    event.setOnGround(true);
                    Minecraft.player.onGround = true;
                    Minecraft.player.isCollidedVertically = true;
                    Minecraft.player.isCollidedHorizontally = true;
                    Minecraft.player.isAirBorne = true;
                    Minecraft.player.jump();
                    timerHelper.reset();
                }
            }
        }else if (spidermode.currentMode.equalsIgnoreCase("Sunrise Disabler")) {
            if (Minecraft.player.isCollidedHorizontally) {
                if (spidermode.currentMode.equalsIgnoreCase("Sunrise Disabler")) {
                    if (Minecraft.playerController.getCurrentGameType() == GameType.ADVENTURE) {
                    } else {
                        int block = -1;
                        for (int i = 0; i < 9; i++) {
                            ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
                            if (s.getItem() instanceof ItemBlock) {
                                block = i;
                                break;
                            }
                        }
                        if (block == -1 && timerUtils.hasReached(1000)) {
                            MWUtils.sendChat("Вам нужен любой блок в хотбаре для этого Spider`а.");
                            timerUtils.reset();
                            return;
                        }
                        if (timerUtils.hasReached(climbTicks.getCurrentValue() * 55)) {
                            try {
                                if (block != -1 && Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.hitVec != null
                                        && Minecraft.objectMouseOver.getBlockPos() != null
                                        && Minecraft.objectMouseOver.sideHit != null) {
                                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(block));
                                    float prevPitch = Minecraft.player.rotationPitch;
                                    Minecraft.player.rotationPitch = -60;
                                    Minecraft.gameRenderer.getMouseOver(1);
                                    Vec3d facing = Minecraft.objectMouseOver.hitVec;
                                    BlockPos stack = Minecraft.objectMouseOver.getBlockPos();
                                    float f = (float) (facing.xCoord - (double) stack.getX());
                                    float f1 = (float) (facing.yCoord - (double) stack.getY());
                                    float f2 = (float) (facing.zCoord - (double) stack.getZ());
                                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player,
                                            CPacketEntityAction.Action.START_SNEAKING));
                                    if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, 2, 0))
                                            .getBlock() == Blocks.AIR) {
                                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack,
                                                Minecraft.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
                                    } else {
                                        Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(
                                                CPacketPlayerDigging.Action.START_DESTROY_BLOCK, stack, Minecraft.objectMouseOver.sideHit));
                                        Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(
                                                CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, stack, Minecraft.objectMouseOver.sideHit));
                                    }
                                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player,
                                            CPacketEntityAction.Action.STOP_SNEAKING));
                                    Minecraft.player.rotationPitch = prevPitch;
                                    Minecraft.gameRenderer.getMouseOver(1);
                                    Minecraft.player.connection
                                            .sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                                    event.setOnGround(true);
                                    Minecraft.player.onGround = true;
                                    Minecraft.player.isCollidedVertically = true;
                                    Minecraft.player.isCollidedHorizontally = true;
                                    Minecraft.player.isAirBorne = true;
                                    Minecraft.player.jump();
                                    timerUtils.reset();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
