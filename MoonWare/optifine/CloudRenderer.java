package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class CloudRenderer
{
    private Minecraft mc;
    private boolean updated;
    private boolean renderFancy;
    int cloudTickCounter;
    private Vec3d cloudColor;
    float partialTicks;
    private boolean updateRenderFancy;
    private int updateCloudTickCounter;
    private Vec3d updateCloudColor = new Vec3d(-1.0D, -1.0D, -1.0D);
    private double updatePlayerX;
    private double updatePlayerY;
    private double updatePlayerZ;
    private int glListClouds = -1;

    public CloudRenderer(Minecraft p_i23_1_)
    {
        mc = p_i23_1_;
        glListClouds = GLAllocation.generateDisplayLists(1);
    }

    public void prepareToRender(boolean p_prepareToRender_1_, int p_prepareToRender_2_, float p_prepareToRender_3_, Vec3d p_prepareToRender_4_)
    {
        renderFancy = p_prepareToRender_1_;
        cloudTickCounter = p_prepareToRender_2_;
        partialTicks = p_prepareToRender_3_;
        cloudColor = p_prepareToRender_4_;
    }

    public boolean shouldUpdateGlList()
    {
        if (!updated)
        {
            return true;
        }
        else if (renderFancy != updateRenderFancy)
        {
            return true;
        }
        else if (cloudTickCounter >= updateCloudTickCounter + 20)
        {
            return true;
        }
        else if (Math.abs(cloudColor.xCoord - updateCloudColor.xCoord) > 0.003D)
        {
            return true;
        }
        else if (Math.abs(cloudColor.yCoord - updateCloudColor.yCoord) > 0.003D)
        {
            return true;
        }
        else if (Math.abs(cloudColor.zCoord - updateCloudColor.zCoord) > 0.003D)
        {
            return true;
        }
        else
        {
            Entity entity = Minecraft.getRenderViewEntity();
            boolean flag = updatePlayerY + (double)entity.getEyeHeight() < 128.0D + (double)(Minecraft.gameSettings.ofCloudsHeight * 128.0F);
            boolean flag1 = entity.prevPosY + (double)entity.getEyeHeight() < 128.0D + (double)(Minecraft.gameSettings.ofCloudsHeight * 128.0F);
            return flag1 != flag;
        }
    }

    public void startUpdateGlList()
    {
        GL11.glNewList(glListClouds, GL11.GL_COMPILE);
    }

    public void endUpdateGlList()
    {
        GL11.glEndList();
        updateRenderFancy = renderFancy;
        updateCloudTickCounter = cloudTickCounter;
        updateCloudColor = cloudColor;
        updatePlayerX = Minecraft.getRenderViewEntity().prevPosX;
        updatePlayerY = Minecraft.getRenderViewEntity().prevPosY;
        updatePlayerZ = Minecraft.getRenderViewEntity().prevPosZ;
        updated = true;
        GlStateManager.resetColor();
    }

    public void renderGlList()
    {
        Entity entity = Minecraft.getRenderViewEntity();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
        double d3 = (float)(cloudTickCounter - updateCloudTickCounter) + partialTicks;
        float f = (float)(d0 - updatePlayerX + d3 * 0.03D);
        float f1 = (float)(d1 - updatePlayerY);
        float f2 = (float)(d2 - updatePlayerZ);
        GlStateManager.pushMatrix();

        if (renderFancy)
        {
            GlStateManager.translate(-f / 12.0F, -f1, -f2 / 12.0F);
        }
        else
        {
            GlStateManager.translate(-f, -f1, -f2);
        }

        GlStateManager.callList(glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public void reset()
    {
        updated = false;
    }
}
