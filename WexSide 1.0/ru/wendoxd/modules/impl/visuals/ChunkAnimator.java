package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRenderChunk;
import ru.wendoxd.events.impl.render.EventRenderChunkContainer;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ChunkAnimator extends Module {

	private Frame chunkAnimator_frame = new Frame("ChunkAnimator");
	private final CheckBox chunkAnimator = new CheckBox("ChunkAnimator").markArrayList("ChunkAnimator");
	public static HashMap<RenderChunk, AtomicLong> renderChunk = new HashMap<>();

	@Override
	protected void initSettings() {
		chunkAnimator_frame.register(chunkAnimator.markSetting("ChunkAnimator"));
		MenuAPI.visuals.register(chunkAnimator_frame);
	}

	private double easeOutCubic(double t) {
		return (--t) * t * t + 1;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRenderChunk && chunkAnimator.isEnabled(false)) {
			if (mc.player != null) {
				if (!renderChunk.containsKey(((EventRenderChunk) event).getRenderChunk())) {
					renderChunk.put(((EventRenderChunk) event).getRenderChunk(), new AtomicLong(-1L));
				}
			}
		}
		if (event instanceof EventRenderChunkContainer && chunkAnimator.isEnabled(false)) {
			if (renderChunk.containsKey(((EventRenderChunkContainer) event).getRenderChunk())) {
				AtomicLong atomic = renderChunk.get(((EventRenderChunkContainer) event).getRenderChunk());
				long delta = atomic.get();
				if (delta == -1L) {
					delta = System.currentTimeMillis();
					atomic.set(delta);
				}

				long timeDifference = System.currentTimeMillis() - delta;
				if (timeDifference <= 250) {
					double chunkY = ((EventRenderChunkContainer) event).getRenderChunk().getPosition().getY();
					double offsetY;
					offsetY = chunkY * easeOutCubic(timeDifference / 250F);
					GlStateManager.translate(0, -chunkY + offsetY, 0);
				}
			}
		}
	}
}
