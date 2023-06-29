/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.renderlayers;


import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.CustomModel;
import wavecapes.stim.StickSimulation;
import wavecapes.util.*;

public class SmoothCapeRenderer {
    public void renderSmoothCape(CustomCapeRenderLayer layer, AbstractClientPlayer abstractClientPlayer, float delta) {
        BufferBuilder worldrenderer = Tessellator.getInstance().getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Matrix4f oldPositionMatrix = null;
        for (int part = 0; part < 16; ++part) {
            modifyPoseStack(layer, poseStack, abstractClientPlayer, delta, part);
            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.last().pose();
            }
            if (part == 0) {
                addTopVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, 0.0f, 0.0f, -0.3f, 0.0f, -0.06f, part);
            }
            if (part == 15) {
                addBottomVertex(worldrenderer, poseStack.last().pose(), poseStack.last().pose(), 0.3f, (float)(part + 1) * 0.06f, 0.0f, -0.3f, (float)(part + 1) * 0.06f, -0.06f, part);
            }
            addLeftVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, -0.3f, (float)(part + 1) * 0.06f, 0.0f, -0.3f, (float)part * 0.06f, -0.06f, part);
            addRightVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, (float)(part + 1) * 0.06f, 0.0f, 0.3f, (float)part * 0.06f, -0.06f, part);
            addBackVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, (float)(part + 1) * 0.06f, -0.06f, -0.3f, (float)part * 0.06f, -0.06f, part);
            addFrontVertex(worldrenderer, oldPositionMatrix, poseStack.last().pose(), 0.3f, (float)(part + 1) * 0.06f, 0.0f, -0.3f, (float)part * 0.06f, 0.0f, part);
            oldPositionMatrix = poseStack.last().pose();
            poseStack.popPose();
        }
        Tessellator.getInstance().draw();
    }

    void modifyPoseStack(CustomCapeRenderLayer layer, PoseStack poseStack, AbstractClientPlayer abstractClientPlayer, float h, int part) {
        modifyPoseStackVanilla(layer, poseStack, abstractClientPlayer, h, part);
        modifyPoseStackVanillaCmodel(layer, poseStack, abstractClientPlayer, h, part);
    }

    private void modifyPoseStackVanilla(CustomCapeRenderLayer layer, PoseStack poseStack, AbstractClientPlayer abstractClientPlayer, float h, int part) {
        if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 0.125);
        double d = Mth.lerp(h, abstractClientPlayer.prevChasingPosX, abstractClientPlayer.chasingPosX) - Mth.lerp(h, abstractClientPlayer.prevPosX, abstractClientPlayer.posX);
        double e = Mth.lerp(h, abstractClientPlayer.prevChasingPosY, abstractClientPlayer.chasingPosY) - Mth.lerp(h, abstractClientPlayer.prevPosY, abstractClientPlayer.posY);
        double m = Mth.lerp(h, abstractClientPlayer.prevChasingPosZ, abstractClientPlayer.chasingPosZ) - Mth.lerp(h, abstractClientPlayer.prevPosZ, abstractClientPlayer.posZ);
        float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
        double o = Math.sin(n * ((float)Math.PI / 180));
        double p = -Math.cos(n * ((float)Math.PI / 180));
        float height = (float)e * 10.0f;
        height = MathHelper.clamp(height, -6.0f, 62.0f);
        float swing = (float)(d * o + m * p) * easeOutSine(0.0625f * (float)part) * 100.0f;
        swing = MathHelper.clamp(swing, 0.0f, 150.0f * easeOutSine(0.0625f * (float)part));
        float sidewaysRotationOffset = (float)(d * p - m * o) * 100.0f;
        sidewaysRotationOffset = MathHelper.clamp(sidewaysRotationOffset, -20.0f, 20.0f);
        float t = Mth.lerp(h, abstractClientPlayer.prevCameraYaw, abstractClientPlayer.cameraYaw);
        height = (float)((double)height + Math.sin(Mth.lerp(h, abstractClientPlayer.prevDistanceWalkedModified, abstractClientPlayer.distanceWalkedModified) * 6.0f) * 32.0 * (double)t);
        if (abstractClientPlayer.isSneaking()) {
            height += 25.0f;
            poseStack.translate(0.0, 0.15f, 0.0);
        }
        float naturalWindSwing = layer.getNatrualWindSwing(part);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(6.0f + swing / 2.0f + height + naturalWindSwing));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(sidewaysRotationOffset / 2.0f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f - sidewaysRotationOffset / 2.0f));
    }

    private void modifyPoseStackVanillaCmodel(CustomCapeRenderLayer layer, PoseStack poseStack, AbstractClientPlayer abstractClientPlayer, float h, int part) {
        if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState()) {
            return;
        }
    }

    private static void addBackVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
            Matrix4f k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }
        float minU = 0.015625f;
        float maxU = 0.171875f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, minV += vPerPart * (float)part).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addFrontVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
            Matrix4f k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }
        float minU = 0.1875f;
        float maxU = 0.34375f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, oldMatrix, x1, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y1, z1).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y2, z2).tex(minU, minV += vPerPart * (float)part).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y2, z2).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addLeftVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float minU = 0.0f;
        float maxU = 0.015625f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV += vPerPart * (float)part).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addRightVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float minU = 0.171875f;
        float maxU = 0.1875f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV += vPerPart * (float)part).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addBottomVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float minU = 0.171875f;
        float maxU = 0.328125f;
        float minV = 0.0f;
        float maxV = 0.03125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, oldMatrix, x1, y2, z2).tex(maxU, minV += vPerPart * (float)part).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static BufferBuilder vertex(BufferBuilder worldrenderer, Matrix4f matrix4f, float f, float g, float h) {
        Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
        vector4f.transform(matrix4f);
        worldrenderer.pos(vector4f.x(), vector4f.y(), vector4f.z());
        return worldrenderer;
    }

    private static void addTopVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float minU = 0.015625f;
        float maxU = 0.171875f;
        float minV = 0.0f;
        float maxV = 0.03125f;
        float deltaV = maxV - minV;
        float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (float)(part + 1);
        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, maxV).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, maxV).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, minV += vPerPart * (float)part).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, minV).normal(0.0f, 1.0f, 0.0f).endVertex();
    }

    private static float easeOutSine(float x) {
        return (float)Math.sin((double)x * Math.PI / 2.0);
    }
}

