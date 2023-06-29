package net.minecraft.util.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TextComponent extends BaseComponent {
    @Getter private final String text;

    @Override
    public String getString() {
        return text;
    }

    @Override
    public TextComponent copy() {
        TextComponent component = new TextComponent(text);
        component.setStyle(getStyle().createShallowCopy());
        for (Component c : getSiblings()) {
            component.append(c.copy());
        }
        return component;
    }
}
