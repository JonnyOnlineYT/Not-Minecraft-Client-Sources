package ru.rockstar.client.features.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.MoveEvent;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class NoWeb extends Feature {

    public ListSetting noWebMode;
    public NumberSetting webSpeed;
    public NumberSetting webJumpMotion;

    public NoWeb() {
        super("NoWeb", "Позволяет быстро ходить в паутине",0, Category.PLAYER);
        noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> true, "Matrix", "Matrix New", "NCP");
        webSpeed = new NumberSetting("Web Speed", 0.8F, 0.1F, 2, 0.1F, () -> noWebMode.currentMode.equals("Matrix New"));
        webJumpMotion = new NumberSetting("Jump Motion", 2F, 0F, 10F, 1, () -> noWebMode.currentMode.equals("Matrix New"));
        addSettings(noWebMode, webJumpMotion, webSpeed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = noWebMode.getOptions();
        this.setSuffix(mode, true);
        if (mode.equalsIgnoreCase("Matrix New")) {
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.6, mc.player.posZ);
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (mc.player.isInWeb) {
                mc.player.motionY += 2F;
            } else if (Block.getIdFromBlock(block) == 30) {
                if (webJumpMotion.getNumberValue() > 0) {
                    mc.player.motionY += webJumpMotion.getNumberValue();
                } else {
                    mc.player.motionY = 0;
                }
                MovementHelper.setSpeed(webSpeed.getNumberValue());
                mc.gameSettings.keyBindJump.pressed = false;
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        String mode = noWebMode.getOptions();
        this.setSuffix(mode, true);
        if (isToggled()) {
            if (mode.equalsIgnoreCase("Matrix")) {
                if (mc.player.onGround && mc.player.isInWeb) {
                    mc.player.isInWeb = true;
                } else {
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        return;
                    mc.player.isInWeb = false;
                }
                if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.483);
                }
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (mc.player.onGround && mc.player.isInWeb) {
                    mc.player.isInWeb = true;
                } else {
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        return;
                    mc.player.isInWeb = false;
                }
                if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementHelper.setEventSpeed(event, 0.403);
                }
            }
        }
    }
}