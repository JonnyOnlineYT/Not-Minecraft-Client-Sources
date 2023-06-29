package org.moonware.client.utils;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponent;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.ClientSounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.text.DecimalFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.lwjgl.opengl.GL11.*;

public class MWUtils {
    public static final Executor ASYNC = Executors.newSingleThreadExecutor(r -> new Thread(r, "MoonWare Thread"));
    public static final String PREFIX = "[§1M§5oon§1W§5are§r]";
    public static final Gson GSON = new Gson();
    public static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("#.#");
    public static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("#.##");
    public static boolean isHovered(double x, double y, double width, double height, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
    public static void scale(float x, float y, float scale, Runnable data) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1);
        glTranslatef(-x, -y, 0);
        data.run();
        glPopMatrix();
    }
    public static int randomIntInclusive(int min, int maxInclusive) {
        return Minecraft.RANDOM.nextInt((maxInclusive - min) + 1) + min;
    }

    public static int randomInt(int min, int max) {
        return Minecraft.RANDOM.nextInt(max - min) + min;
    }

    public static float randomFloat(float min, float max) {
        if (min > max) {
            float f = max;
            max = min;
            min = f;
        }
        return Minecraft.RANDOM.nextFloat() * (max - min) + min;
    }

    public static double randomDouble(double min, double max) {
        if (min > max) {
            double d = max;
            max = min;
            min = d;
        }
        return Minecraft.RANDOM.nextDouble() * (max - min) + min;
    }

    public static void sendChat(String message) {
        Minecraft.ingameGUI.getChatGUI().printChatMessage(new TextComponent(PREFIX + message));
    }

    public static void playSound(String url) {
        playSound(url, ClientSounds.value.getNumberValue());
    }

    public static void playSound(String url, float volume) {
        ASYNC.execute(() -> {
            try (Clip clip = AudioSystem.getClip(); AudioInputStream inputStream = AudioSystem.getAudioInputStream(MoonWare.class.getResourceAsStream("/assets/minecraft/moonware/sounds/" + url))) {
                clip.open(inputStream);
                ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
                clip.start();
            } catch (Exception ignored) {}
        });
    }
}
