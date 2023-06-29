package ru.wendoxd.models.overrides;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import ru.wendoxd.models.ModelObject;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.visuals.SwingAnimations;

public class OverrideModelCallback {
	public static ResourceLocation swordTexture = new ResourceLocation("wexside/ice.png"),
			jokerTexture = new ResourceLocation("wexside/joker.png"),
			shieldTexture = new ResourceLocation("wexside/shield.png");
	public static ModelObject iceSword, shield, demon;

	public static boolean defaultRender(ItemStack stack, ItemCameraTransforms.TransformType transform) {
		if (!Module.modelChanger.isEnabled(false)) {
			return false;
		}
		if (stack.getItem() instanceof ItemSword && Module.change_sword.get() == 1) {
			GL11.glPushMatrix();
			if (transform == TransformType.THIRD_PERSON_LEFT_HAND
					|| transform == TransformType.THIRD_PERSON_RIGHT_HAND) {
				GL11.glTranslated(0.075, -0.25, -0);
				GL11.glRotated(-90, 0, 1, 0);
				GL11.glRotated(20, 1, 0, 0);
				GL11.glRotated(180, 0, 0, 1);
				GL11.glScaled(0.1, 0.1, 0.1);
			} else {
				GL11.glRotated(90, 1, 0, 0);
				GL11.glRotated(-120, 0, 0, 1);
				GL11.glRotated(-5, 0, 1, 0);
				GL11.glScaled(0.1, 0.1, 0.1);
				if (SwingAnimations.animations.get() == 1) {
					GL11.glRotated(-180, 1, 0, 0);
				}
			}
			Minecraft.getMinecraft().getTextureManager().bindTexture(swordTexture);
			GL11.glCallList(iceSword.lists[0]);
			GL11.glPopMatrix();
			return true;
		}
		if (stack.getItem() instanceof ItemShield && Module.change_shield.get() == 1) {
			GL11.glPushMatrix();
			if (transform == TransformType.THIRD_PERSON_LEFT_HAND
					|| transform == TransformType.THIRD_PERSON_RIGHT_HAND) {
				GL11.glTranslated(-0.4, -0.5, -0.55);
				GL11.glRotated(180, 1, 0, 0);
				GL11.glRotated(180, 0, 0, 1);
				GL11.glRotated(-5, 0, 1, 0);
				GL11.glScaled(0.01, 0.01, 0.01);
			} else {
				GL11.glTranslated(-0.4, -0.2, -0.2);
				GL11.glRotated(180, 0, 1, 0);
				GL11.glScaled(0.007, 0.007, 0.007);
			}
			Minecraft.getMinecraft().getTextureManager().bindTexture(shieldTexture);
			GL11.glCallList(shield.lists[0]);
			GL11.glPopMatrix();
			return true;
		}
		if (stack.getItem() == Items.field_190929_cY && Module.change_totem.get() == 1) {
			GL11.glPushMatrix();
			if (transform == TransformType.THIRD_PERSON_LEFT_HAND
					|| transform == TransformType.THIRD_PERSON_RIGHT_HAND) {
				GL11.glTranslated(0, -0.5, -0.01);
				GL11.glRotated(180, 0, 1, 0);
				GL11.glScaled(0.001, 0.001, 0.001);
			} else {
				if (transform == TransformType.FIRST_PERSON_LEFT_HAND
						|| transform == TransformType.FIRST_PERSON_RIGHT_HAND) {
					GL11.glRotated(transform == TransformType.FIRST_PERSON_RIGHT_HAND ? -125 : 125, 0, 1, 0);
					GL11.glTranslated(0, -0.3, 0);
				} else {
					GL11.glTranslated(0, -0.3, 0);
					GL11.glRotated(180, 0, 1, 0);
				}
				GL11.glScaled(0.00075, 0.00075, 0.00075);
			}
			Minecraft.getMinecraft().getTextureManager().bindTexture(jokerTexture);
			GL11.glCallList(demon.lists[0]);
			GL11.glPopMatrix();
			return true;
		}
		return false;
	}

	public static void init() throws Exception {
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Random rand = new Random();
			rand.setSeed(-1244405769899267607L);
			FileInputStream fis = new FileInputStream("00");
			int r;
			while ((r = fis.read()) >= 0) {
				baos.write(r ^ rand.nextInt());
			}
			fis.close();
			iceSword = new ModelObject(new ByteArrayInputStream(baos.toByteArray()));
		}
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Random rand = new Random();
			rand.setSeed(7047923623408553808L);
			FileInputStream fis = new FileInputStream("11");
			int r;
			while ((r = fis.read()) >= 0) {
				baos.write(r ^ rand.nextInt());
			}
			fis.close();
			demon = new ModelObject(new ByteArrayInputStream(baos.toByteArray()));
		}
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Random rand = new Random();
			FileInputStream fis = new FileInputStream("22");
			int r;
			while ((r = fis.read()) >= 0) {
				baos.write(r ^ 444);
			}
			fis.close();
			shield = new ModelObject(new ByteArrayInputStream(baos.toByteArray()));
		}
	}
}
