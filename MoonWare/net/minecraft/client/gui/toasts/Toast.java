package net.minecraft.client.gui.toasts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;

public interface Toast {
    Namespaced TOASTS = new Namespaced("textures/gui/toasts.png");
    Object field_193655_b = new Object();

    Toast.Visibility draw(long delta);

    default Object getType() {
        return field_193655_b;
    }

    @RequiredArgsConstructor
    enum Visibility {
        SHOW(SoundEvents.UI_TOAST_IN),
        HIDE(SoundEvents.UI_TOAST_OUT);
        @Getter private final SoundEvent sound;

        public void playSound(SoundHandler handler) {
            handler.playSound(PositionedSoundRecord.func_194007_a(sound, 1F, 1F));
        }
    }
}
