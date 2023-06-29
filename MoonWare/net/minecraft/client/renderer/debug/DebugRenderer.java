package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRenderer
{
    public final DebugRenderer.IDebugRenderer debugRendererPathfinding;
    public final DebugRenderer.IDebugRenderer debugRendererWater;
    public final DebugRenderer.IDebugRenderer debugRendererChunkBorder;
    public final DebugRenderer.IDebugRenderer debugRendererHeightMap;
    public final DebugRenderer.IDebugRenderer field_191325_e;
    public final DebugRenderer.IDebugRenderer field_191557_f;
    public final DebugRenderer.IDebugRenderer field_193852_g;
    private boolean chunkBordersEnabled;
    private boolean pathfindingEnabled;
    private boolean waterEnabled;
    private boolean heightmapEnabled;
    private boolean field_191326_j;
    private boolean field_191558_l;
    private boolean field_193853_n;

    public DebugRenderer(Minecraft clientIn)
    {
        debugRendererPathfinding = new DebugRendererPathfinding(clientIn);
        debugRendererWater = new DebugRendererWater(clientIn);
        debugRendererChunkBorder = new DebugRendererChunkBorder(clientIn);
        debugRendererHeightMap = new DebugRendererHeightMap(clientIn);
        field_191325_e = new DebugRendererCollisionBox(clientIn);
        field_191557_f = new DebugRendererNeighborsUpdate(clientIn);
        field_193852_g = new DebugRendererSolidFace(clientIn);
    }

    public boolean shouldRender()
    {
        return chunkBordersEnabled || pathfindingEnabled || waterEnabled || heightmapEnabled || field_191326_j || field_191558_l || field_193853_n;
    }

    /**
     * Toggles the debug screen's visibility.
     */
    public boolean toggleChunkBounds()
    {
        chunkBordersEnabled = !chunkBordersEnabled;
        return chunkBordersEnabled;
    }

    public void renderDebug(float partialTicks, long finishTimeNano)
    {
        if (pathfindingEnabled)
        {
            debugRendererPathfinding.render(partialTicks, finishTimeNano);
        }

        if (chunkBordersEnabled && !Minecraft.getMinecraft().isReducedDebug())
        {
            debugRendererChunkBorder.render(partialTicks, finishTimeNano);
        }

        if (waterEnabled)
        {
            debugRendererWater.render(partialTicks, finishTimeNano);
        }

        if (heightmapEnabled)
        {
            debugRendererHeightMap.render(partialTicks, finishTimeNano);
        }

        if (field_191326_j)
        {
            field_191325_e.render(partialTicks, finishTimeNano);
        }

        if (field_191558_l)
        {
            field_191557_f.render(partialTicks, finishTimeNano);
        }

        if (field_193853_n)
        {
            field_193852_g.render(partialTicks, finishTimeNano);
        }
    }

    public static void func_191556_a(String p_191556_0_, int p_191556_1_, int p_191556_2_, int p_191556_3_, float p_191556_4_, int p_191556_5_)
    {
        renderDebugText(p_191556_0_, (double)p_191556_1_ + 0.5D, (double)p_191556_2_ + 0.5D, (double)p_191556_3_ + 0.5D, p_191556_4_, p_191556_5_);
    }

    public static void renderDebugText(String str, double x, double y, double z, float partialTicks, int color)
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (Minecraft.player != null && Minecraft.getRenderManager() != null && Minecraft.getRenderManager().options != null)
        {
            Font fontrenderer = Minecraft.font;
            EntityPlayer entityplayer = Minecraft.player;
            double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks;
            double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks;
            double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(x - d0), (float)(y - d1) + 0.07F, (float)(z - d2));
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.02F, -0.02F, 0.02F);
            RenderManager rendermanager = Minecraft.getRenderManager();
            GlStateManager.rotate(-rendermanager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(rendermanager.options.thirdPersonView == 2 ? 1 : -1) * rendermanager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, color);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public interface IDebugRenderer
    {
        void render(float p_190060_1_, long p_190060_2_);
    }
}
