package Celestial.utils.otherutils.pon;

import Celestial.event.events.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class EventRenderChunkContainer
        implements Event {
    public net.minecraft.client.renderer.chunk.RenderChunk RenderChunk;

    public EventRenderChunkContainer(RenderChunk renderChunk) {
        this.RenderChunk = renderChunk;
    }
}

