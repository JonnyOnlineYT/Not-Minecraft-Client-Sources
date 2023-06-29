package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import optifine.Config;
import shadersmod.client.Shaders;

public class ParticleItemPickup extends Particle
{
    private final Entity item;
    private final Entity target;
    private int age;
    private final int maxAge;
    private final float yOffset;
    private final RenderManager renderManager = Minecraft.getRenderManager();

    public ParticleItemPickup(World worldIn, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_)
    {
        super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
        item = p_i1233_2_;
        target = p_i1233_3_;
        maxAge = 3;
        yOffset = p_i1233_4_;
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        int i = 0;

        if (Config.isShaders())
        {
            i = Shaders.activeProgram;
            Shaders.nextEntity(item);
        }

        float f = ((float) age + partialTicks) / (float) maxAge;
        f = f * f;
        double d0 = item.posX;
        double d1 = item.posY;
        double d2 = item.posZ;
        double d3 = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double)partialTicks;
        double d4 = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double)partialTicks + (double) yOffset;
        double d5 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double)partialTicks;
        double d6 = d0 + (d3 - d0) * (double)f;
        double d7 = d1 + (d4 - d1) * (double)f;
        double d8 = d2 + (d5 - d2) * (double)f;
        int j = getBrightnessForRender(partialTicks);
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        d6 = d6 - Particle.interpPosX;
        d7 = d7 - Particle.interpPosY;
        d8 = d8 - Particle.interpPosZ;
        GlStateManager.enableLighting();
        renderManager.doRenderEntity(item, d6, d7, d8, item.rotationYaw, partialTicks, false);

        if (Config.isShaders())
        {
            Shaders.useProgram(i);
        }
    }

    public void onUpdate()
    {
        ++age;

        if (age == maxAge)
        {
            setExpired();
        }
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    public int getFXLayer()
    {
        return 3;
    }
}
