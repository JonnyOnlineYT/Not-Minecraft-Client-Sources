package net.minecraft.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.moonware.client.utils.FontStorage;

public class PauseScreen extends Screen {
    private float animation;
    @Override
    public void init() {
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 24 + -16, I18n.format("menu.returnToGame"), btn -> close()));
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements"),
                btn -> Minecraft.openScreen(new GuiScreenAdvancements(Minecraft.player.connection.func_191982_f()))));
        widgets.add(new ButtonWidget(width / 2 + 2, height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats"),
                btn -> Minecraft.openScreen(new GuiStats(this, Minecraft.player.getStatFileWriter()))));
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 96 + -16, 98, 20, I18n.format("menu.options"),
                btn -> Minecraft.openScreen(new GuiOptions(this))));
        ButtonWidget button = new ButtonWidget(width / 2 + 2, height / 4 + 96 + -16, 98, 20, Minecraft
                .isIntegratedServerRunning() ? I18n.format("menu.shareToLan") : "Reconnect", btn -> {
            if (Minecraft.isIntegratedServerRunning()) {
                if (Minecraft.getIntegratedServer().getPublic()) return;
                Minecraft.openScreen(new GuiShareToLan(this));
                return;
            }
            ServerData data = minecraft.getServerData();
            Minecraft.world.quit();
            minecraft.loadWorld(null);
            Minecraft.openScreen(data != null ? new ConnectingScreen(new TitleScreen(), minecraft, data) : null);
        });
        button.enabled = !Minecraft.isIntegratedServerRunning() || !Minecraft.getIntegratedServer().getPublic();
        widgets.add(button);
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 120 + -16, I18n.format(Minecraft.isIntegratedServerRunning()
                ? "menu.returnToMenu" : "menu.disconnect"), btn -> {
            boolean singleplayer = Minecraft.isIntegratedServerRunning();
            Minecraft.world.quit();
            minecraft.loadWorld(null);
            Minecraft.openScreen(singleplayer ? new GuiWorldSelection() : new MultiplayerScreen());
        }));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        GlStateManager.pushMatrix();
        if (animation < 1F) animation += partialTick / 3F;
        drawDefaultBackground();
        GlStateManager.translate(width / 2F, height / 2F, 1F);
        GlStateManager.scale(animation * animation, animation * animation, animation * animation);
        GlStateManager.translate(-width / 2F, -height / 2F, 1);
        FontStorage.circleregular.drawCenter(I18n.format("menu.game"), width / 2F, 45, -1);
        super.draw(mouseX, mouseY, partialTick);
        GlStateManager.popMatrix();
    }
}
