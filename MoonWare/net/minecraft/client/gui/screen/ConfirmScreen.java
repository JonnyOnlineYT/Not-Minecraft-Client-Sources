package net.minecraft.client.gui.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;

import java.util.List;

@RequiredArgsConstructor
public class ConfirmScreen extends Screen {
    protected final BooleanConsumer callback;
    protected final String title;
    protected final String text;
    protected final String yes;
    protected final String no;
    private List<String> textSplit;

    public ConfirmScreen(BooleanConsumer callback, String title, String text) {
        this(callback, title, text, I18n.format("gui.yes"), I18n.format("gui.no"));
    }

    @Override
    public void init() {
        textSplit = font.split(text, width - 50);
        widgets.add(new ButtonWidget(width / 2 - 155, height / 6 + 96, 150, 20, yes, btn -> callback.accept(true)));
        widgets.add(new ButtonWidget(width / 2 - 155 + 160, height / 6 + 96, 150, 20, no, btn -> callback.accept(false)));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, title, width / 2, 70, -1);
        if (textSplit != null) {
            for (int i = 0; i < textSplit.size(); i++) {
                drawCenteredString(font, textSplit.get(i), width / 2, 90 + i * font.height, -1);
            }
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void close() {
        callback.accept(false);
    }
}
