package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import org.moonware.client.helpers.math.GCDFix;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class AntiAim extends Feature {

    public float rot;
    public NumberSetting spinSpeed;
    public ListSetting pitchMode;
    public ListSetting mode;
    public ListSetting degreeMode;

    public AntiAim() {
        super("AntiAim", "АнтиАим как в CSGO", Type.Other);
        mode = new ListSetting("Yaw Mode", "Jitter", () -> true, "Freestanding", "Spin", "Jitter");
        spinSpeed = new NumberSetting("Spin Speed", 1, 0, 10, 0.1f, () -> degreeMode.currentMode.equals("Spin"));
        pitchMode = new ListSetting("Custom Pitch", "Down", () -> true, "None", "Down", "Up", "Fake-Down", "Fake-Up");
        degreeMode = new ListSetting("Degree Mode", "Spin", () -> true, "Random", "Spin");
        addSettings(mode, spinSpeed, pitchMode, degreeMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String antiAimMode = mode.getCurrentMode();
        setSuffix(antiAimMode);
        float speed = spinSpeed.getNumberValue() * 10;
        switch (pitchMode.currentMode) {
            case "Down":
                event.setPitch(90);
                Minecraft.player.rotationPitchHead = 90;
                break;
            case "Up":
                event.setPitch(-90);
                Minecraft.player.rotationPitchHead = -90;
                break;
            case "Fake-Down":
                Minecraft.player.rotationPitchHead = 90;
                break;
            case "Fake-Up":
                Minecraft.player.rotationPitchHead = -90;
                break;
        }
        if (mode.currentMode.equals("Jitter")) {
            float yaw = Minecraft.player.rotationYaw + 180 + (Minecraft.player.ticksExisted % 8 < 4 ? MWUtils.randomFloat(-90, 90) : -MWUtils.randomFloat(90, -90));
            event.setYaw(GCDFix.getFixedRotation(yaw));
            Minecraft.player.renderYawOffset = yaw;
            Minecraft.player.rotationYawHead = yaw;
        } else if (antiAimMode.equals("Freestanding")) {
            float yaw = (float) (Minecraft.player.rotationYaw + 5 + Math.random() * 175);
            event.setYaw(GCDFix.getFixedRotation(yaw));
            Minecraft.player.renderYawOffset = yaw;
            Minecraft.player.rotationYawHead = yaw;
        } else if (antiAimMode.equalsIgnoreCase("Spin")) {
            float yaw = GCDFix.getFixedRotation((float) (Math.floor(spinAim(speed)) + MWUtils.randomFloat(-4, 1)));
            event.setYaw(yaw);
            Minecraft.player.renderYawOffset = yaw;
            Minecraft.player.rotationYawHead = yaw;
        }

        if (Minecraft.player.isSneaking()) {
            if (degreeMode.currentMode.equals("Spin")) {
                float yaw = GCDFix.getFixedRotation((float) (Math.floor(spinAim(speed)) + MWUtils.randomFloat(-4, 1)));
                event.setYaw(yaw);
                Minecraft.player.renderYawOffset = yaw;
                Minecraft.player.rotationYawHead = yaw;
            } else if (degreeMode.currentMode.equals("Random")) {
                float yaw = (float) (Minecraft.player.rotationYaw + Math.floor(spinAim(speed) + (Minecraft.player.ticksExisted % 8 < 4 ? MWUtils.randomFloat(33, 22) : -MWUtils.randomFloat(33, 22))));
                event.setYaw(yaw);
                Minecraft.player.renderYawOffset = yaw;
                Minecraft.player.rotationYawHead = yaw;
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}