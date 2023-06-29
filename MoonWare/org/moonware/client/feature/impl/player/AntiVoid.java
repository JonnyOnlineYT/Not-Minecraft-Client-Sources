package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.movement.Flight;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class AntiVoid extends Feature {

    private final NumberSetting fallDist;
    private final ListSetting antiVoidMode;
    public float fall;
    public boolean toggleFeature;

    public AntiVoid() {
        super("AntiVoid", "Не дает вам упасть в бездну", Type.Other);
        antiVoidMode = new ListSetting("AntiVoid Mode", "Packet", () -> true, "Packet", "Spoof", "High-Motion", "Invalid Position", "Invalid Pitch", "Flag");
        fallDist = new NumberSetting("Fall Distance", 5, 1, 10, 1, () -> true);
        addSettings(antiVoidMode, fallDist);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        String mode = antiVoidMode.getOptions();
        setSuffix(mode);
        if (MoonWare.featureManager.getFeatureByClass(Flight.class).getState()) {
            return;
        }

        if (!Minecraft.player.onGround && !Minecraft.player.isCollidedVertically) {
            if (Minecraft.player.fallDistance > fallDist.getNumberValue()) {
                if (Minecraft.world.getBlockState(new BlockPos(0, -fall, 0)).getBlock() == Blocks.AIR) {
                    if (mode.equalsIgnoreCase("High-Motion")) {
                        Minecraft.player.motionY += 3f;
                    } else if (mode.equals("Packet")) {
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + 12, Minecraft.player.posZ, true));
                    } else if (mode.equals("Spoof")) {
                        Minecraft.player.connection.sendPacket(new CPacketPlayer(true));
                    } else if (mode.equals("Flag")) {
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX + 1, Minecraft.player.posY + 1, Minecraft.player.posZ + 1, true));
                    } else if (mode.equals("Invalid Pitch")) {
                        event.setOnGround(true);
                        event.setPitch(-91);
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, true));
                    } else if (mode.equals("Invalid Position")) {
                        if (Minecraft.player.onGround) {
                            toggleFeature = true;
                        }

                        if (!toggleFeature && NoFall.noFallMode.currentMode.equals("Matrix")) {
                            MWUtils.sendChat("Переключи мод NoFall на другой!");
                        }

                        if (!toggleFeature && !NoFall.noFallMode.currentMode.equals("Matrix")) {
                            Minecraft.player.setPosition(Minecraft.player.posX + 1, Minecraft.player.posY + 1, Minecraft.player.posZ + 1);
                        } else {
                            setState(false);
                        }
                    }
                }
            }
        }
    }
}