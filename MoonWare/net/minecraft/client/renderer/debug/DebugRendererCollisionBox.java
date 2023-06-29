package net.minecraft.client.renderer.debug;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer
{
    private final Minecraft field_191312_a;
    private EntityPlayer field_191313_b;
    private double field_191314_c;
    private double field_191315_d;
    private double field_191316_e;

    public DebugRendererCollisionBox(Minecraft p_i47215_1_)
    {
        field_191312_a = p_i47215_1_;
    }

    public void render(float p_190060_1_, long p_190060_2_)
    {
        field_191313_b = Minecraft.player;
        field_191314_c = field_191313_b.lastTickPosX + (field_191313_b.posX - field_191313_b.lastTickPosX) * (double)p_190060_1_;
        field_191315_d = field_191313_b.lastTickPosY + (field_191313_b.posY - field_191313_b.lastTickPosY) * (double)p_190060_1_;
        field_191316_e = field_191313_b.lastTickPosZ + (field_191313_b.posZ - field_191313_b.lastTickPosZ) * (double)p_190060_1_;
        World world = Minecraft.player.world;
        List<AxisAlignedBB> list = world.getCollisionBoxes(field_191313_b, field_191313_b.getEntityBoundingBox().expandXyz(6.0D));
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        for (AxisAlignedBB axisalignedbb : list)
        {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.expandXyz(0.002D).offset(-field_191314_c, -field_191315_d, -field_191316_e), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
