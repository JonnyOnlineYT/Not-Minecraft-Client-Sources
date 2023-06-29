package ua.apraxia.modules.impl.move;

import com.mojang.realmsclient.gui.ChatFormatting;
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
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.math.TimerUtility;
import ua.apraxia.utility.other.ChatUtility;

public class Spider extends Module {
    TimerUtility spiderTimer = new TimerUtility();
    private final ModeSetting spiderMode = new ModeSetting("Spider Mode", "Matrix",  "Sunrise");
    public SliderSetting climbSpeed = new SliderSetting("Spider Speed", 1, 0, 5, 0.1F);

    public Spider() {
        super("Spider", Categories.Movement);
        addSetting(spiderMode, climbSpeed);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }
    @EventTarget
    public void onPreMotion(EventPreMotion eventPreMotion) {
        if (spiderMode.is("Sunrise")) {
            if (Utility.mc.playerController.getCurrentGameType() == GameType.ADVENTURE && spiderTimer.hasReached(1000)) {
                ChatUtility.addChatMessage("Возьми блоки в инвентарь арбуз");
                spiderTimer.reset();
            }
            if (Utility.mc.player.collidedHorizontally) {
                if (Utility.mc.playerController.getCurrentGameType() == GameType.ADVENTURE) {
                } else {
                    int block = -1;
                    for (int i = 0; i < 9; i++) {
                        ItemStack s = Utility.mc.player.inventory.getStackInSlot(i);
                        if (s.getItem() instanceof ItemBlock) {
                            block = i;
                            break;
                        }
                    }
                    if (block == -1 && spiderTimer.hasReached(1000)) {
                        ChatUtility.addChatMessage("Возьми блоки в инвентарь арбуз");
                        spiderTimer.reset();
                        return;
                    }
                    if (spiderTimer.hasReached(1.0f * 55)) {
                        try {
                            if (block != -1 && Utility.mc.objectMouseOver != null && Utility.mc.objectMouseOver.hitVec != null && Utility.mc.objectMouseOver.getBlockPos() != null &&Utility.mc.objectMouseOver.sideHit != null) {
                                Utility.mc.player.connection.sendPacket(new CPacketHeldItemChange(block));
                                float prevPitch = Utility.mc.player.rotationPitch;
                                Utility.mc.player.rotationPitch = -60;
                                Utility.mc.entityRenderer.getMouseOver(1);
                                Vec3d facing = Utility.mc.objectMouseOver.hitVec;
                                BlockPos stack =Utility.mc.objectMouseOver.getBlockPos();
                                float f = (float) (facing.x - (double) stack.getX());
                                float f1 = (float) (facing.y - (double) stack.getY());
                                float f2 = (float) (facing.z - (double) stack.getZ());
                                Utility.mc.player.connection.sendPacket(new CPacketEntityAction(Utility.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                                if (Utility.mc.world.getBlockState(new BlockPos(Utility.mc.player).add(0, 2, 0)).getBlock() == Blocks.AIR) {
                                    Utility.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack, Utility.mc.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
                                } else {
                                    Utility.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, stack, Utility.mc.objectMouseOver.sideHit));
                                    Utility.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, stack, Utility.mc.objectMouseOver.sideHit));
                                }
                                Utility.mc.player.connection.sendPacket(new CPacketEntityAction(Utility.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                                Utility.mc.player.rotationPitch = prevPitch;
                                Utility.mc.entityRenderer.getMouseOver(1);
                                Utility.mc.player.connection.sendPacket(new CPacketHeldItemChange(Utility.mc.player.inventory.currentItem));
                                Utility.mc.player.onGround = true;
                                Utility.mc.player.collidedVertically = true;
                                Utility.mc.player.collidedHorizontally = true;
                                Utility.mc.player.isAirBorne = true;
                                Utility.mc.player.jump();
                                spiderTimer.reset();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (spiderMode.is("Matrix")) {
            if (Utility.mc.player.collidedHorizontally) {
                if (spiderTimer.hasReached(climbSpeed.value * 55)) {
                    Utility.mc.player.onGround = true;
                    Utility.mc.player.collidedVertically = true;
                    Utility.mc.player.collidedHorizontally = true;
                    Utility.mc.player.isAirBorne = true;
                    Utility.mc.player.jump();
                    spiderTimer.reset();
                }
            }
        }
    }
}