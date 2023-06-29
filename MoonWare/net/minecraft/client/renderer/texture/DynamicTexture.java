package net.minecraft.client.renderer.texture;

import net.minecraft.client.resources.IResourceManager;
import optifine.Config;
import shadersmod.client.ShadersTex;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DynamicTexture extends AbstractTexture {
    private final int[] dynamicTextureData;
    private final int width;
    private final int height;
    private boolean shadersInitialized;

    public DynamicTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), dynamicTextureData, 0, bufferedImage.getWidth());
        updateDynamicTexture();
    }

    public DynamicTexture(int textureWidth, int textureHeight) {
        shadersInitialized = false;
        width = textureWidth;
        height = textureHeight;
        dynamicTextureData = new int[textureWidth * textureHeight * 3];
        if (Config.isShaders()) {
            ShadersTex.initDynamicTexture(getGlTextureId(), textureWidth, textureHeight, this);
            shadersInitialized = true;
        } else {
            TextureUtil.allocateTexture(getGlTextureId(), textureWidth, textureHeight);
        }
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
    }

    public void updateDynamicTexture() {
        if (Config.isShaders()) {
            if (!shadersInitialized) {
                ShadersTex.initDynamicTexture(getGlTextureId(), width, height, this);
                shadersInitialized = true;
            }
            ShadersTex.updateDynamicTexture(getGlTextureId(), dynamicTextureData, width, height, this);
        } else {
            TextureUtil.uploadTexture(getGlTextureId(), dynamicTextureData, width, height);
        }
    }

    public int[] getTextureData() {
        return dynamicTextureData;
    }
}
