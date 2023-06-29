package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private final Map<Integer, Path> pathMap = Maps.newHashMap();
    private final Map<Integer, Float> pathMaxDistance = Maps.newHashMap();
    private final Map<Integer, Long> creationMap = Maps.newHashMap();
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;

    public DebugRendererPathfinding(Minecraft minecraftIn)
    {
        minecraft = minecraftIn;
    }

    public void addPath(int p_188289_1_, Path p_188289_2_, float p_188289_3_)
    {
        pathMap.put(Integer.valueOf(p_188289_1_), p_188289_2_);
        creationMap.put(Integer.valueOf(p_188289_1_), Long.valueOf(System.currentTimeMillis()));
        pathMaxDistance.put(Integer.valueOf(p_188289_1_), Float.valueOf(p_188289_3_));
    }

    public void render(float p_190060_1_, long p_190060_2_)
    {
        if (!pathMap.isEmpty())
        {
            long i = System.currentTimeMillis();
            player = Minecraft.player;
            xo = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)p_190060_1_;
            yo = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)p_190060_1_;
            zo = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)p_190060_1_;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0F, 1.0F, 0.0F, 0.75F);
            GlStateManager.disableTexture2D();
            GlStateManager.glLineWidth(6.0F);

            for (Integer integer : pathMap.keySet())
            {
                Path path = pathMap.get(integer);
                float f = pathMaxDistance.get(integer).floatValue();
                renderPathLine(p_190060_1_, path);
                PathPoint pathpoint = path.getTarget();

                if (addDistanceToPlayer(pathpoint) <= 40.0F)
                {
                    RenderGlobal.renderFilledBox((new AxisAlignedBB((float)pathpoint.xCoord + 0.25F, (float)pathpoint.yCoord + 0.25F, (double)pathpoint.zCoord + 0.25D, (float)pathpoint.xCoord + 0.75F, (float)pathpoint.yCoord + 0.75F, (float)pathpoint.zCoord + 0.75F)).offset(-xo, -yo, -zo), 0.0F, 1.0F, 0.0F, 0.5F);

                    for (int j = 0; j < path.getCurrentPathLength(); ++j)
                    {
                        PathPoint pathpoint1 = path.getPathPointFromIndex(j);

                        if (addDistanceToPlayer(pathpoint1) <= 40.0F)
                        {
                            float f1 = j == path.getCurrentPathIndex() ? 1.0F : 0.0F;
                            float f2 = j == path.getCurrentPathIndex() ? 0.0F : 1.0F;
                            RenderGlobal.renderFilledBox((new AxisAlignedBB((float)pathpoint1.xCoord + 0.5F - f, (float)pathpoint1.yCoord + 0.01F * (float)j, (float)pathpoint1.zCoord + 0.5F - f, (float)pathpoint1.xCoord + 0.5F + f, (float)pathpoint1.yCoord + 0.25F + 0.01F * (float)j, (float)pathpoint1.zCoord + 0.5F + f)).offset(-xo, -yo, -zo), f1, 0.0F, f2, 0.5F);
                        }
                    }
                }
            }

            for (Integer integer1 : pathMap.keySet())
            {
                Path path1 = pathMap.get(integer1);

                for (PathPoint pathpoint3 : path1.getClosedSet())
                {
                    if (addDistanceToPlayer(pathpoint3) <= 40.0F)
                    {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint3.nodeType), (double)pathpoint3.xCoord + 0.5D, (double)pathpoint3.yCoord + 0.75D, (double)pathpoint3.zCoord + 0.5D, p_190060_1_, -65536);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint3.costMalus), (double)pathpoint3.xCoord + 0.5D, (double)pathpoint3.yCoord + 0.25D, (double)pathpoint3.zCoord + 0.5D, p_190060_1_, -65536);
                    }
                }

                for (PathPoint pathpoint4 : path1.getOpenSet())
                {
                    if (addDistanceToPlayer(pathpoint4) <= 40.0F)
                    {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint4.nodeType), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.75D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint4.costMalus), (double)pathpoint4.xCoord + 0.5D, (double)pathpoint4.yCoord + 0.25D, (double)pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
                    }
                }

                for (int k = 0; k < path1.getCurrentPathLength(); ++k)
                {
                    PathPoint pathpoint2 = path1.getPathPointFromIndex(k);

                    if (addDistanceToPlayer(pathpoint2) <= 40.0F)
                    {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint2.nodeType), (double)pathpoint2.xCoord + 0.5D, (double)pathpoint2.yCoord + 0.75D, (double)pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint2.costMalus), (double)pathpoint2.xCoord + 0.5D, (double)pathpoint2.yCoord + 0.25D, (double)pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
                    }
                }
            }

            for (Integer integer2 : creationMap.keySet().toArray(new Integer[0]))
            {
                if (i - creationMap.get(integer2).longValue() > 20000L)
                {
                    pathMap.remove(integer2);
                    creationMap.remove(integer2);
                }
            }

            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public void renderPathLine(float p_190067_1_, Path p_190067_2_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < p_190067_2_.getCurrentPathLength(); ++i)
        {
            PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);

            if (addDistanceToPlayer(pathpoint) <= 40.0F)
            {
                float f = (float)i / (float)p_190067_2_.getCurrentPathLength() * 0.33F;
                int j = i == 0 ? 0 : MathHelper.hsvToRGB(f, 0.9F, 0.9F);
                int k = j >> 16 & 255;
                int l = j >> 8 & 255;
                int i1 = j & 255;
                bufferbuilder.pos((double)pathpoint.xCoord - xo + 0.5D, (double)pathpoint.yCoord - yo + 0.5D, (double)pathpoint.zCoord - zo + 0.5D).color(k, l, i1, 255).endVertex();
            }
        }

        tessellator.draw();
    }

    private float addDistanceToPlayer(PathPoint p_190066_1_)
    {
        return (float)(Math.abs((double)p_190066_1_.xCoord - player.posX) + Math.abs((double)p_190066_1_.yCoord - player.posY) + Math.abs((double)p_190066_1_.zCoord - player.posZ));
    }
}
