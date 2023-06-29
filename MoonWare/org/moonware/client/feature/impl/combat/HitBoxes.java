package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.moonware.client.event.EventTarget;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class HitBoxes extends Feature {

    public static NumberSetting expand;

    public HitBoxes() {
        super("HitBoxes", "Увеличивает хитбокс у ентити", Type.Combat);
        expand = new NumberSetting("Expand", 0.2F, 0.01F, 2.0F, 0.01F, () -> true);
        addSettings(expand);
    }

    @EventTarget
    public void onEnable() {
        setSuffix("" + MathematicHelper.round(expand.getNumberValue(), 2));
        for (Entity entity : Minecraft.world.playerEntities) {
            if(entity != Minecraft.player){
                float width = entity.width;
                float height = entity.height;
                float expandValue = expand.getNumberValue();
                entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - width - expandValue, entity.posY, entity.posZ + width + expandValue, entity.posX + width + expandValue, entity.posY + height + expandValue, entity.posZ - width - expandValue));
            }
        }
    }
}
