package ru.rockstar.client.features.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventTransformSideFirstPerson;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class SwingAnimations extends Feature
{
    public static BooleanSetting auraOnly;
    public static NumberSetting smooth;
    public static NumberSetting spinSpeed;
    public static BooleanSetting smallItem;
    public static NumberSetting smallItemSize;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting swordAnim;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;
    public static NumberSetting x;
    public static NumberSetting y;
    public static NumberSetting z;
    public static NumberSetting rotate1;
    public static NumberSetting rotate2;
    public static NumberSetting rotate3;
    public static NumberSetting angle;
    public static NumberSetting SwingX;
    public static NumberSetting SwingY;
    public static NumberSetting SwingZ;
    public static NumberSetting SwingRotate1;
    public static NumberSetting SwingRotate2;
    public static NumberSetting SwingRotate3;
    public static NumberSetting SwingAngle;
    public static NumberSetting scale;
    public static NumberSetting fapSmooth;
    
    public SwingAnimations() {
        super("SwingAnimations", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044e \u043d\u0430 \u0440\u0443\u043a\u0443", 0, Category.VISUALS);
        SwingAnimations.auraOnly = new BooleanSetting("Aura Only", true, () -> true);
        SwingAnimations.smooth = new NumberSetting("Swing Smooth", 8.0f, 1.0f, 20.0f, 1.0f, () -> !SwingAnimations.swordAnim.currentMode.equals("Neutral"));
        SwingAnimations.spinSpeed = new NumberSetting("Spin Speed", 4.0f, 1.0f, 10.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Astolfo") || SwingAnimations.swordAnim.currentMode.equals("Spin"));
        SwingAnimations.smallItem = new BooleanSetting("Mini Item", false, () -> !SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.smallItemSize = new NumberSetting("Mini Item Size", 0.7f, 0.1f, 0.9f, 0.1f, () -> !SwingAnimations.swordAnim.currentMode.equals("Custom") && SwingAnimations.smallItem.getBoolValue());
        SwingAnimations.item360 = new BooleanSetting("Item360", false, () -> true);
        SwingAnimations.item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> SwingAnimations.item360.getBoolValue(), new String[] { "Horizontal", "Vertical", "Zoom" });
        SwingAnimations.item360Hand = new ListSetting("Item360 Hand", "All", () -> SwingAnimations.item360.getBoolValue(), new String[] { "All", "Left", "Right" });
        SwingAnimations.item360Speed = new NumberSetting("Item360 Speed", 5.0f, 1.0f, 15.0f, 1.0f, () -> SwingAnimations.item360.getBoolValue());
        SwingAnimations.swordAnim = new ListSetting("Sword Animation", "Jello", () -> true, new String[] { "Spin", "Swank", "Sigma", "Jello", "Fap", "Big", "Astolfo", "Neutral", "Custom" });
        SwingAnimations.fapSmooth = new NumberSetting("Fap Smooth", 5.0f, 0.5f, 10.0f, 0.5f, () -> SwingAnimations.swordAnim.currentMode.equals("Fap"));
        SwingAnimations.x = new NumberSetting("X", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.y = new NumberSetting("Y", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.z = new NumberSetting("Z", -1.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.rotate1 = new NumberSetting("Rotate 1", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.rotate2 = new NumberSetting("Rotate 2", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.rotate3 = new NumberSetting("Rotate 3", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.angle = new NumberSetting("Angle", 50.0f, 0.0f, 360.0f, 5.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingX = new NumberSetting("Swing X", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingY = new NumberSetting("Swing Y", 0.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingZ = new NumberSetting("Swing Z", -1.0f, -1.0f, 1.0f, 0.01f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingRotate1 = new NumberSetting("Swing Rotate 1", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingRotate2 = new NumberSetting("Swing Rotate 2", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingRotate3 = new NumberSetting("Swing Rotate 3", 0.0f, -360.0f, 360.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.SwingAngle = new NumberSetting("Swing angle", 50.0f, 0.0f, 360.0f, 5.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        SwingAnimations.scale = new NumberSetting("Scale", 1.0f, 0.1f, 5.0f, 0.1f, () -> SwingAnimations.swordAnim.currentMode.equals("Custom"));
        this.addSettings(SwingAnimations.auraOnly, SwingAnimations.swordAnim, SwingAnimations.fapSmooth, SwingAnimations.x, SwingAnimations.y, SwingAnimations.z, SwingAnimations.rotate1, SwingAnimations.rotate2, SwingAnimations.rotate3, SwingAnimations.angle, SwingAnimations.SwingX, SwingAnimations.SwingY, SwingAnimations.SwingZ, SwingAnimations.SwingRotate1, SwingAnimations.SwingRotate2, SwingAnimations.SwingRotate3, SwingAnimations.SwingAngle, SwingAnimations.scale, SwingAnimations.smooth, SwingAnimations.spinSpeed, SwingAnimations.smallItem, SwingAnimations.smallItemSize, SwingAnimations.item360, SwingAnimations.item360Mode, SwingAnimations.item360Hand, SwingAnimations.item360Speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(SwingAnimations.swordAnim.getCurrentMode(), true);
    }
}