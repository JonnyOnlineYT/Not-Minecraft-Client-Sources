package org.spray.heaven.features.module.modules.combat;

import java.util.Arrays;
import java.util.Objects;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "Criticals", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class Criticals extends Module {

    private Setting mode = register(new Setting("Mode", "Packet", Arrays.asList("Packet", "Sentiel", "SunRise", "NCP")));

    @Override
    public void onUpdate() {
        setSuffix(mode.getCurrentMode());
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(EventType.SEND)) {
            if (event.getPacket() instanceof CPacketUseEntity
                    && ((CPacketUseEntity) event.getPacket()).getAction() == Action.ATTACK) {

                switch (mode.getCurrentMode()) {
                    case "Packet":
                        Wrapper.sendPacket(
                                new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0645, mc.player.posZ, false));
                        Wrapper.sendPacket(
                                new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        break;

                    case "NCP":
                        if (!(mc.gameSettings.keyBindJump.isKeyDown() || mc.player.isInWater()) && mc.player.onGround) {
                            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05000000074505806, mc.player.posZ, false));
                            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.012511000037193298, mc.player.posZ, false));
                            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        }
                    case "SunRise":
                        if (!(mc.gameSettings.keyBindJump.isKeyDown() || mc.player.isInWater()) && mc.player.onGround) {
                            Objects.requireNonNull(mc.getConnection())
                                    .sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.001, mc.player.posZ, false));
                            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        }
                        break;
                    case "Sentiel":
                        if (mc.player.onGround) {
                            mc.player.motionY = -1e-2;
                            Wrapper.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.28E-9D,
                                    mc.player.posZ, true));
                            Wrapper.sendPacket(
                                    new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        }
                        break;
                }
            }
        }
    }
}
