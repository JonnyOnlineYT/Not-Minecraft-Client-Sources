/*
 * Decompiled with CFR 0.150.
 */
package wavecapes.renderlayers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.Namespaced;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.CustomModel;

public class CustomCapeRenderLayer
implements LayerRenderer<AbstractClientPlayer> {
    private ModelRenderer[] customCape = new ModelRenderer[16];
    private final RenderPlayer playerRenderer;
    private SmoothCapeRenderer smoothCapeRenderer = new SmoothCapeRenderer();

    public CustomCapeRenderLayer(RenderPlayer playerRenderer, ModelBase model) {
        this.playerRenderer = playerRenderer;
        buildMesh(model);
    }

    private void buildMesh(ModelBase model) {
        customCape = new ModelRenderer[16];
        for (int i = 0; i < 16; ++i) {
            ModelRenderer base = new ModelRenderer(model, 0, i);
            base.setTextureSize(64, 32);
            customCape[i] = base.addBox(-5.0f, i, -1.0f, 10, 1, 1);
        }
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float paramFloat1, float paramFloat2, float deltaTick, float animationTick, float paramFloat5, float paramFloat6, float paramFloat7) {
        if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState()) {
            return;
        }
        if (abstractClientPlayer.isInvisible()) {
            return;
        }
        if (!abstractClientPlayer.hasPlayerInfo() || abstractClientPlayer.isInvisible() || !abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE)) {
            return;
        }
        AbstractClientPlayer holder = abstractClientPlayer;
        holder.updateSimulation(abstractClientPlayer, 16);
        playerRenderer.bindTexture(new Namespaced("moonware/neverhookcape3.png"));
        smoothCapeRenderer.renderSmoothCape(this, abstractClientPlayer, deltaTick);
    }

    float getNatrualWindSwing(int part) {
        long highlightedPart = System.currentTimeMillis() / 3L % 360L;
        float relativePart = (float)(part + 1) / 16.0f;
        return (float)(Math.sin(Math.toRadians(relativePart * 360.0f - (float)highlightedPart)) * 3.0);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

