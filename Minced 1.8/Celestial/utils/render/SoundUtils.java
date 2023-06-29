/*
 * Decompiled with CFR 0.150.
 */
package Celestial.utils.render;

import Celestial.utils.Helper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundUtils
        implements Helper {
    public static synchronized void playSound(String url, float volume, boolean stop) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                InputStream audioSrc = SoundUtils.class.getResourceAsStream("/assets/minecraft/minced/sounds/" + url);
                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
                if (stop) {
                    clip.stop();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }).start();
    }
}

