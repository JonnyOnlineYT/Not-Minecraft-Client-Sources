package ru.rockstar.api.utils.other;
import net.minecraft.client.audio.Sound;
import ru.rockstar.Main;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundHelper {

	public static void playSound(final String url) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/assets/minecraft/rockstar/songs/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {

            }
        }).start();
    }
	
	public static synchronized void playSound(final String url, final float volume, final boolean stop) {
        new Thread(() -> {
            try {
            	Clip clip;
                InputStream audioSrc;
                InputStream bufferedIn;
                AudioInputStream inputStream;
                FloatControl gainControl;
            	clip = AudioSystem.getClip();
                audioSrc = SoundHelper.class.getResourceAsStream("/assets/minecraft/rockstar/songs/" + url);
                bufferedIn = new BufferedInputStream(audioSrc);
                inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
                if (stop) {
                    clip.stop();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
