package ru.wendoxd.utils.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundUtils {
	public static float DEFAULT_PITCH = 1.7f;

	public static void playSound(String sound, float volume) {
		new Thread(() -> {
			try {
				AudioInputStream ais = AudioSystem
						.getAudioInputStream(new File(Minecraft.getMinecraft().mcDataDir + "/" + sound));
				Clip clip = AudioSystem.getClip();
				clip.open(ais);
				FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				vc.setValue(volume);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void playSound() {
		playSound(DEFAULT_PITCH);
	}

	public static void playSound(float pitch, float volume) {
		if (Minecraft.getMinecraft().player == null)
			return;
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(new PositionedSoundRecord(new ResourceLocation("block.note.harp"), SoundCategory.RECORDS,
						volume, pitch, false, 0, ISound.AttenuationType.LINEAR,
						(float) Minecraft.getMinecraft().player.posX, (float) Minecraft.getMinecraft().player.posY,
						(float) Minecraft.getMinecraft().player.posZ));
	}

	public static void playSound(float pitch) {
		playSound(pitch, 3);
	}
}
