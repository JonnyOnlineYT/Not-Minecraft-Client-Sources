package org.moonware.client.helpers.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;

public class ESPUtil implements Utils {
    private static final Frustum frustum = new Frustum();

    public static boolean isInView(Entity ent) {
        frustum.setPosition(Minecraft.getRenderViewEntity().posX, Minecraft.getRenderViewEntity().posY, Minecraft.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }


}
