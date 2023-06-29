package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;

public class SwingAnimations extends Module {
    public static boolean blocking;
    public static ListSetting swordAnim;
    public static BooleanSetting auraOnly;
    public static BooleanSetting item360;
    public static BooleanSetting stopInSwing;

    public static NumberSetting item360Speed;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;

    public static NumberSetting swingSpeed;
    public static NumberSetting spinSpeed;

    public SwingAnimations() {
        super("Sword Translate", "Изменяет движение предмета при нажатии на ЛКМ", ModuleCategory.Render);
        swordAnim = new ListSetting("Animation Mode", "Minced", () -> true, "Minced", "Liquid", "Normal");
        auraOnly = new BooleanSetting("Only Aura", false, () -> true);
        swingSpeed = new NumberSetting("Swing Speed", "�������� ����� ����", 8.0F, 1.0F, 20.0F, 1, () -> true);
        spinSpeed = new NumberSetting("Spin Speed", 4, 1, 10, 1, () -> swordAnim.currentMode.equals("Spin"));
        item360 = new BooleanSetting("Item360", false, () -> true);
        stopInSwing = new BooleanSetting("Stop on hit", true, () -> item360.getCurrentValue());

        item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> item360.getCurrentValue(), "Horizontal", "Vertical", "Zoom");
        item360Hand = new ListSetting("Item360 Hand", "All", () -> item360.getCurrentValue(), "All", "Left", "Right");
        item360Speed = new NumberSetting("Item360 Speed", 8, 1, 15, 1, () -> item360.getCurrentValue());
        addSettings(auraOnly, swordAnim, spinSpeed, swingSpeed, item360, item360Mode, item360Hand, stopInSwing, item360Speed);
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        String anim = swordAnim.getOptions();

    }


}