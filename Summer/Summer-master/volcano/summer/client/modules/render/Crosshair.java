package volcano.summer.client.modules.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class Crosshair extends Module {

	public static Value<Double> sGap;
	public static Value<Double> width;
	public static Value<Double> length;
	public static Value<Double> dynamicgap;
	public static Value<Boolean> dynamic;
	public static Value<Double> red;
	public static Value<Double> green;
	public static Value<Double> blue;

	public static Value<Boolean> rainbow;

	public Crosshair() {
		super("Crosshair", 0, Category.RENDER);
		dynamic = new Value<Boolean>("Dynamic", "dynamic", false, this);
		dynamicgap = new ClampedValue<Double>("DynamicGap", "dynamicgap", 3.0, 0.5, 10.0, this);
		length = new ClampedValue<Double>("Length", "length", 20.0, 0.5, 50.0, this);
		sGap = new ClampedValue<Double>("Gap", "gap", 4.0, 0.5, 10.0, this);
		width = new ClampedValue<Double>("Width", "width", 2.0, 0.5, 10.0, this);
		red = new ClampedValue<Double>("Red", "red", 255.0, 1.0, 255.0, this);
		blue = new ClampedValue<Double>("Blue", "blue", 0.0, 1.0, 255.0, this);
		green = new ClampedValue<Double>("Green", "green", 255.0, 1.0, 255.0, this);
		rainbow = new Value<Boolean>("Rainbow", "rainbow", false, this);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			if (rainbow.getValue()) {
				int color = getRainbow(6000, -15);
				int screenWidth = ((EventRender2D) event).getWidth();
				int screenHeight = ((EventRender2D) event).getHeight();
				int wMiddle = screenWidth / 2;
				int hMiddle = screenHeight / 2;
				boolean wider = dynamic.getValue() && mc.thePlayer.isMoving();
				double gap = wider ? dynamicgap.getValue() : sGap.getValue();
				// Left
				R2DUtils.drawBorderedRect(wMiddle - gap - length.getValue(), hMiddle - (width.getValue() / 2.0),
						wMiddle - gap, hMiddle + (width.getValue() / 2.0), 0.5f, Color.black.getRGB(), color);
				// Right
				R2DUtils.drawBorderedRect(wMiddle + gap, hMiddle - (width.getValue() / 2.0),
						wMiddle + gap + length.getValue(), hMiddle + (width.getValue() / 2.0), 0.5f,
						Color.black.getRGB(), color);
				// Top
				R2DUtils.drawBorderedRect(wMiddle - (width.getValue() / 2.0), hMiddle - gap - length.getValue(),
						wMiddle + (width.getValue() / 2.0), hMiddle - gap, 0.5f, Color.black.getRGB(), color);
				// Bottom
				R2DUtils.drawBorderedRect(wMiddle - (width.getValue() / 2.0), hMiddle + gap,
						wMiddle + (width.getValue() / 2.0), hMiddle + gap + length.getValue(), 0.5f,
						Color.black.getRGB(), color);
			} else {
				int color = new Color(red.getValue().intValue(), green.getValue().intValue(),
						blue.getValue().intValue()).getRGB();
				int screenWidth = ((EventRender2D) event).getWidth();
				int screenHeight = ((EventRender2D) event).getHeight();
				int wMiddle = screenWidth / 2;
				int hMiddle = screenHeight / 2;
				boolean wider = dynamic.getValue() && mc.thePlayer.isMoving();
				double gap = wider ? dynamicgap.getValue() : sGap.getValue();
				// Left
				R2DUtils.drawBorderedRect(wMiddle - gap - length.getValue(), hMiddle - (width.getValue() / 2.0),
						wMiddle - gap, hMiddle + (width.getValue() / 2.0), 0.5f, Color.black.getRGB(), color);
				// Right
				R2DUtils.drawBorderedRect(wMiddle + gap, hMiddle - (width.getValue() / 2.0),
						wMiddle + gap + length.getValue(), hMiddle + (width.getValue() / 2.0), 0.5f,
						Color.black.getRGB(), color);
				// Top
				R2DUtils.drawBorderedRect(wMiddle - (width.getValue() / 2.0), hMiddle - gap - length.getValue(),
						wMiddle + (width.getValue() / 2.0), hMiddle - gap, 0.5f, Color.black.getRGB(), color);
				// Bottom
				R2DUtils.drawBorderedRect(wMiddle - (width.getValue() / 2.0), hMiddle + gap,
						wMiddle + (width.getValue() / 2.0), hMiddle + gap + length.getValue(), 0.5f,
						Color.black.getRGB(), color);
			}
		}

	}

	public static double rn = 1.5D;

	private int Color() {
		int cxd = 0;
		cxd = (int) (cxd + rn);
		if (cxd > 50) {
			cxd = 0;
		}
		Minecraft.getMinecraft();
		Color color = new Color(Color.HSBtoRGB(
				(float) (Minecraft.thePlayer.ticksExisted / 60.0D + Math.sin(cxd / 60.0D * 1.5707963267948966D)) % 1.0F,
				0.5882353F, 1.0F));
		int col = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).getRGB();
		return col;
	}

	public static Color fade(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}

}
