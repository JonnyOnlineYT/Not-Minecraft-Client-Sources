package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.render.EventRender2D;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.movement.MovementUtils;
import Celestial.utils.render.ClientHelper;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;

import java.awt.*;

public class Crosshair extends Module
{
    public static ColorSetting colorGlobal;
    public static ListSetting crosshairMode;
    public BooleanSetting dynamic;
    public NumberSetting width;
    public NumberSetting widteh;
    public NumberSetting radeus;
    public NumberSetting gap;
    public NumberSetting length;
    public NumberSetting dynamicGap;
    private float coldwnLast;

    public Crosshair() {
        super("Crosshair", "Изменяет ваш прицел", ModuleCategory.Render);
        this.coldwnLast = 0.0f;
        Crosshair.crosshairMode = new ListSetting("Crosshair Mode", "Smooth", () -> true, new String[] { "Smooth", "Border", "Rect", "Circle" });
        this.dynamic = new BooleanSetting("Dynamic", false, () -> !Crosshair.crosshairMode.currentMode.equals("Circle"));
        this.dynamicGap = new NumberSetting("Dynamic Gap", 3.0f, 1.0f, 20.0f, 1.0f, this.dynamic::getCurrentValue);
        this.gap = new NumberSetting("Gap", 2.0f, 0.0f, 10.0f, 0.1f, () -> !Crosshair.crosshairMode.currentMode.equals("Circle"));
        Crosshair.colorGlobal = new ColorSetting("Crosshair Color", new Color(16777215).getRGB(), () -> true);
        this.width = new NumberSetting("Width", 1.0f, 0.0f, 8.0f, 1.0f, () -> !Crosshair.crosshairMode.currentMode.equals("Circle"));
        this.length = new NumberSetting("Length", 3.0f, 0.5f, 30.0f, 1.0f, () -> !Crosshair.crosshairMode.currentMode.equals("Circle"));
        this.radeus = new NumberSetting("Radius", 3.0f, 1.0f, 10.0f, 1.0f, () -> Crosshair.crosshairMode.currentMode.equals("Circle"));
        this.widteh = new NumberSetting("Width", 3.0f, 1.0f, 3.0f, 1.0f, () -> Crosshair.crosshairMode.currentMode.equals("Circle"));
        this.addSettings(Crosshair.crosshairMode, this.dynamic, this.dynamicGap, this.gap, Crosshair.colorGlobal, this.width, this.length, this.radeus, this.widteh);
    }

    @EventTarget
    public void onRender2D(final EventRender2D event) {
        final int crosshairColor = Crosshair.colorGlobal.getColorValue();
        final float screenWidth = (float)event.getResolution().getScaledWidth();
        final float screenHeight = (float)event.getResolution().getScaledHeight();
        final float width = screenWidth / 2.0f;
        final float height = screenHeight / 2.0f;
        final boolean dyn = this.dynamic.getCurrentValue();
        final float dyngap = this.dynamicGap.getNumberValue();
        final float wid = this.width.getNumberValue();
        final float len = this.length.getNumberValue();
        final boolean isMoving = dyn && MovementUtils.isMoving();
        final float gaps = isMoving ? dyngap : this.gap.getNumberValue();
        final String mode = Crosshair.crosshairMode.getOptions();
        if (mode.equalsIgnoreCase("Smooth")) {
            RenderUtils.drawSmoothRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, crosshairColor);
            RenderUtils.drawSmoothRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, crosshairColor);
            RenderUtils.drawSmoothRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, crosshairColor);
            RenderUtils.drawSmoothRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, crosshairColor);
        }
        else if (mode.equalsIgnoreCase("Border")) {
            RenderUtils.drawBorderedRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            RenderUtils.drawBorderedRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            RenderUtils.drawBorderedRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
            RenderUtils.drawBorderedRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, 0.5, Color.WHITE.getRGB(), crosshairColor, true);
        }
        else if (mode.equalsIgnoreCase("Rect")) {
            RenderUtils.drawRect(width - gaps - len, height - wid / 2.0f, width - gaps, height + wid / 2.0f, crosshairColor);
            RenderUtils.drawRect(width + gaps, height - wid / 2.0f, width + gaps + len, height + wid / 2.0f, crosshairColor);
            RenderUtils.drawRect(width - wid / 2.0f, height - gaps - len, width + wid / 2.0f, height - gaps, crosshairColor);
            RenderUtils.drawRect(width - wid / 2.0f, height + gaps, width + wid / 2.0f, height + gaps + len, crosshairColor);
        }
        else if (mode.equalsIgnoreCase("Circle")) {
            final double cinc = Crosshair.mc.player.getCooledAttackStrength(0.0f) * 359.0f;
            this.coldwnLast = (float) MathematicHelper.lerp(this.coldwnLast, (float)cinc, 0.30000001192092896);
            ClientHelper.drawCircle2(width, height, 1.0f, 360.0f, this.radeus.getNumberValue(), new Color(52, 52, 52, 190).hashCode(), (int)this.widteh.getNumberValue());
            ClientHelper.drawCircle2(width, height, 1.0f, 1.0f + this.coldwnLast, this.radeus.getNumberValue(), crosshairColor, (int)this.widteh.getNumberValue());
        }
    }
}