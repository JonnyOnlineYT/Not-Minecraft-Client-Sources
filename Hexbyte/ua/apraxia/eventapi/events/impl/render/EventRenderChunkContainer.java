package ua.apraxia.eventapi.events.impl.render;

import ua.apraxia.eventapi.events.callables.EventCancellable;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class EventRenderChunkContainer extends EventCancellable {

    public RenderChunk renderChunk;

    public EventRenderChunkContainer(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }

    public RenderChunk getRenderChunk() {
        return renderChunk;
    }

    public void setRenderChunk(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }
}
