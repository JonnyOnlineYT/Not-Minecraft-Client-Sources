package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.features.module.hidden.Menu;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.ui.tools.ToolsUI;
import org.spray.infinity.utils.render.FontUtils;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

	protected GameMenuScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void onPostInit(CallbackInfo ci) {
		this.addDrawableChild(
				new ButtonWidget(this.width / 2 - 60, this.height - 30, 120, 20, new LiteralText("Tools"), (button) -> {
					this.client.openScreen(new ToolsUI((Screen) this));
				}));
	}

	@Inject(method = "render", at = @At("TAIL"), cancellable = true)
	private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		int modKey = Infinity.getModuleManager().get(Menu.class).getKey();
		String key = modKey == 96
				? Formatting.BLUE + "GRAVE " + Formatting.GRAY + "\"" + Formatting.BLUE + " ` " + Formatting.GRAY
						+ "\" "
				: Formatting.BLUE + String.valueOf(InputUtil.fromKeyCode(modKey, modKey)).replace("key.keyboard.", "")
						.toUpperCase();
		FontUtils.drawStringWithShadow(matrices, "Open gui on the " + key + Formatting.WHITE + " key", 2, 3,
				0xFFFFFFFF);

	}

}
