package net.minecraft.util.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SelectorComponent extends BaseComponent {
    @Getter private final String selector;

    @Override
    public String getString() {
        return selector;
    }

    @Override
    public SelectorComponent copy() {
        SelectorComponent component = new SelectorComponent(selector);
        component.setStyle(getStyle().createShallowCopy());
        for (Component c : getSiblings()) {
            component.append(c.copy());
        }
        return component;
    }
}
