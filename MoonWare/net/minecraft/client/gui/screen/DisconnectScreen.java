package net.minecraft.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Component;

import java.util.List;

@RequiredArgsConstructor
public class DisconnectScreen extends Screen {
    private final Screen parent;
    private final Component title;
    private final Component text;
    private List<String> textSplit;
    private int textHeight;

    @Override
    public void init() {
        textSplit = font.split(text.asFormattedString(), width - 50);
        textHeight = textSplit.size() * font.height;
        widgets.add(new ButtonWidget(width / 2 - 100, Math.min(height / 2 + textHeight / 2 + font.height,
                height - 30), I18n.format("gui.toMenu"), btn -> close()));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, title.asFormattedString(), width / 2, height / 2 - textHeight / 2 - font.height * 2, 11184810);
        int i = height / 2 - textHeight / 2;
        if (textSplit != null) {
            for (String s : textSplit) {
                drawCenteredString(font, s, width / 2, i, 16777215);
                i += font.height;
            }
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void close() {
        Minecraft.openScreen(parent);
    }
}
