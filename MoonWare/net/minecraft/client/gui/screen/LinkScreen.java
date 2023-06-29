package net.minecraft.client.gui.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.OS;

public class LinkScreen extends ConfirmScreen {
    public LinkScreen(Runnable callback, String link) {
        super(b -> {
            if (b) OS.openUri(link);
            callback.run();
        }, I18n.format("chat.link.confirm"), link, I18n.format("chat.link.open"), I18n.format("gui.cancel"));
    }

    @Override
    public void init() {
        widgets.add(new ButtonWidget(width / 2 - 50 - 105, height / 6 + 96, 100, 20, yes, btn -> callback.accept(true)));
        widgets.add(new ButtonWidget(width / 2 - 50, height / 6 + 96, 100, 20, I18n.format("chat.copy"), btn -> {
            OS.setClipboard(text);
            callback.accept(false);
        }));
        widgets.add(new ButtonWidget(width / 2 - 50 + 105, height / 6 + 96, 100, 20, no, btn -> callback.accept(false)));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        super.draw(mouseX, mouseY, partialTick);
        drawCenteredString(font, I18n.format("chat.link.warning"), width / 2, 110, 0xFFFF0000);
    }
}
