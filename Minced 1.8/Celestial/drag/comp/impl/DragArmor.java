package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.Hud;

public class DragArmor extends DragComp
{
    public DragArmor()
    {
        super("Armor Status", 380, 357, 4, 1);
    }

    @Override
    public boolean allowDraw()
    {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.armorHUD.getCurrentValue();
    }
}
