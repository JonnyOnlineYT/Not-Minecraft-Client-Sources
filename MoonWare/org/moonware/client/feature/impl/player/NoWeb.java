package org.moonware.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class NoWeb extends Feature {

    public ListSetting noWebMode;
    public NumberSetting webSpeed;
    public NumberSetting webJumpMotion;

    public NoWeb() {
        super("NoWeb", "Позволяет быстро ходить в паутине", Type.Other);
        noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> true, "Matrix", "Matrix New", "NCP");
        webSpeed = new NumberSetting("Web Speed", 0.8F, 0.1F, 2, 0.1F, () -> noWebMode.currentMode.equals("Matrix New"));
        webJumpMotion = new NumberSetting("Jump Motion", 2F, 0F, 10F, 1, () -> noWebMode.currentMode.equals("Matrix New"));
        addSettings(noWebMode, webJumpMotion, webSpeed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = noWebMode.getOptions();
        setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix New")) {
            BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.6, Minecraft.player.posZ);
            Block block = Minecraft.world.getBlockState(blockPos).getBlock();
            if (Minecraft.player.isInWeb) {
                Minecraft.player.motionY += 2F;
            } else if (Block.getIdFromBlock(block) == 30) {
                if (webJumpMotion.getNumberValue() > 0) {
                    Minecraft.player.motionY += webJumpMotion.getNumberValue();
                } else {
                    Minecraft.player.motionY = 0;
                }
                MovementHelper.setSpeed(webSpeed.getNumberValue());
                Minecraft.gameSettings.keyBindJump.pressed = false;
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        String mode = noWebMode.getOptions();
        setSuffix(mode);
        if (getState()) {
            if (mode.equalsIgnoreCase("Matrix")) {
                if (Minecraft.player.onGround && Minecraft.player.isInWeb) {
                    Minecraft.player.isInWeb = true;
                } else {
                    if (Minecraft.gameSettings.keyBindJump.isKeyDown())
                        return;
                    Minecraft.player.isInWeb = false;
                }
                if (Minecraft.player.isInWeb && !Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.483);
                }
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (Minecraft.player.onGround && Minecraft.player.isInWeb) {
                    Minecraft.player.isInWeb = true;
                } else {
                    if (Minecraft.gameSettings.keyBindJump.isKeyDown())
                        return;
                    Minecraft.player.isInWeb = false;
                }
                if (Minecraft.player.isInWeb && !Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.403);
                }
            }
        }
    }
}