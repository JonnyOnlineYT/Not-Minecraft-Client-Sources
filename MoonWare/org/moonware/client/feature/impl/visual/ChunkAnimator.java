package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRenderChunk;
import org.moonware.client.event.events.impl.render.EventRenderChunkContainer;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ChunkAnimator extends Feature {

    private final HashMap<RenderChunk, AtomicLong> renderChunkMap = new HashMap<>();

    public ChunkAnimator() {
        super("ChunkAnimator", "Анимирует твои чанки", Type.Visuals);
    }

    private double easeOutCubic(double t) {
        return (--t) * t * t + 1;
    }

    @EventTarget
    private void onRenderChunk(EventRenderChunk event) {
        if (Minecraft.player != null) {
            if (!renderChunkMap.containsKey(event.getRenderChunk())) {
                renderChunkMap.put(event.getRenderChunk(), new AtomicLong(-1L));
            }
        }
    }

    @EventTarget
    private void onChunkRender(EventRenderChunkContainer event) {
        if (renderChunkMap.containsKey(event.getRenderChunk())) {
            AtomicLong timeAlive = renderChunkMap.get(event.getRenderChunk());
            long timeClone = timeAlive.get();
            if (timeClone == -1L) {
                timeClone = System.currentTimeMillis();
                timeAlive.set(timeClone);
            }

            long timeDifference = System.currentTimeMillis() - timeClone;
            if (timeDifference <= 250) {
                double chunkY = event.getRenderChunk().getPosition().getY();
                double offsetY;
                offsetY = chunkY * easeOutCubic(timeDifference / 250F);
                GlStateManager.translate(0.0, -chunkY + offsetY, 0.0);
            }
        }
    }

}
