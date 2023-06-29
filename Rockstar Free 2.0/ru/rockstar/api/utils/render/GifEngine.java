// 
// Decompiled by Procyon v0.5.36
// 

package ru.rockstar.api.utils.render;

import java.awt.image.BufferedImage;
import javax.imageio.stream.ImageInputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class GifEngine
{
    private int current;
    private Counter counter;
    private DynamicTexture texture;
    private List<DynamicTexture> frames;
    private int x;
    private int y;
    private int width;
    private int height;
    
    public GifEngine(final ResourceLocation rs, final int width, final int height) {
        this.current = 0;
        this.counter = new Counter();
        this.texture = new DynamicTexture(width, height);
        this.width = width;
        this.height = height;
        this.frames = this.images(rs);
    }
    
    public void update() {
        if (this.counter.hasReached(Minecraft.getMinecraft().timer.renderPartialTicks) && this.frames.size() > 0) {
            if (this.current > this.frames.size() - 1) {
                this.current = 0;
            }
            this.texture = this.frames.get(this.current);
            ++this.current;
            this.counter.reset();
        }
    }
    
    public void bind(final int x, final int y) {
        this.x = x;
        this.y = y;
        UIRender.bind(this.x + 5.0f, (float)this.y, (float)(this.width), (float)(this.height), 1.0f, this.getTexture().getGlTextureId());
    }
    
    public List<DynamicTexture> images(final ResourceLocation rs) {
        final List<DynamicTexture> images = new ArrayList<DynamicTexture>();
        try {
            final ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            final InputStream xui = Minecraft.getMinecraft().getResourceManager().getResource(rs).getInputStream();
            final ImageInputStream stream = ImageIO.createImageInputStream(xui);
            reader.setInput(stream);
            for (int count = reader.getNumImages(true), index = 0; index < count; ++index) {
                final BufferedImage frame = reader.read(index);
                images.add(new DynamicTexture(frame));
            }
        }
        catch (IOException ex) {}
        return images;
    }
    
    public DynamicTexture getTexture() {
        return this.texture;
    }
    
    public void setTexture(final DynamicTexture texture) {
        this.texture = texture;
    }
    
    public List<DynamicTexture> getFrames() {
        return this.frames;
    }
    
    public void setFrames(final List<DynamicTexture> frames) {
        this.frames = frames;
    }
}
