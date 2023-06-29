package net.minecraft.client.gui.toasts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class ToastHud extends Gui {
    private static final ToastDraw<?>[] display = new ToastDraw[5];
    private static final Deque<Toast> queue = new ArrayDeque<>();

    public static void draw() {
        if (Minecraft.gameSettings.hideGUI) return;
        RenderHelper.disableStandardItemLighting();
        for (int i = 0; i < display.length; ++i) {
            ToastDraw<?> draw = display[i];
            if (draw != null && draw.draw(i)) display[i] = null;
            if (display[i] == null && !queue.isEmpty()) display[i] = new ToastDraw<>(queue.removeFirst());
        }
    }

    @Nullable
    public static <T extends Toast> T getToast(Class<? extends T> type, Object p_192990_2_) {
        for (ToastDraw<?> draw : display) {
            if (draw == null || !type.isAssignableFrom(draw.getToast().getClass()) ||
                    !draw.getToast().getType().equals(p_192990_2_)) continue;
            return (T) draw.getToast();
        }
        for (Toast toast : queue) {
            if (!type.isAssignableFrom(toast.getClass()) || !toast.getType().equals(p_192990_2_)) continue;
            return (T) toast;
        }

        return null;
    }

    public static void cleanup() {
        Arrays.fill(display, null);
        queue.clear();
    }

    public static void queue(Toast toast) {
        queue.add(toast);
    }

    @RequiredArgsConstructor
    private static class ToastDraw<T extends Toast> {
        @Getter private final T toast;
        private long field_193689_c = -1;
        private long field_193690_d = -1;
        private Toast.Visibility visibility = Toast.Visibility.SHOW;

        private float func_193686_a(long p_193686_1_) {
            float f = MathHelper.clamp((float) (p_193686_1_ - field_193689_c) / 600.0F, 0.0F, 1.0F);
            f = f * f * f * f;
            return visibility == Toast.Visibility.HIDE ? 1.0F - f : f;
        }

        public boolean draw(int index) {
            long i = Minecraft.getSystemTime();
            if (field_193689_c == -1L) {
                field_193689_c = i;
                visibility.playSound(Minecraft.getSoundHandler());
            }
            if (visibility == Toast.Visibility.SHOW && i - field_193689_c <= 600L) {
                field_193690_d = i;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(Minecraft.getScaledWidth() - 160F * func_193686_a(i), index * 32, 500 + index);
            Toast.Visibility newVisibility = toast.draw(i - field_193690_d);
            GlStateManager.popMatrix();
            if (newVisibility != visibility) {
                field_193689_c = i - (long) ((int) ((1.0F - func_193686_a(i)) * 600.0F));
                visibility = newVisibility;
                visibility.playSound(Minecraft.getSoundHandler());
            }
            return visibility == Toast.Visibility.HIDE && i - field_193689_c > 600L;
        }
    }
}
