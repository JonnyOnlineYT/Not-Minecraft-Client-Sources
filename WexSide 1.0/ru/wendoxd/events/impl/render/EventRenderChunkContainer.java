package ru.wendoxd.events.impl.render;

import net.minecraft.client.renderer.chunk.RenderChunk;
import ru.wendoxd.events.Event;

public class EventRenderChunkContainer extends Event {

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