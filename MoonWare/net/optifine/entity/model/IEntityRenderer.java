package net.optifine.entity.model;

import net.minecraft.util.Namespaced;

public interface IEntityRenderer
{
    Class getEntityClass();

    void setEntityClass(Class var1);

    Namespaced getLocationTextureCustom();

    void setLocationTextureCustom(Namespaced var1);
}
