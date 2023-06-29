package net.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.event.ClickEvent;
import optifine.Config;
import org.lwjgl.BufferUtils;
import org.moonware.client.utils.MWUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Screenshot {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    public static void saveScreenshot(Minecraft minecraft) {
        saveScreenshot(minecraft, Minecraft.width, Minecraft.height, Minecraft.getFramebuffer())
                .whenComplete((image, ex) -> {
            MWUtils.ASYNC.execute(() -> {
                if (image != null) {
                    try {
                        File dir = new File(Minecraft.gameDir, "screenshots");
                        dir.mkdirs();
                        File file = null;
                        String s = DATE_FORMAT.format(new Date());
                        for (int i = 1; file == null || file.exists(); i++) {
                            file = new File(dir, s + (i == 1 ? "" : "_" + i) + ".png");
                            if (!file.exists()) break;
                            ++i;
                        }
                        ImageIO.write(image, "png", file);
                        Component component = new TextComponent(file.getName());
                        component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath()));
                        component.getStyle().setUnderlined(true);
                        Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("screenshot.success", component));
                    } catch (Exception e) {
                        System.err.println("Unable to save screenshot.");
                        e.printStackTrace();
                        Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("screenshot.failure", e.getMessage()));
                    }
                    return;
                }
                if (ex != null) {
                    System.err.println("Unable to make screenshot.");
                    ex.printStackTrace();
                    Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("screenshot.failure", ex.getMessage()));
                    return;
                }
                throw new IllegalStateException("Exception and screenshot are both false.");
            });
        });

    }

    public static CompletableFuture<BufferedImage> saveScreenshot(Minecraft minecraft, int width, int height, Framebuffer buffer) {
        CompletableFuture<BufferedImage> future = new CompletableFuture<>();
        try {
            int i = Config.getGameSettings().scale;
            ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            int j = scaledresolution.getScaleFactor();
            int k = Config.getScreenshotSize();
            boolean flag = OpenGlHelper.isFramebufferEnabled() && k > 1;
            if (flag) {
                Config.getGameSettings().scale = j * k;
                resize(width * k, height * k);
                GlStateManager.pushMatrix();
                GlStateManager.clear(16640);
                Minecraft.getFramebuffer().bindFramebuffer(true);
                Minecraft.gameRenderer.updateCameraAndRender(minecraft.getRenderPartialTicks(), System.nanoTime());
            }
            BufferedImage image = createScreenshot(width, height, buffer);
            if (flag) {
                Minecraft.getFramebuffer().unbindFramebuffer();
                GlStateManager.popMatrix();
                Config.getGameSettings().scale = i;
                resize(width, height);
            }
            future.complete(image);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public static BufferedImage createScreenshot(int width, int height, Framebuffer framebufferIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = framebufferIn.framebufferTextureWidth;
            height = framebufferIn.framebufferTextureHeight;
        }

        int i = width * height;

        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }

        GlStateManager.glPixelStorei(3333, 1);
        GlStateManager.glPixelStorei(3317, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebufferIn.framebufferTexture);
            GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        } else {
            GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }

        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, width, height);
        BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
        return bufferedimage;
    }

    private static void resize(int p_resize_0_, int p_resize_1_) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Minecraft.width = Math.max(1, p_resize_0_);
        Minecraft.height = Math.max(1, p_resize_1_);

        if (Minecraft.screen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            Minecraft.screen.init(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }

        updateFramebufferSize();
    }

    private static void updateFramebufferSize() {
        Minecraft minecraft = Minecraft.getMinecraft();
        Minecraft.getFramebuffer().createBindFramebuffer(Minecraft.width, Minecraft.height);

        if (Minecraft.gameRenderer != null) {
            Minecraft.gameRenderer.updateShaderGroupSize(Minecraft.width, Minecraft.height);
        }
    }
}
