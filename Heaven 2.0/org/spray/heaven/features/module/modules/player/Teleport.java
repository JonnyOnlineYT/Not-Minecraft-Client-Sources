package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "Teleport", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class Teleport extends Module {

    private final Timer timer = new Timer();
    private BlockPos teleportPosition = null;
    private float reach;
    private boolean teleport;

    @Override
    public void onEnable() {
        reach = mc.playerController.getBlockReachDistance();
        EntityUtil.setReach(mc.player, 50);
    }

    @Override
    public void onDisable() {
        EntityUtil.setReach(mc.player, reach);
        teleport = false;
        timer.reset();
    }

    @Override
    public void onUpdate() {
        RayTraceResult object = mc.objectMouseOver;

        if (object.hitVec != null) {
            for (float offset = -2.0F; offset < 18.0F; offset++) {
                double[] mouseOverPos = new double[] { object.hitVec.xCoord, object.hitVec.yCoord + offset,
                        object.hitVec.zCoord };

                BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                Block blockBelow = BlockUtil.getBlock(blockBelowPos);

                if (mc.inGameHasFocus) {
                    teleportPosition = blockBelowPos;
                    break;
                } else {
                    teleportPosition = null;
                }
            }
        }

        if (teleportPosition != null && Mouse.isButtonDown(1) && !teleport) {
            timer.reset();
            teleport = true;
        }

        if (teleport) {
            double x = teleportPosition.getX();
            double y = teleportPosition.getY() + 2;
            double z = teleportPosition.getZ();

            mc.getConnection()
                    .sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 5, mc.player.posZ, false));
            Wrapper.getPlayer().setPosition(x, y, z);
            mc.getConnection()
                    .sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, false));
            mc.getConnection().sendPacket(new CPacketPlayer(true));

            teleport = false;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!event.getType().equals(EventType.SEND))
            return;

        if (teleport) {
            if (event.getPacket() instanceof CPacketKeepAlive) {
                event.cancel();
            }
            if (event.getPacket() instanceof CPacketConfirmTeleport) {
                event.cancel();
            }
        }
    }
}