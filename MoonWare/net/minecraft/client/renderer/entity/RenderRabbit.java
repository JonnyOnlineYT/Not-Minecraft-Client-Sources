package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRabbit;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;

public class RenderRabbit extends RenderLiving<EntityRabbit>
{
    private static final Namespaced BROWN = new Namespaced("textures/entity/rabbit/brown.png");
    private static final Namespaced WHITE = new Namespaced("textures/entity/rabbit/white.png");
    private static final Namespaced BLACK = new Namespaced("textures/entity/rabbit/black.png");
    private static final Namespaced GOLD = new Namespaced("textures/entity/rabbit/gold.png");
    private static final Namespaced SALT = new Namespaced("textures/entity/rabbit/salt.png");
    private static final Namespaced WHITE_SPLOTCHED = new Namespaced("textures/entity/rabbit/white_splotched.png");
    private static final Namespaced TOAST = new Namespaced("textures/entity/rabbit/toast.png");
    private static final Namespaced CAERBANNOG = new Namespaced("textures/entity/rabbit/caerbannog.png");

    public RenderRabbit(RenderManager p_i47196_1_)
    {
        super(p_i47196_1_, new ModelRabbit(), 0.3F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityRabbit entity)
    {
        String s = Formatting.strip(entity.getName());

        if (s != null && "Toast".equals(s))
        {
            return TOAST;
        }
        else
        {
            switch (entity.getRabbitType())
            {
                case 0:
                default:
                    return BROWN;

                case 1:
                    return WHITE;

                case 2:
                    return BLACK;

                case 3:
                    return WHITE_SPLOTCHED;

                case 4:
                    return GOLD;

                case 5:
                    return SALT;

                case 99:
                    return CAERBANNOG;
            }
        }
    }
}
