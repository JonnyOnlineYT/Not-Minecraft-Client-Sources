package net.minecraft.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.text.Component;

import java.util.List;

@RequiredArgsConstructor
public class AlertScreen extends Screen {
    private final Component title;
    private final Component text;
    private final Component ok;
    private final Runnable action;
    private List<String> textSplit;

    @Override
    public void init() {
        textSplit = font.split(text.asFormattedString(), width - 50);
        widgets.add(new ButtonWidget(width / 2 - 100, height - 28, ok.asFormattedString(), btn -> close()));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, title.asFormattedString(), width / 2, 30, -1);
        if (textSplit != null) {
            for (int i = 0; i < textSplit.size(); i++) {
                drawCenteredString(font, textSplit.get(i), width / 2, 50 + i * (font.height + 1), -1);
            }
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void close() {
        action.run();
    }
}
