package org.moonware.client.ui.titlescreen;

import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Session;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.moonware.client.utils.MWFont;

import java.util.*;
import java.util.stream.Collectors;

public class AltScreen extends TitleLikeScreen {
    private GuiTextField name;
    @Override
    public void init() {
        super.init();
        name = new GuiTextField(0, font, width / 2 - 75, height / 2, 150, 20);
        name.setMaxStringLength(16);
        name.setValidator(s -> s.matches("[a-zA-Z0-9_]*"));
        widgets.add(new ButtonWidget(width / 2 - 50, height / 2 + 24, 100, 20, "Войти", btn -> {
            if (name.getText().trim().isEmpty()) return;
            Minecraft.session = new Session(name.getText(), UUIDTypeAdapter.fromUUID(new UUID(0, 0)), "0", "legacy");
        }));
    }
    private  int random  = (int) (Math.random() * 3999);
    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawTitle();
        super.draw(mouseX, mouseY, partialTick);
        name.drawTextBox();
        StringBuilder frontText = new StringBuilder();
        int num = NumberUtils.isNumber(name.getText()) ? Integer.parseInt(name.getText()) : 0;
        int rnd = MathHelper.clamp(random,1,3999);
        MWFont.MONTSERRAT_BOLD.get(35).draw(num <= 0 || num >= 4000 ? "Ошибка : " + РИМСКОЕ(rnd)  :РИМСКОЕ(num), 200, 200, -1);
    }

    @Override
    public void update() {
        name.update();
        super.update();
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        name.mouseClicked(mouseX, mouseY, button);
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        random = (int) Math.random() * 3999;
        name.textboxKeyTyped(c, key);
        super.keyPressed(key, c);
    }
    public static String РИМСКОЕ(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List romanNumerals = РИМСКОЕ.sortAndReverse();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            РИМСКОЕ currentSymbol = (РИМСКОЕ) romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
    enum РИМСКОЕ {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        РИМСКОЕ(int value) {
            this.value = value;
        }

        public static List sortAndReverse() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((РИМСКОЕ e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
        public int getValue() {
            return value;
        }
    }
}
