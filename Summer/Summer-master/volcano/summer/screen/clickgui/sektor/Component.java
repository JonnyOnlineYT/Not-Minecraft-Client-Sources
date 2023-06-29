package volcano.summer.screen.clickgui.sektor;

import net.minecraft.client.Minecraft;

public abstract class Component {
	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected int x;
	protected int y;
	protected int absX;
	protected int absY;
	protected int width;
	protected int height;
	protected int renderWidth;
	protected int renderHeight;
	protected Component parent;
	protected boolean isVisible;

	public abstract void keyPressed(int var1);

	public abstract void draw(int var1, int var2, float var3, int var4, int var5);

	public abstract void mouseClicked(int var1, int var2, int var3);

	public abstract void mouseReleased(int var1, int var2, int var3);
}